package com.drofff.palindrome.type;

public class TicketFile {

	private String name;

	private byte[] content;

	private String contentType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public static class Builder {

		private final TicketFile ticketFile = new TicketFile();

		public Builder name(String name) {
			ticketFile.name = name;
			return this;
		}

		public Builder contentType(String contentType) {
			ticketFile.contentType = contentType;
			return this;
		}

		public Builder content(byte[] content) {
			ticketFile.content = content;
			return this;
		}

		public TicketFile build() {
			return ticketFile;
		}

	}

}
