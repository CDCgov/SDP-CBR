package gov.cdc.sdp.hl7v2filter;

import gov.cdc.sdp.hl7v2.filter.Expression;
import gov.cdc.sdp.hl7v2.filter.FilterBuilder;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InEqualityExpressionTest extends BaseTest {


    @Test
    public void testNumberEq() {
        Expression e = buildExpression("1.00 = 1.00");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("1.00 = 2.00");
        v = (Boolean)e.evaluate(null);
        assertFalse(v);
    }

    @Test
    public void testNumberGtEq() {
        
        Expression e = buildExpression("2.00 >= 1.00");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("1.00 >= 2.00");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("1.00 >= 1.00");
        v = (Boolean)e.evaluate(null);
        assertTrue( v);
    }

    @Test
    public void testNumberLtEq() {
        
        Expression e = buildExpression("1.00 <= 2.00");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e =buildExpression("2.00 <= 1.00");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("1.00 <= 1.00");
        v = (Boolean)e.evaluate(null);
        assertTrue( v);
    }

    @Test
    public void testNumberLt() {
        
        Expression e = buildExpression("1.00 < 2.00");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("2.00 < 1.00");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("1.00 < 1.00");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);
    }

    @Test
    public void testNumberGt() {
        
        Expression e = buildExpression("2.00 > 1.00");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("1.00 > 1.00");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("1.00 > 1.00");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);
    }


    @Test
    public void testDateTimeGt() {
        
        Expression e = buildExpression("@1999-10-10T10:10:10 > @1998-10-10T10:10:10");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("@1998-10-10T10:10:10 > @1999-10-10T10:10:11");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);
    }


    @Test
    public void testDateTimeGtEq() {
        
        Expression e = buildExpression("@1999-10-10T10:10:10 >= @1998-10-10T10:10:10");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("@1998-10-10T10:10:10 >= @1999-10-10T10:10:11");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("@1998-10-10T10:10:10 >= @1998-10-10T10:10:10");
        v = (Boolean)e.evaluate(null);
        assertTrue( v);
    }

    @Test
    public void testDateTimeLtEq() {
        
        Expression e = buildExpression("@1998-10-10T10:10:10 <= @1999-10-10T10:10:10");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("@1998-10-10T10:10:11 <= @1998-10-10T10:10:10");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("@1998-10-10T10:10:10 <= @1998-10-10T10:10:10");
        v = (Boolean)e.evaluate(null);
        assertTrue( v);
    }

    @Test
    public void testDateTimeLt() {
        
        Expression e = buildExpression("@1998-10-10T10:10:10 < @1999-10-10T10:10:10");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("@1998-10-10T10:10:11 < @1998-10-10T10:10:10");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("@1998-10-10T10:10:10 < @1998-10-10T10:10:10");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);
    }



    @Test
    public void testTimeGt() {
        
        Expression e = buildExpression("@T10:10:11 > @T10:10:10");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("@T10:10:10 > @T10:10:11");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("@T10:10:10 > @T10:10:10");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);
    }


    @Test
    public void testTimeGtEq() {
        
        Expression e = buildExpression("@T10:10:11 >= @T10:10:10");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("@T10:10:10 >= @T10:10:11");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("@T10:10:10 >= @T10:10:10");
        v = (Boolean)e.evaluate(null);
        assertTrue( v);
    }

    @Test
    public void testTimeLtEq() {
        
        Expression e = buildExpression("@T10:10:10 <= @T10:10:11");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("@T10:10:11 <= @T10:10:10");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("@T10:10:10 <= @T10:10:10");
        v = (Boolean)e.evaluate(null);
        assertTrue( v);
    }

    @Test
    public void testTimeLt() {
        
        Expression e = buildExpression("@T10:10:10 < @T10:10:11");
        Boolean v = (Boolean)e.evaluate(null);
        assertTrue( v);

        e = buildExpression("@T10:10:11 < @T10:10:10");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("@T10:10:10 < @T10:10:10");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);
    }



    @Test
    public void testBooleanGt() {
        
        Expression e = buildExpression("true > true");
        Boolean v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("true > false");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);
    }

    @Test
    public void testBooleanGtEq() {
        
        Expression e = buildExpression("true >= true");
        Boolean v = (Boolean)e.evaluate(null);
        assertFalse( v);

        e = buildExpression("true >= false");
        v = (Boolean)e.evaluate(null);
        assertFalse( v);
    }


}
