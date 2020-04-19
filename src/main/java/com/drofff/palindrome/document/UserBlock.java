package com.drofff.palindrome.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document
public class UserBlock {

	@Id
	private String id;

	@NotBlank(message = "Reason should be specified")
	private String reason;

	@NotNull(message = "Date and time is required")
	private LocalDateTime dateTime;

	@NotBlank(message = "User is required")
	private String userId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public static class Builder {

		private final UserBlock userBlock = new UserBlock();

		public Builder now() {
			userBlock.dateTime = LocalDateTime.now();
			return this;
		}

		public Builder userId(String userId) {
			userBlock.userId = userId;
			return this;
		}

		public Builder reason(String reason) {
			userBlock.reason = reason;
			return this;
		}

		public UserBlock build() {
			return userBlock;
		}

	}

}
