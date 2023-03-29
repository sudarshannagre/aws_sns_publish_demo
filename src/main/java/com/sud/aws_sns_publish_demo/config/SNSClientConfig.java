package com.sud.aws_sns_publish_demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class SNSClientConfig {
	
	@Autowired
	private Environment env;

	public SnsClient getSNSClient() {
		
		Region region = Region.of(env.getProperty("aws.region.name"));
		
		SnsClient snsClient = SnsClient.builder()
				.region(region)
				.credentialsProvider(ProfileCredentialsProvider.create())
				.build();

		return snsClient;
	}
}
