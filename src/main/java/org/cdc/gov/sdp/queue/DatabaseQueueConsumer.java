package org.cdc.gov.sdp.queue;

import static org.springframework.jdbc.support.JdbcUtils.closeResultSet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.sql.DataSource;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.RollbackExchangeException;
import org.apache.camel.impl.ScheduledBatchPollingConsumer;
import org.apache.camel.util.CastUtils;
import org.apache.camel.util.ObjectHelper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

public class DatabaseQueueConsumer extends ScheduledBatchPollingConsumer {
	private String tableName;
	private JdbcTemplate jdbcTemplate;

	private final String query;
	private String onConsumeFailed;
	private String onConsume;
	private String onConsumeBatchComplete;

	private String[] default_headers = { "id", "cbr_id", "source", "source_id", "source_attributes", "batch",
			"batch_index", "payload", "cbr_recevied_time", "sender", "recipient", "attempts", "status", "created_at",
			"updated_at" };

	private boolean breakBatchOnConsumeFail = false;

	private class ocPreparedStatementCallback<T> implements PreparedStatementCallback<Integer> {
		public ocPreparedStatementCallback(Integer recordId) {
			this.recordId = recordId;
		}

		private Integer recordId;

		@Override
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException {
			log.trace("Execute query {}", query);
			ps.setInt(1, this.recordId);
			ps.execute();
			int uc = ps.getUpdateCount();
			log.trace("Update count {}", uc);
			return uc;
		};

	}

	public DatabaseQueueConsumer(Endpoint endpoint, DataSource ds, Processor processor, String tn) {
		super(endpoint, processor);
		this.jdbcTemplate = new JdbcTemplate(ds);
		this.tableName = tn;

		this.query = "SELECT * FROM " + tableName + " WHERE attempts = 0 and status = 'queued'";
		this.onConsumeFailed = "UPDATE " + tableName + " SET status = 'failed' where id=?";
		this.onConsume = "UPDATE " + tableName + " SET status = 'sent' where id=?";
		this.onConsumeBatchComplete = "SELECT * FROM " + tableName;
	}

	@Override
	public DatabaseQueueEndpoint getEndpoint() {
		return (DatabaseQueueEndpoint) super.getEndpoint();
	}

	private static final class DataHolder {
		private Exchange exchange;
		private Object data;

		private DataHolder() {
		}
	}

	@Override
	protected int poll() throws Exception {
		shutdownRunningTask = null;
		pendingExchanges = 0;

		String preparedQuery = query;
		log.trace("poll: {}", preparedQuery);

		final PreparedStatementCallback<Integer> callback = new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				Queue<DataHolder> answer = new LinkedList<DataHolder>();

				log.debug("Executing query: {}", preparedQuery);
				ResultSet rs = ps.executeQuery();

				boolean closeEager = true;
				try {
					log.trace("Got result list from query: {}", rs);
					List<?> data = getEndpoint().queryForList(default_headers, rs, true);
					addListToQueue(data, answer);
				} finally {
					if (closeEager) {
						closeResultSet(rs);
					}
				}

				// process all the exchanges in this batch
				try {
					if (answer.isEmpty()) {
						// no data
						return 0;
					} else {
						int rows = processBatch(CastUtils.cast(answer));
						return rows;
					}
				} catch (Exception e) {
					throw ObjectHelper.wrapRuntimeCamelException(e);
				} finally {
					closeResultSet(rs);
				}
			}
		};

		Integer messagePolled = jdbcTemplate.execute(preparedQuery, callback);
		return messagePolled;
	}

	private void addListToQueue(Object data, Queue<DataHolder> answer) {
		if (data instanceof List) {
			// create a list of exchange objects with the data
			List<?> list = (List) data;
			for (Object item : list) {
				addItemToQueue(item, answer);
			}
		} else {
			// create single object as data
			addItemToQueue(data, answer);
		}
	}

	private void addItemToQueue(Object item, Queue<DataHolder> answer) {
		Exchange exchange = createExchange(item);
		DataHolder holder = new DataHolder();
		holder.exchange = exchange;
		holder.data = item;
		answer.add(holder);
	}

	// @SuppressWarnings("unchecked")
	protected Exchange createExchange(Object data) {
		final Exchange exchange = getEndpoint().createExchange(ExchangePattern.InOnly);
		Message msg = exchange.getIn();
		Map<String, Object> new_headers = msg.getHeaders();
		HashMap<String, Object> data_hash = (HashMap<String, Object>) data;

		for (String header : data_hash.keySet()) {
			new_headers.put(header, data_hash.get(header));
		}

		msg.setBody(data);
		msg.setHeaders(new_headers);

		return exchange;
	}

	@Override
	public int processBatch(Queue<Object> exchanges) throws Exception {
		int total = exchanges.size();

		if (maxMessagesPerPoll > 0 && total == maxMessagesPerPoll) {
			log.debug("Maximum messages to poll is {} and there were exactly {} messages in this poll.",
					maxMessagesPerPoll, total);
		}

		for (int index = 0; index < total && isBatchAllowed(); index++) {
			// only loop if we are started (allowed to run)
			DataHolder holder = ObjectHelper.cast(DataHolder.class, exchanges.poll());
			Exchange exchange = holder.exchange;
			Object data = holder.data;

			// add current index and total as properties
			exchange.setProperty(Exchange.BATCH_INDEX, index);
			exchange.setProperty(Exchange.BATCH_SIZE, total);
			exchange.setProperty(Exchange.BATCH_COMPLETE, index == total - 1);

			// update pending number of exchanges
			pendingExchanges = total - index - 1;

			// process the current exchange
			try {
				getProcessor().process(exchange);
			} catch (Exception e) {
				exchange.setException(e);
			}

			if (exchange.isFailed()) {
				// we should rollback
				Exception cause = exchange.getException();
				if (cause != null) {
					throw cause;
				} else {
					throw new RollbackExchangeException("Rollback transaction due error processing exchange", exchange);
				}
			}

			// pick the on consume to use
			String sql = exchange.isFailed() ? onConsumeFailed : onConsume;

			try {
				// we can only run on consume if there was data
				if (data != null && sql != null) {
					Message b = exchange.getIn();
					Map<String, Object> h = exchange.getIn().getHeaders();
					String hid = exchange.getIn().getHeader("id").toString();
					Integer rid = new Integer(hid);
					int updateCount = jdbcTemplate.execute(sql, new ocPreparedStatementCallback<Integer>(rid));
				}
			} catch (Exception e) {
				if (breakBatchOnConsumeFail) {
					throw e;
				} else {
					handleException("Error executing onConsume/onConsumeFailed query" + sql, e);
				}
			}
		}

		try {
			if (onConsumeBatchComplete != null) {
				// TODO
				// int updateCount =
				// jdbcTemplate.execute(onConsumeBatchComplete, new
				// ocPreparedStatementCallback<Integer>(rid));
			}
		} catch (Exception e) {
			if (breakBatchOnConsumeFail) {
				throw e;
			} else {
				handleException("Error executing onConsumeBatchComplete query " + onConsumeBatchComplete, e);
			}
		}
		return total;
	}
}
