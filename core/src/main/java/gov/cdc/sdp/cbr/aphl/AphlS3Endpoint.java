package gov.cdc.sdp.cbr.aphl;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.aws.s3.S3Configuration;
import org.apache.camel.component.aws.s3.S3Endpoint;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;

/**
 * The aws-s3 component is used for storing and retrieving objecct from Amazon
 * S3 Storage Service.
 */
@UriEndpoint(producerOnly = true, scheme = "aphl-s3", title = "APHL Push only AWS S3 Storage Service", syntax = "aphl-s3:bucketNameOrArn", label = "cloud,file")
public class AphlS3Endpoint extends DefaultEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(S3Endpoint.class);

    private AmazonS3 s3Client;

    @UriPath(description = "Bucket name or ARN")
    @Metadata(required = "true")
    private String bucketNameOrArn; // to support component docs
    @UriParam
    private S3Configuration configuration;
    @UriParam(label = "consumer", defaultValue = "10")
    private int maxMessagesPerPoll = 10;

    @Deprecated
    public AphlS3Endpoint(String uri, CamelContext context, S3Configuration configuration) {
        super(uri, context);
        this.configuration = configuration;
        ensureClient();
    }

    public AphlS3Endpoint(String uri, Component comp, S3Configuration configuration) {
        super(uri, comp);
        this.configuration = configuration;
        ensureClient();
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return null;
    }

    @Override
    public Producer createProducer() throws Exception {
        return new AphlS3Producer(this);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void ensureClient() {

        s3Client = configuration.getAmazonS3Client() != null ? configuration.getAmazonS3Client() : createS3Client();

        if (ObjectHelper.isNotEmpty(configuration.getAmazonS3Endpoint())) {
            s3Client.setEndpoint(configuration.getAmazonS3Endpoint());
        }

        LOG.debug("started CDC APHL Producer only AWS S3 client");
    }

    public S3Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(S3Configuration configuration) {
        this.configuration = configuration;
    }

    public void setS3Client(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public AmazonS3 getS3Client() {
        return s3Client;
    }

    /**
     * Provide the possibility to override this method for an mock
     * implementation
     */
    AmazonS3 createS3Client() {

        AmazonS3Client client = null;
        ClientConfiguration clientConfiguration = null;
        boolean isClientConfigFound = false;
        if (hasProxyConfiguration()) {
            clientConfiguration = new ClientConfiguration();
            clientConfiguration.setProxyHost(configuration.getProxyHost());
            clientConfiguration.setProxyPort(configuration.getProxyPort());
            isClientConfigFound = true;
        }
        if (configuration.getAccessKey() != null && configuration.getSecretKey() != null) {
            AWSCredentials credentials = new BasicAWSCredentials(configuration.getAccessKey(),
                    configuration.getSecretKey());
            if (isClientConfigFound) {
                client = new AmazonS3Client(credentials, clientConfiguration);
            } else {
                client = new AmazonS3Client(credentials);
            }
        } else {
            if (isClientConfigFound) {
                client = new AmazonS3Client(clientConfiguration);
            } else {
                client = new AmazonS3Client();
            }
        }

        S3ClientOptions clientOptions = S3ClientOptions.builder().setPathStyleAccess(configuration.isPathStyleAccess())
                .build();
        client.setS3ClientOptions(clientOptions);
        return client;
    }

    boolean hasProxyConfiguration() {
        return ObjectHelper.isNotEmpty(configuration.getProxyHost())
                && ObjectHelper.isNotEmpty(configuration.getProxyPort());
    }
}