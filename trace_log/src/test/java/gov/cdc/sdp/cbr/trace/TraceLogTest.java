package gov.cdc.sdp.cbr.trace;

import static org.junit.Assert.*;

import java.io.InvalidObjectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import gov.cdc.sdp.cbr.trace.model.TraceLog;
import gov.cdc.sdp.cbr.trace.model.TraceStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:context.xml" })
@WebAppConfiguration
@PropertySource("classpath:application.properties")
public class TraceLogTest {
    
    private static DataSource ds = null;
    
    @Autowired
    TraceService traceService;
    
    @Autowired
    CamelContext camelContext;
    
    @Before
    public void setUp() {
        if (TraceLogTest.ds == null) {
            TraceLogTest.ds = (DataSource) camelContext.getRegistry().lookupByName("traceLogDs");
        }
    }
    
    @Test
    public void testSetup() {
        assertNotNull(traceService);
    }
    
    @Test
    public void testLogCreation() throws SQLException {
        Connection conn = ds.getConnection();
        confirmEmptyTable(ds, conn);
        traceService.addTraceMessage("test123", "src", TraceStatus.WARN, "This is a test");
        
        PreparedStatement ps = conn.prepareStatement("select * from trace_log_test");
        ResultSet rs = ps.executeQuery();
        assertTrue(rs.next());
        String cbrId = rs.getString("cbr_id");
        String description = rs.getString("description");
        int status = rs.getInt("status");
        String source = rs.getString("source");
        Timestamp createdAt = rs.getTimestamp("created_at");
        
        assertEquals("test123", cbrId);
        assertEquals(TraceStatus.WARN.getLevel(), status);
        assertEquals("This is a test", description);
        assertEquals("src", source);
        assertTrue(new Date().getTime() - 1000 < createdAt.getTime());
        
        assertFalse(rs.next());
    }

    @Test
    public void testGetStatusValid() throws SQLException, InvalidObjectException, IllegalArgumentException {
        Connection conn = ds.getConnection();
        confirmEmptyTable(ds, conn);
        
        traceService.addTraceMessage("test123", "src", TraceStatus.WARN, "This is a test");
        traceService.addTraceMessage("test123", "src", TraceStatus.INFO, "This is a test");
        traceService.addTraceMessage("test123", "src", TraceStatus.ERROR, "This is a test");
        traceService.addTraceMessage("test123", "src", TraceStatus.WARN, "This is a test");
        TraceStatus status = traceService.getStatus("test123");
        assertEquals (TraceStatus.ERROR, status);
        
        traceService.addTraceMessage("test124", "src", TraceStatus.INFO, "This is a test");
        status = traceService.getStatus("test124");
        assertEquals (TraceStatus.INFO, status);
        
        traceService.addTraceMessage("test125", "src", TraceStatus.ERROR, "This is a test");
        status = traceService.getStatus("test125");
        assertEquals (TraceStatus.ERROR, status);
    }
    
    @Test( expected = InvalidObjectException.class)
    public void testGetStatusInvalidStatus() throws SQLException, InvalidObjectException, IllegalArgumentException {
        Connection conn = ds.getConnection();
        confirmEmptyTable(ds, conn);
        
        // Setup

        PreparedStatement ps = conn.prepareStatement("insert into  trace_log_test "
                + "(cbr_id, status, description, created_at, source)" 
                + " values (?,?,?,?,?)");
        ps.setString(1,"sdasd");
        ps.setInt(2,27);
        ps.setString(3,"stuff");
        ps.setTimestamp(4, new Timestamp(new Date().getTime()));
        ps.setString(5,"src");
        ps.executeUpdate();
        ps.close();
        
        traceService.getStatus("sdasd");
        
    }
    
    @Test( expected = IllegalArgumentException.class)
    public void testGetStatusNoLogs() throws SQLException, InvalidObjectException, IllegalArgumentException {
        Connection conn = ds.getConnection();
        confirmEmptyTable(ds, conn);
    
        try {
            traceService.getStatus("test123");
            
        } finally {
            conn.close();
        }
        fail ("Test should exit with an exception condition before this assertion");
        
        
    }
    
    @Test
    public void getMessages() throws SQLException, InvalidObjectException {
        Connection conn = ds.getConnection();
        confirmEmptyTable(ds, conn);
     
        traceService.addTraceMessage("test123", "src", TraceStatus.WARN, "0");
        traceService.addTraceMessage("test123", "src", TraceStatus.INFO, "1");
        traceService.addTraceMessage("test123", "src", TraceStatus.ERROR, "2");
        traceService.addTraceMessage("test123", "src", TraceStatus.WARN, "3");
     

        traceService.addTraceMessage("miscMsg", "src", TraceStatus.WARN, "trash");
        traceService.addTraceMessage("otherMsg", "src", TraceStatus.INFO, "moreTrash");
        
        List<TraceLog> messages = traceService.getTrace("test123");
        assertNotNull(messages);
        assertEquals(4, messages.size());
        assertEquals("0", messages.get(0).getDescription());
        assertEquals("1", messages.get(1).getDescription());
        assertEquals("2", messages.get(2).getDescription());
        assertEquals("3", messages.get(3).getDescription());
        assertEquals("src", messages.get(3).getSource());
        

        messages = traceService.getTrace("miscMsg");
        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals("trash", messages.get(0).getDescription());
        assertEquals(TraceStatus.WARN, messages.get(0).getStatus());
        
    }
    
    // We should allow for creation of audit log statements for individual messages within a batch message.
    // We should be able to create trace messages for a batch message and get them either as part of the
    // batch's audit log or the audit log for a specific message.
    //
    // Example:
    // cbrOriginatingId: "CBR_ID_1"     "CBR_ID_1"     "CBR_ID_1"     "CBR_ID_1"
    // cbrId:            "CBR_ID_1_1"   "CBR_ID_1_2"   "CBR_ID_1_2"   "CBR_ID_1"
    // status:           "ERROR"        "INFO"         "INFO"         "WARN"
    // src:              "src"          "src"          "src"          "src"
    // description:      "test1"        "test2"        "test3"        "test4"
    //
    // "CBR_ID_1" is the id of the incoming batch message from the content provider.
    // getStatus("CBR_ID_1")   --> ERROR
    // getStatus("CBR_ID_1_1") --> ERROR
    // getStatus("CBR_ID_1_2") --> INFO
    //
    // getTrace("CBR_ID_1")    --> 4 msgs
    // getTrace("CBR_ID_1_1")  --> 1 msgs
    // getTrace("CBR_ID_1_2")  --> 2 msg
    
    @Test
    public void testGetStatusForBatchMessage() {
        // Need to add new param
        
        // traceService.addTraceMessage("CBR_ID_1", "CBR_ID_1", "src", TraceStatus.WARN, "0");
        // traceService.addTraceMessage("CBR_ID_1", "CBR_ID_1_1", "src", TraceStatus.ERROR, "1");
        // traceService.addTraceMessage("CBR_ID_1", "CBR_ID_1_2", "src", TraceStatus.INFO, "2");
        // traceService.addTraceMessage("CBR_ID_1", "CBR_ID_1_2", "src", TraceStatus.INFO, "3");
        
        // assertEquals(ERROR, traceService.getStatus("CBR_ID_1"));
        // assertEquals(ERROR, traceService.getStatus("CBR_ID_1_1"));
        // assertEquals(INFO, traceService.getStatus("CBR_ID_1_2"));
    }
    
    @Test
    public void testGetTraceForBatchMessage() {
        // Need to add new param
        
        // traceService.addTraceMessage("CBR_ID_1", "CBR_ID_1", "src", TraceStatus.WARN, "0");
        // traceService.addTraceMessage("CBR_ID_1", "CBR_ID_1_1", "src", TraceStatus.ERROR, "1");
        // traceService.addTraceMessage("CBR_ID_1", "CBR_ID_1_2", "src", TraceStatus.INFO, "2");
        // traceService.addTraceMessage("CBR_ID_1", "CBR_ID_1_2", "src", TraceStatus.INFO, "3");
        
        // List<TraceLog> lst = traceService.getTrace("CBR_ID_1");
        // assertions here
        // List<TraceLog> lst = traceService.getTrace("CBR_ID_1_1");
        // assertions here
        // List<TraceLog> lst = traceService.getTrace("CBR_ID_1_2");
        // assertions here
        
    }
    
    @After
    public void tearDown() throws SQLException {
        Connection conn = ds.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM trace_log_test");
        ps.execute();
        ps.close();
        conn.close();
    }
    
    @AfterClass
    public static void afterClass() throws SQLException {
        Connection conn = ds.getConnection();
        PreparedStatement ps = conn.prepareStatement("DROP TABLE if exists trace_log_test");
        ps.execute();
        ps.close();
        conn.close();
    }

    // HELPER METHODS
    private void confirmEmptyTable(DataSource ds, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("select count(*) from trace_log_test");
        ResultSet rs = ps.executeQuery();
        rs.next();
        int initialCount = rs.getInt(1);
        assertEquals(0,initialCount);
    }
   
}
