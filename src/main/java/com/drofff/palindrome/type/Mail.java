package com.drofff.palindrome.type;

import org.json.simple.JSONObject;

public class Mail {

	private String topic;

	private String text;

	public static Mail fromJSONObject(JSONObject jsonObject) {
		Mail mail = new Mail();
		mail.text = (String) jsonObject.get("text");
		mail.topic = (String) jsonObject.get("topic");
		return mail;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
