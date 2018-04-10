package gov.cdc.sdp.cbr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.model.PhinMSSourceAttributes;
import gov.cdc.sdp.cbr.model.SDPMessage;

public class SerializerTest {

    @Test
    public void testSerialization() {
        String sample = "{\"batch\":true,\"batchId\":\"batch_id_1\",\"batchIndex\":12993399,\"cbrReceivedTime\":\"officia nulla\",\"id\":\"dolor fugiat nulla do\",\"payload\":\"in est ipsum Lorem\",\"recipient\":\"laborum nulla minim amet\",\"sender\":\"ullamco nulla\",\"source\":\"amet exercitation do esse mollit\",\"source_id\":\"cillum nisi labore officia\",\"source_received_time\":\"sit aliquip commodo\","
                + "\"sourceAttributes\": {\"PAYLOADNAME\":\"TEST1\",\"ERRORCODE\":\"0\"}}";

        Gson gson = new Gson();
        SDPMessage msg = gson.fromJson(sample, SDPMessage.class);
        assertTrue(msg.isBatch());
        assertEquals("batch_id_1", msg.getBatchId());
        assertNotNull(msg.getSourceAttributes());
        assertEquals("0", msg.getSourceAttributes().get(PhinMSSourceAttributes.ERRORCODE));
        msg.toString();
    }

}
