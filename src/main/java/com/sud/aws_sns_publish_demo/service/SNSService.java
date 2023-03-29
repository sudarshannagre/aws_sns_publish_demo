package com.sud.aws_sns_publish_demo.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sud.aws_sns_publish_demo.config.SNSClientConfig;
import com.sud.aws_sns_publish_demo.model.Message;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.DeleteTopicRequest;
import software.amazon.awssdk.services.sns.model.DeleteTopicResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
import software.amazon.awssdk.services.sns.model.UnsubscribeRequest;
import software.amazon.awssdk.services.sns.model.UnsubscribeResponse;

@Service
public class SNSService {

	@Autowired
	private SNSClientConfig snsClientConfig;
	
	@Value("${aws.topic.arn}")
	private String TOPIC_ARN;

	private static final Logger logger = LoggerFactory.getLogger(SNSService.class);
	
    public String createSNSTopic(String topicName ) {
    	logger.info("createSNSTopic() : start at : {}", new Date());
    	SnsClient snsClient = snsClientConfig.getSNSClient();
        CreateTopicResponse result = null;
        try {
            CreateTopicRequest request = CreateTopicRequest.builder()
                    .name(topicName)
                    .build();

            result = snsClient.createTopic(request);
            logger.info("createSNSTopic() : end at : {}", new Date());
            return result.topicArn();
        } catch (SnsException e) {

            logger.error(e.awsErrorDetails().errorMessage());
            logger.info("createSNSTopic() : end at : {}", new Date());
            return e.awsErrorDetails().errorMessage();
        }finally {
			 snsClient.close();
		}
    }
	
    public String subEmail(String email) {
    	logger.info("subEmail() : start at : {}", new Date());
    	SnsClient snsClient = snsClientConfig.getSNSClient();
        try {
            SubscribeRequest request = SubscribeRequest.builder()
                .protocol("email")
                .endpoint(email)
                .returnSubscriptionArn(true)
                .topicArn(TOPIC_ARN)
                .build();

            SubscribeResponse result = snsClient.subscribe(request);
            logger.info("subEmail() : end at : {}", new Date());
            return ("Subscription ARN: " + result.subscriptionArn() + "\n\n Status is " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            logger.error(e.awsErrorDetails().errorMessage());
            logger.info("subEmail() : end at : {}", new Date());
            return e.awsErrorDetails().errorMessage();
        }finally {
			 snsClient.close();
		}
    }

	public String publishMessage(Message message) {
		logger.info("publishMessage() : start at : {}", new Date());
		SnsClient snsClient = snsClientConfig.getSNSClient();

		try {
			
			PublishRequest request = PublishRequest.builder()
					.message(new Gson().toJson(message))
					.topicArn(TOPIC_ARN)
					.build();

			PublishResponse response = snsClient.publish(request);
			String result = "Result : " + response.messageId() + " Message sent. Status is "
					+ response.sdkHttpResponse().statusCode();
			logger.info(result);
			logger.info("publishMessage() : end at : {}", new Date());
			return result;
		} catch (SnsException e) {
			logger.error(e.awsErrorDetails().errorMessage());
			logger.info("publishMessage() : end at : {}", new Date());
			return e.awsErrorDetails().errorMessage();
		}finally {
			 snsClient.close();
		}
	}
	
    public String unSubscribe(String subscriptionArn) {
    	logger.info("unSubscribe() : start at : {}", new Date());
    	SnsClient snsClient = snsClientConfig.getSNSClient();
        try {
            UnsubscribeRequest request = UnsubscribeRequest.builder()
                .subscriptionArn(subscriptionArn)
                .build();

            UnsubscribeResponse result = snsClient.unsubscribe(request);
            
            logger.info("unSubscribe() : end at : {}", new Date());
            return ("\n\nStatus was " + result.sdkHttpResponse().statusCode()
                + "\n\nSubscription was removed for " + request.subscriptionArn());

        } catch (SnsException e) {
            logger.error(e.awsErrorDetails().errorMessage());
            logger.info("unSubscribe() : end at : {}", new Date());
            return e.awsErrorDetails().errorMessage();
        }
    }
	
    public String deleteSNSTopic() {
    	logger.info("deleteSNSTopic() : start at : {}", new Date());
    	SnsClient snsClient = snsClientConfig.getSNSClient();
        try {
        	
            DeleteTopicRequest request = DeleteTopicRequest.builder()
                .topicArn(TOPIC_ARN)
                .build();

            DeleteTopicResponse result = snsClient.deleteTopic(request);
            logger.info("deleteSNSTopic() : end at : {}", new Date());
            return ("Status was " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
        	logger.error(e.awsErrorDetails().errorMessage());
        	logger.info("deleteSNSTopic() : end at : {}", new Date());
        	return e.awsErrorDetails().errorMessage();
        }finally {
			 snsClient.close();
		}
    }
}
