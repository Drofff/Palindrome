package com.drofff.palindrome.type;

import org.springframework.http.MediaType;

public class ExportedViolation {

    private String filename;

    private MediaType mediaType;

    private byte[] content;

    public ExportedViolation() {}

    public ExportedViolation(String filename, MediaType mediaType, byte[] content) {
        this.filename = filename;
        this.mediaType = mediaType;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
