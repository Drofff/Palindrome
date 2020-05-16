package com.drofff.palindrome.dto;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.ViolationType;

import java.time.LocalDateTime;

public class PoliceViolationDto {

    private String id;

    private LocalDateTime dateTime;

    private ViolationType violationType;

    private boolean paid;

    private Police officer;

    private Driver violator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Police getOfficer() {
        return officer;
    }

    public void setOfficer(Police officer) {
        this.officer = officer;
    }

    public Driver getViolator() {
        return violator;
    }

    public void setViolator(Driver violator) {
        this.violator = violator;
    }

}
