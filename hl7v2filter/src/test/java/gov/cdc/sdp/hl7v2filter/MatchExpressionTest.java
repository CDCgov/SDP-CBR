package gov.cdc.sdp.hl7v2filter;

import gov.cdc.sdp.hl7v2.filter.Expression;
import gov.cdc.sdp.hl7v2.filter.Filter;
import gov.cdc.sdp.hl7v2.filter.FilterBuilder;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatchExpressionTest extends BaseTest{


    @Test
    public void testMatches() {
        Filter e = buildFilter("'Hey there people' matches '^Hey.*'");
        Boolean v = e.evaluate(null);
        assertTrue(v);

        e = buildFilter("'Hey there people' matches '^Stop'");
         v = e.evaluate(null);
        assertFalse(v);
    }
}
