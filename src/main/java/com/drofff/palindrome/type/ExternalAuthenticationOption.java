package com.drofff.palindrome.type;

import static com.drofff.palindrome.enums.ExternalAuthType.DEVICE;
import static com.drofff.palindrome.enums.ExternalAuthType.EMAIL;
import static java.util.Collections.emptySet;

import java.util.Set;

import com.drofff.palindrome.enums.ExternalAuthType;

public class ExternalAuthenticationOption {

	private String id;

	private String label;

	private ExternalAuthType type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ExternalAuthType getType() {
		return type;
	}

	public void setType(ExternalAuthType type) {
		this.type = type;
	}

	public static class Builder {

		private final ExternalAuthenticationOption authenticationOption = new ExternalAuthenticationOption();

		public Builder withId(String id) {
			authenticationOption.id = id;
			return this;
		}

		public Builder withLabel(String label) {
			authenticationOption.label = label;
			return this;
		}

		public Builder usingEmail() {
			authenticationOption.type = EMAIL;
			return this;
		}

		public Builder usingPushNotification() {
			authenticationOption.type = DEVICE;
			return this;
		}

		public ExternalAuthenticationOption build() {
			return authenticationOption;
		}

		public Set<ExternalAuthenticationOption> singleton() {
			return emptySet();
		}

	}

}
