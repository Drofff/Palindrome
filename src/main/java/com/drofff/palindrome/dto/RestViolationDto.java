package com.drofff.palindrome.dto;

import com.drofff.palindrome.document.ViolationType;

import java.time.LocalDateTime;

public class RestViolationDto {

    private String location;

    private LocalDateTime dateTime;

    private ViolationType violationType;

    private boolean paid;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public ViolationType getViolationType() {
        return violationType;
    }

    public void setViolationType(ViolationType violationType) {
        this.violationType = violationType;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

}
