package org.cdc.gov.sdp.aphl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
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

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

	@Test
	@DirtiesContext
	public void testAphlS3Producer() throws Exception {
		final String accessKey = "accessKey1";
		final String secretKey = "verySecretKey1";
		final String bucketName = "tradingpartners-east.sandbox.aimsplatform.com";/// AIMSPlatform
		final String s3url = "http://127.0.0.1:8000";

		final String file_name = "hl7v2.txt";
		final String source_file_path = "/home/k/SDP-CBR/src/test/resources/hl7v2.txt";
		final String input_file_path = "/home/k/SDP-CBR/test/" + file_name;
		final String output_file_path = "/home/k/SDP-CBR/test2/" + file_name;

		final String s3ObjectKey = "TRY";

		// Copy known test file to test route input folder
		if (!Files.exists(Paths.get(input_file_path))) {
			Files.copy(Paths.get(source_file_path), Paths.get(input_file_path));
		}

		// Set content expectations
		List<String> expected_lines = Files.readAllLines(Paths.get(input_file_path));

		// Establish connection to s3 instance (mock or legitimate)
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		AmazonS3 s3client = new AmazonS3Client(credentials);
		s3client.setEndpoint(s3url);

		// Get object and put in 2nd test directory to easily examine it
		s3client.getObject(new GetObjectRequest(bucketName, s3ObjectKey), new File(output_file_path));

		// Easily examine it
		List<String> lines = Files.readAllLines(Paths.get(output_file_path));
		assertEquals(expected_lines, lines);

		// Finally, clean up your mess
		if (s3client.doesObjectExist(bucketName, file_name)) {
			s3client.deleteObject(bucketName, file_name);
		}
	}
}
