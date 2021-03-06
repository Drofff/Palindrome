package com.drofff.palindrome.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class ChangeRequest implements Entity, Hronable {

	@Id
	private String id;

	private String comment;

	private String targetClassName;

	private Entity targetValue;

	private String targetOwnerId;

	private String senderId;

	private LocalDateTime dateTime;

	private boolean approved;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTargetClassName() {
		return targetClassName;
	}

	public void setTargetClassName(String targetClassName) {
		this.targetClassName = targetClassName;
	}

	public Entity getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(Entity targetValue) {
		this.targetValue = targetValue;
	}

	public String getTargetOwnerId() {
		return targetOwnerId;
	}

	public void setTargetOwnerId(String targetOwnerId) {
		this.targetOwnerId = targetOwnerId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	@Override
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public boolean isForDriver() {
		return Driver.class.getName().equals(targetClassName);
	}

	public boolean isForCar() {
		return Car.class.getName().equals(targetClassName);
	}

	public static class Builder {

		private static final boolean DEFAULT_APPROVED = false;

		private final ChangeRequest changeRequest = new ChangeRequest();

		public Builder withDriverUpdate(Driver driver) {
			changeRequest.targetClassName = Driver.class.getName();
			changeRequest.targetValue = driver;
			return this;
		}

		public Builder withCarUpdate(Car car) {
			changeRequest.targetClassName = Car.class.getName();
			changeRequest.targetValue = car;
			return this;
		}

		public Builder targetOwnerId(String id) {
			changeRequest.targetOwnerId = id;
			return this;
		}

		public Builder senderId(String id) {
			changeRequest.senderId = id;
			return this;
		}

		public Builder comment(String comment) {
			changeRequest.comment = comment;
			return this;
		}

		public Builder now() {
			changeRequest.dateTime = LocalDateTime.now();
			return this;
		}

		public ChangeRequest build() {
			changeRequest.approved = DEFAULT_APPROVED;
			return changeRequest;
		}

	}

}
