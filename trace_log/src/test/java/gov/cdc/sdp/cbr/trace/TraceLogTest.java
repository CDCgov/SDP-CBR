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
        traceService.addTraceMessage("test123", TraceStatus.WARN, "This is a test");
        
        PreparedStatement ps = conn.prepareStatement("select * from trace_log_test");
        ResultSet rs = ps.executeQuery();
        assertTrue(rs.next());
        String cbrId = rs.getString("cbr_id");
        String description = rs.getString("description");
        int status = rs.getInt("status");
        Timestamp createdAt = rs.getTimestamp("created_at");
        
        assertEquals("test123", cbrId);
        assertEquals(TraceStatus.WARN.getLevel(), status);
        assertEquals("This is a test", description);
        assertTrue(new Date().getTime() - 1000 < createdAt.getTime());
        
        assertFalse(rs.next());
    }

    @Test
    public void testGetStatusValid() throws SQLException, InvalidObjectException, IllegalArgumentException {
        Connection conn = ds.getConnection();
        confirmEmptyTable(ds, conn);
        
        traceService.addTraceMessage("test123", TraceStatus.WARN, "This is a test");
        traceService.addTraceMessage("test123", TraceStatus.INFO, "This is a test");
        traceService.addTraceMessage("test123", TraceStatus.ERROR, "This is a test");
        traceService.addTraceMessage("test123", TraceStatus.WARN, "This is a test");
        TraceStatus status = traceService.getStatus("test123");
        assertEquals (TraceStatus.ERROR, status);
        
        traceService.addTraceMessage("test124", TraceStatus.INFO, "This is a test");
        status = traceService.getStatus("test124");
        assertEquals (TraceStatus.INFO, status);
        
        traceService.addTraceMessage("test125", TraceStatus.ERROR, "This is a test");
        status = traceService.getStatus("test125");
        assertEquals (TraceStatus.ERROR, status);
    }
    
    @Test( expected = InvalidObjectException.class)
    public void testGetStatusInvalidStatus() throws SQLException, InvalidObjectException, IllegalArgumentException {
        Connection conn = ds.getConnection();
        confirmEmptyTable(ds, conn);
        
        // Setup

        PreparedStatement ps = conn.prepareStatement("insert into  trace_log_test "
                + "(cbr_id, status, description, created_at)" 
                + " values (?,?,?,?)");
        ps.setString(1,"sdasd");
        ps.setInt(2,27);
        ps.setString(3,"stuff");
        ps.setTimestamp(4, new Timestamp(new Date().getTime()));
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
     
        traceService.addTraceMessage("test123", TraceStatus.WARN, "0");
        traceService.addTraceMessage("test123", TraceStatus.INFO, "1");
        traceService.addTraceMessage("test123", TraceStatus.ERROR, "2");
        traceService.addTraceMessage("test123", TraceStatus.WARN, "3");
     

        traceService.addTraceMessage("miscMsg", TraceStatus.WARN, "trash");
        traceService.addTraceMessage("otherMsg", TraceStatus.INFO, "moreTrash");
        
        List<TraceLog> messages = traceService.getTrace("test123");
        assertNotNull(messages);
        assertEquals(4, messages.size());
        assertEquals("0", messages.get(0).getDescription());
        assertEquals("1", messages.get(1).getDescription());
        assertEquals("2", messages.get(2).getDescription());
        assertEquals("3", messages.get(3).getDescription());
        

        messages = traceService.getTrace("miscMsg");
        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals("trash", messages.get(0).getDescription());
        assertEquals(TraceStatus.WARN, messages.get(0).getStatus());
        
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
