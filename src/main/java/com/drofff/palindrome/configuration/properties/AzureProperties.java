package com.drofff.palindrome.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "azure")
public class AzureProperties {

	private String connectionLink;

	private String containerName;

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

}
