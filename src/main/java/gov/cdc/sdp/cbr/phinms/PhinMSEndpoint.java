package gov.cdc.sdp.cbr.phinms;

import javax.sql.DataSource;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

@UriEndpoint(scheme = "phinms", title = "PHIN-MS", syntax = "phinms:tableName", label = "database,sql,phinms")
public class PhinMSEndpoint extends DefaultEndpoint {

    @UriParam(description = "Sets the DataSource to use to communicate with the database.")
    private DataSource dataSource;

    private String tableName;

    public PhinMSEndpoint(String uri, Component component, DataSource ds, String tableName) {
        super(uri, component);
        this.dataSource = ds;
        this.tableName = tableName;
    }

    @Override
    public Producer createProducer() throws Exception {
        return new PhinMSProducer(this, getEndpointUri(), dataSource, tableName);
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return null;
    }
}
