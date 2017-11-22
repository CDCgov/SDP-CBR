package gov.cdc.sdp.hl7v2filter;

import gov.cdc.sdp.hl7v2.filter.*;
import org.junit.Test;

import static org.junit.Assert.*;



public class LiteralExpressionTest extends  BaseTest{

    @Test
    public void test(){
        String a = "";
        Expression e = new LiteralExpression(a);
        Object v = e.evaluate(null);
        assert a == v;
    }

    @Test
    public void testBoolean(){
        
        Expression e = buildExpression("true");
        assert (e instanceof LiteralExpression);
        Boolean v = (Boolean)((LiteralExpression)e).evaluate(null);
        assert v;

         e = buildExpression("false");
        assert (e instanceof LiteralExpression);
         v = (Boolean)((LiteralExpression)e).evaluate(null);
        assert !v;
    }



    @Test
    public void testNumber(){
        
        Expression e = buildExpression("1.00");
        assert (e instanceof LiteralExpression);
        Number v = (Number)((LiteralExpression)e).evaluate(null);
        assertTrue( v.doubleValue() == 1.00);
    }


    @Test
    public void testNull(){
        
        Expression e = buildExpression("null");
        assert (e instanceof LiteralExpression);
        Object v = ((LiteralExpression)e).evaluate(null);
        assertNull(null);
    }


    @Test
    public void testTime(){
        
        Expression e = buildExpression("@T10:10:10");
        assert (e instanceof LiteralExpression);
        Time v = (Time)((LiteralExpression)e).evaluate(null);
        assertEquals( v.getHour(),10);
        assertEquals( v.getMinute(),10);
        assertEquals( v.getSecond(),10);
        assertEquals( v.getMillisecond(), 0);
    }

    @Test
    public void testDateTime(){
        
        Expression e = buildExpression("@1999-01-01T10:10:10");
        assert (e instanceof LiteralExpression);
        DateTime v = (DateTime)((LiteralExpression)e).evaluate(null);
        assertEquals (v.getYear() ,1999);
        assertEquals (v.getMonth() , 1);
        assertEquals (v.getDay() , 1);
        assertEquals( v.getHour(),10);
        assertEquals( v.getMinute(),10);
        assertEquals( v.getSecond(),10);
        assertEquals( v.getMillisecond(), 0);
    }

}
