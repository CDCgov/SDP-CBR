package org.cdc.gov.sdp.aphl;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.aws.s3.S3Configuration;
import org.apache.camel.component.aws.s3.S3Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

public class AphlS3Component  extends UriEndpointComponent {
    
    public AphlS3Component() {
        super(AphlS3Endpoint.class);
    }

    public AphlS3Component(CamelContext context) {
        super(context, AphlS3Endpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        S3Configuration configuration = new S3Configuration();
        setProperties(configuration, parameters);

        if (remaining == null || remaining.trim().length() == 0) {
            throw new IllegalArgumentException("Bucket name must be specified.");
        }
        if (remaining.startsWith("arn:")) {
            remaining = remaining.substring(remaining.lastIndexOf(":") + 1, remaining.length());
        }
        configuration.setBucketName(remaining);


        AphlS3Endpoint endpoint = new AphlS3Endpoint(uri, this, configuration);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}