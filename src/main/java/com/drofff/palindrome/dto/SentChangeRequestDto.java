package com.drofff.palindrome.dto;

import com.drofff.palindrome.document.Driver;

import java.time.LocalDateTime;

public class SentChangeRequestDto {

    private String id;

    private String comment;

    private Driver targetOwner;

    private LocalDateTime dateTime;

    private boolean approved;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Driver getTargetOwner() {
        return targetOwner;
    }

    public void setTargetOwner(Driver targetOwner) {
        this.targetOwner = targetOwner;
    }

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

}
