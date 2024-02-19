package com.banco.configurations;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AWSConfig {

    @Value("${cloud.aws.credentials.client.id}")
    private String accessKey;
    @Value("${cloud.aws.credentials.client.secret}")
    private String accessSecret;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.mailing.credentials.client.id}")
    private String accessMailingKey;
    @Value("${cloud.aws.mailing.credentials.client.secret}")
    private String accessMailingSecret;

    @Bean
    public AmazonS3 s3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region).build();
    }

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        AWSCredentials credentials = new BasicAWSCredentials(accessMailingKey, accessMailingSecret);
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_1).build();
    }

    @Bean
    public SnsClient snsClient(){
        return SnsClient.builder().credentialsProvider(() -> new AwsCredentials() {
            @Override
            public String accessKeyId() {
                return accessMailingKey;
            }

            @Override
            public String secretAccessKey() {
                return accessMailingSecret;
            }
        }).region(Region.US_EAST_1).build();
    }
}