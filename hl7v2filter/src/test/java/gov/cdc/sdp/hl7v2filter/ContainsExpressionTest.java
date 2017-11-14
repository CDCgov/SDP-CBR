package gov.cdc.sdp.hl7v2filter;

import gov.cdc.sdp.hl7v2.filter.Expression;
import gov.cdc.sdp.hl7v2.filter.Filter;
import gov.cdc.sdp.hl7v2.filter.FilterBuilder;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ContainsExpressionTest extends BaseTest{

    @Test
    public void testMatches() {
        Filter e = buildFilter("'Hey there people' contains 'there'");
        Boolean v = e.evaluate(null);
        assertTrue(v);

        e = buildFilter("'Hey there people' contains 'therer'");
        v = e.evaluate(null);
        assertTrue(!v);
    }
}
