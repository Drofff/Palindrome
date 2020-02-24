package com.drofff.palindrome.configuration.properties;

import static com.drofff.palindrome.utils.FormattingUtils.parseBytesFromStr;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "azure")
public class AzureProperties {

	private String connectionLink;

	private String containerName;

	private String maxFileSize;

	public String getConnectionLink() {
		return connectionLink;
	}

	public void setConnectionLink(String connectionLink) {
		this.connectionLink = connectionLink;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public String getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(String maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public long getMaxFileSizeBytes() {
		return parseBytesFromStr(maxFileSize);
	}

}
