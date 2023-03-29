package com.sud.aws_sns_publish_demo.model;

public class Message {

	private String subject;
	
	private String body;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Message [subject=" + subject + ", body=" + body + "]";
	}
	
}
