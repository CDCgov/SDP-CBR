package gov.cdc.sdp.hl7v2filter;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import gov.cdc.sdp.hl7v2.filter.Context;
import gov.cdc.sdp.hl7v2.filter.Expression;
import gov.cdc.sdp.hl7v2.filter.Filter;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class PathExpressionTest extends BaseTest{

    String sourceFile = "src/test/resources/hl7v2.txt";

    public Message parseMessage(String msg){
       try {
           HapiContext context = new DefaultHapiContext();
           Parser p = context.getGenericParser();
           return p.parse(msg);
       }catch (Exception e){
           return null;
       }
    }
    @Test
    public void testCreateSegmentPath() throws IOException{
        
        Expression e = buildExpression("PID-5(*)-1");
        Context ctx = new Context();
        ctx.set("MESSAGE", parseMessage(readFile(sourceFile)));
        List v = (List)e.evaluate(ctx);
        assert v.contains("SMITH");
        assertEquals(1,v.size());
    }

    @Test
    public void testCreateGroupPath() {
        
        Expression e = buildExpression("Observation/MSH-1-1-1");
        assertNotNull(e); // != null;
    }

    @Test
    public void testCreateMultiGroupPath() {
        
        Expression e = buildExpression("/Ob/Observation/MSH-1-1-1");
        assertNotNull(e);
    }


    @Test
    public void testSegmentPathEq() throws IOException{
        
        Expression e = buildExpression("PID-5(*)-1 = 'SMITH'");
        Context ctx = new Context();
        ctx.set("MESSAGE", parseMessage(readFile(sourceFile)));
        assertTrue((Boolean)e.evaluate(ctx));

    }

    @Test
    public void testEvalGroupPathContains()  throws IOException{
        String sf = "src/test/resources/hl7v2_repetitions.txt";
        Filter e = buildFilter("/OBSERVATION/OBX contains 'Preg'");
        assertTrue((Boolean)e.evaluate(parseMessage(readFile(sf))));
    }

    @Test
    public void testEvalGroupContraintPathContains()  throws IOException{
        String sf = "src/test/resources/hl7v2_repetitions.txt";
        
        Expression e = buildExpression("/OBSERVATION[OBX-3-2 contains 'Pregnancy' and OBX-5-1 = 'N']/ != null");
        Context ctx = new Context();
        ctx.set("MESSAGE", parseMessage(readFile(sf)));
        assertTrue((Boolean)e.evaluate(ctx));
    }

    @Test
    public void testEvalSegmentGt()  throws IOException{
        String sf = "src/test/resources/hl7v2_repetitions.txt";
        
        Expression e = buildExpression("PID-7 > @1960-01-01");
        Context ctx = new Context();
        ctx.set("MESSAGE", parseMessage(readFile(sf)));
        assertTrue((Boolean)e.evaluate(ctx));

        e = buildExpression("PID-7 < @1960-01-01");
        ctx = new Context();
        ctx.set("MESSAGE", parseMessage(readFile(sf)));
        assertFalse((Boolean)e.evaluate(ctx));

    }
}
