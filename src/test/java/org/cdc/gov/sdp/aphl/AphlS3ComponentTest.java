package org.cdc.gov.sdp.aphl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "/AphlS3ComponentTest-context.xml" })
@PropertySource("/application.properties")
public class AphlS3ComponentTest {
	@Autowired
	protected CamelContext camelContext;

	@EndpointInject(uri = "mock:foo")
	protected MockEndpoint foo;

	@Produce(uri = "direct:aphls3")
	protected ProducerTemplate template;

	private final String accessKey = "accessKey1";
	private final String secretKey = "verySecretKey1";
	private final String bucketName = "tradingpartners-east.sandbox.aimsplatform.com";/// AIMSPlatform
	private final String s3url = "http://127.0.0.1:8000";

	private final String file_name = "hl7v2.txt";
	private final String input_file_path = "src/test/resources/" + file_name;
	private final String output_file_path = "test/" + file_name;

	private final String S3_KEY_HEADER = "CamelAwsS3Key";
	private final String s3ObjectKey = "TRY";

	@Test
	@DirtiesContext
	public void testAphlS3Route() throws Exception {
		// Set content expectations
		String expected_content = readFile(input_file_path);
		foo.expectedMessageCount(1);

		// Build content
		Exchange exchange = new DefaultExchange(camelContext);
		Message msg = new DefaultMessage();

		msg.setHeader(S3_KEY_HEADER, s3ObjectKey);
		msg.setBody(expected_content);
		exchange.setIn(msg);

		// Send content
		template.send(exchange);

		// Confirm it was delivered
		MockEndpoint.assertIsSatisfied(camelContext);

		// Now to confirm it was delivered correctly...
		// Establish connection to s3 instance (mock or legitimate)
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		AmazonS3 s3client = new AmazonS3Client(credentials);
		s3client.setEndpoint(s3url);

		// Get object and put in a test directory to easily examine it
		s3client.getObject(new GetObjectRequest(bucketName, s3ObjectKey), new File(output_file_path));
		assertTrue(Files.exists(Paths.get(output_file_path)));

		// Examine it
		String actual_content = readFile(output_file_path);
		assertEquals(expected_content, actual_content);

		// Finally, clean up the mess
		if (s3client.doesObjectExist(bucketName, s3ObjectKey)) {
			s3client.deleteObject(bucketName, s3ObjectKey);
		}
		Files.deleteIfExists(Paths.get(output_file_path));
		assertTrue(!Files.exists(Paths.get(output_file_path)));
	}

	private String readFile(String file) throws IOException {
		return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
	}
}
