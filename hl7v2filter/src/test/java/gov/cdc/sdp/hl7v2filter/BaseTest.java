package gov.cdc.sdp.hl7v2filter;

import gov.cdc.sdp.hl7v2.filter.Expression;
import gov.cdc.sdp.hl7v2.filter.Filter;
import gov.cdc.sdp.hl7v2.filter.FilterBuilder;
import static  org.junit.Assert.*;

import java.io.IOException;

public class BaseTest {

    protected String readFile(String file) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
    }

    public Expression buildExpression(String exp) {
        FilterBuilder b = new FilterBuilder();
        return b.build(exp);
    }

    public Filter buildFilter(String exp) {
        FilterBuilder b = new FilterBuilder();
        return b.buildFilter(exp);
    }
}
