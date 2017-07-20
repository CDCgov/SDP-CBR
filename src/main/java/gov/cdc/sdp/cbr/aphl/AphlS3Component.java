package gov.cdc.sdp.cbr.aphl;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.aws.s3.S3Configuration;
import org.apache.camel.impl.UriEndpointComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AphlS3Component extends UriEndpointComponent {
	private static final Logger LOG = LoggerFactory.getLogger(AphlS3Component.class);

	public AphlS3Component() {
		super(AphlS3Endpoint.class);
	}

	public AphlS3Component(CamelContext context) {
		super(context, AphlS3Endpoint.class);
	}

	@Override
	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		S3Configuration configuration = new S3Configuration();
		setProperties(configuration, parameters);

		if (remaining == null || remaining.trim().length() == 0) {
			LOG.error("Bucket name must be specified");
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