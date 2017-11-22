package gov.cdc.sdp.hl7v2filter;

import gov.cdc.sdp.hl7v2.filter.FilterBuilder;
import gov.cdc.sdp.hl7v2.filter.OrExpression;
import gov.cdc.sdp.hl7v2.filter.Expression;
import gov.cdc.sdp.hl7v2.filter.LiteralExpression;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
public class OrExpressionTest extends BaseTest{

    @Test
    public void testBooleanExpressions(){
        LiteralExpression left = new LiteralExpression(true);
        LiteralExpression right = new LiteralExpression(true);
        Expression or = new OrExpression(left,right);

        Object v = or.evaluate(null);
        assertTrue( (boolean)v);

        left.setValue((Boolean)false);
        v = or.evaluate(null);
        assertTrue( (boolean)v);

        right.setValue(false);
        v = or.evaluate(null);
        assertFalse( (boolean)v);

        left.setValue(true);
        v = or.evaluate(null);
        assertTrue( (boolean)v);

        left.setValue(null);
        v = or.evaluate(null);
        assertFalse( (boolean)v);

    }
    @Test
    public void testListExpressions(){
        List lst = new ArrayList();
        LiteralExpression left = new LiteralExpression(lst);
        LiteralExpression right = new LiteralExpression(true);
        Expression and = new OrExpression(left,right);

        left.setValue(lst);
        Object v = and.evaluate(null);
        assertTrue( (boolean)v);

        lst.add("hey");
        v = and.evaluate(null);
        assertTrue( (boolean)v);

        right.setValue(false);
        v = and.evaluate(null);
        assertTrue( (boolean)v);

        left.setValue(null);
        v = and.evaluate(null);
        assertFalse( (boolean)v);
    }


    @Test
    public void testBoolean() {
        
        Expression e = buildExpression("true or true");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("false or false");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);
    }

    @Test
    public void testMixedBoolean() {
        
        Expression e = buildExpression("null or true");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("null or false");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);
    }

    @Test
    public void testGroupedBoolean() {
        
        Expression e = buildExpression("(true or (false and true))");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("(false or (true and false))");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);
    }
}
