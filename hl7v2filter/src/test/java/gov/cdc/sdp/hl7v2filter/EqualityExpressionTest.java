package gov.cdc.sdp.hl7v2filter;

import gov.cdc.sdp.hl7v2.filter.Expression;
import gov.cdc.sdp.hl7v2.filter.FilterBuilder;
import org.junit.Test;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualityExpressionTest extends BaseTest{



    @Test
    public void testBooleanEq() {
        Expression e = buildExpression("true = true");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue(v);

        e = buildExpression("true = false");
        v = (Boolean)e.evaluate(null);
        assertFalse(v);
    }

    @Test
    public void testBooleanNotEq() {
        Expression e = buildExpression("true != true");
        Boolean v = (Boolean)e.evaluate(null);
        assertFalse(v);

        e = buildExpression("true != false");
        v = (Boolean)e.evaluate(null);
        assertTrue( v);
    }

    @Test
    public void testTimeEq() {
        Expression e = buildExpression("@T10:10:10 = @T10:10:10");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e =buildExpression("@T10:10:10 = @T10:10:11");
        v = (Boolean)e.evaluate(null);
        assertFalse(v);
    }

    @Test
    public void testTimeNotEq() {

        Expression e = buildExpression("@T10:10:10 != @T10:10:10");
        Boolean v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("@T10:10:10 != @T10:10:11");
        v = (Boolean)e.evaluate(null);
        assertTrue( v);
    }

    @Test
    public void testDateTimeEq() {
        Expression e = buildExpression("@1999-10-10T10:10:10 = @1999-10-10T10:10:10");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("@1999-10-10T10:10:10 = @1999-10-10T10:10:11");
        v = (Boolean)e.evaluate(null);
        assertFalse(v);
    }




}
