package com.sud.aws_sns_publish_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sud.aws_sns_publish_demo.model.Message;
import com.sud.aws_sns_publish_demo.service.SNSService;

@RestController
@RequestMapping("/v1")
public class SNSController {

	@Autowired
	private SNSService snsService;
	
	@PostMapping(value = "/topic/{topicName}")
	public String createTopic(@PathVariable String topicName) {
		return snsService.createSNSTopic(topicName);
	}
	
	@PutMapping(value = "/topic/{email}")
	public String subscribeEmail(@PathVariable String email) {
		return snsService.subEmail(email);
	}

	@PostMapping(value = "/topic")
	public String publishMessage(@RequestBody Message message) {
		return snsService.publishMessage(message);
	}
	
	
	@DeleteMapping(value = "/topic/{subscriptionArn}")
	public String unSubscribe(@PathVariable String subscriptionArn) {
		return snsService.unSubscribe(subscriptionArn);
	}
	
	@DeleteMapping(value = "/topic")
	public String getMsg() {
		return snsService.deleteSNSTopic();
	}
}
