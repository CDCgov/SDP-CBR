package org.cdc.gov.sdp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.cdc.gov.sdp.model.PhinMSSourceAttributes;
import org.cdc.gov.sdp.model.SDPMessage;
import org.junit.Test;

import com.google.gson.Gson;

public class SerializerTest {

	@Test
	public void testSerialization() {
		String sample = "{\"batch\":true,\"batch_id\":\"batch_id_1\",\"batch_index\":12993399,\"cbr_received_time\":\"officia nulla\",\"id\":\"dolor fugiat nulla do\",\"payload\":\"in est ipsum Lorem\",\"recipient\":\"laborum nulla minim amet\",\"sender\":\"ullamco nulla\",\"source\":\"amet exercitation do esse mollit\",\"source_id\":\"cillum nisi labore officia\",\"source_received_time\":\"sit aliquip commodo\","
				+ "\"source_attributes\": {\"PAYLOADNAME\":\"TEST1\",\"ERRORCODE\":\"0\"}}";

		Gson gson = new Gson();
		SDPMessage msg = gson.fromJson(sample, SDPMessage.class);
		assertTrue(msg.isBatch());
		assertEquals("batch_id_1", msg.getBatchId());
		assertNotNull(msg.getSourceAttributes());
		assertEquals("0", msg.getSourceAttributes().get(PhinMSSourceAttributes.ERRORCODE));
		msg.toString();
	}

}
