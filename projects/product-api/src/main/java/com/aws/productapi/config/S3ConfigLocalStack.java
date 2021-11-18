package com.aws.productapi.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;

@Configuration
@Profile("local")
public class S3ConfigLocalStack {

    private static final String BUCKET_NAME = "pcs-invoice";

    private AmazonS3 amazonS3;

    public S3ConfigLocalStack() {
        amazonS3 = getAmazonS3();

        createBucket();

        AmazonSNS snsClient = getAmazonSNS();

        String s3InvoiceEventsTopicArn = createTopic(snsClient);

        AmazonSQS sqsClient = getAmazonSQS();

        createQueue(snsClient, s3InvoiceEventsTopicArn, sqsClient);

        configureBucket(s3InvoiceEventsTopicArn);
    }

    private void configureBucket(String s3InvoiceEventsTopicArn) {
        TopicConfiguration topicConfiguration = new TopicConfiguration();
        topicConfiguration.setTopicARN(s3InvoiceEventsTopicArn);
        topicConfiguration.addEvent(S3Event.ObjectCreatedByPut);

        amazonS3.setBucketNotificationConfiguration(BUCKET_NAME,
                new BucketNotificationConfiguration().addConfiguration("putObject", topicConfiguration));
    }

    private void createQueue(AmazonSNS snsClient, String s3InvoiceEventsTopicArn, AmazonSQS sqsClient) {
        String s3InvoiceQueueUrl = sqsClient.createQueue(
                new CreateQueueRequest("s3-invoice-events")).getQueueUrl();

        Topics.subscribeQueue(snsClient, sqsClient, s3InvoiceEventsTopicArn, s3InvoiceQueueUrl);
    }

    private String createTopic(AmazonSNS snsClient) {
        CreateTopicRequest createTopicRequest = new CreateTopicRequest("s3-invoice-events");
        return snsClient.createTopic(createTopicRequest).getTopicArn();
    }

    private AmazonSQS getAmazonSQS() {
        return AmazonSQSClient.builder()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://localhost:4566",
                                Regions.US_EAST_1.getName()))
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }

    private AmazonSNS getAmazonSNS() {
        return AmazonSNSClient.builder()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://localhost:4566",
                                Regions.US_EAST_1.getName()))
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }

    private AmazonS3 getAmazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials("test", "test");

        this.amazonS3 = AmazonS3Client.builder()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://localhost:4566",
                                Regions.US_EAST_1.getName()))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .enablePathStyleAccess()
                .build();
        return amazonS3;
    }

    private void createBucket() {
        amazonS3.createBucket(BUCKET_NAME);
    }

    @Bean
    public AmazonS3 amazonS3Client() {
        return this.amazonS3;
    }
}
