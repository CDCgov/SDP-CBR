package gov.cdc.sdp.hl7v2filter;

import gov.cdc.sdp.hl7v2.filter.AndExpression;
import gov.cdc.sdp.hl7v2.filter.Expression;
import gov.cdc.sdp.hl7v2.filter.FilterBuilder;
import gov.cdc.sdp.hl7v2.filter.LiteralExpression;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class AndExpressionTest extends  BaseTest{

    @Test
    public void testBooleanExpressions(){
        LiteralExpression left = new LiteralExpression(true);
        LiteralExpression right = new LiteralExpression(true);
        Expression and = new AndExpression(left,right);

        Object v = and.evaluate(null);
        assert (Boolean)v;

        left.setValue((Boolean)false);
        v = and.evaluate(null);
         assertTrue(!(Boolean)v);

        right.setValue(false);
        v = and.evaluate(null);
        assertTrue(!(Boolean)v);

        left.setValue(true);
        v = and.evaluate(null);
        assertTrue( !(Boolean)v);

        left.setValue(null);
        v = and.evaluate(null);
        assertTrue( !(Boolean)v);

    }
    @Test
    public void testListExpressions(){
        List lst = new ArrayList();
        LiteralExpression left = new LiteralExpression(lst);
        LiteralExpression right = new LiteralExpression(true);
        Expression and = new AndExpression(left,right);

        left.setValue(lst);
        Object v = and.evaluate(null);
        assertTrue(!(Boolean)v);

        lst.add("hey");
        v = and.evaluate(null);
        assertTrue((Boolean)v);

        right.setValue(false);
        v = and.evaluate(null);
        assertTrue(!(Boolean)v);
    }


    @Test
    public void testBoolean() {
        Expression e = buildExpression("true and true");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue(v);

        e = buildExpression("true and false");
        v = (Boolean)e.evaluate(null);
        assertTrue(!v);
    }

    @Test
    public void testMixedBoolean() {
        Expression e = buildExpression("1.0 and true");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue(v);

        e = buildExpression("1.0 and false");
        v = (Boolean)e.evaluate(null);
        assertTrue(!v);
    }

    @Test
    public void testGroupedBoolean() {
        Expression e = buildExpression("(true and (true and true))");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue(v);

        e = buildExpression("(true and (true and false))");
        v = (Boolean)e.evaluate(null);
        assertTrue(!v);
    }

}
