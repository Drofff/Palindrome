package com.drofff.palindrome.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class RestDriverDto implements RestResponseDto {

    private String id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String address;

    private String photoUrl;

    private String licenceNumber;

    private int violationsCount;

    private LocalDateTime lastViolationDateTime;

	private Set<String> ownedCarNumbers = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public int getViolationsCount() {
        return violationsCount;
    }

    public void setViolationsCount(int violationsCount) {
        this.violationsCount = violationsCount;
    }

    public LocalDateTime getLastViolationDateTime() {
        return lastViolationDateTime;
    }

    public void setLastViolationDateTime(LocalDateTime lastViolationDateTime) {
        this.lastViolationDateTime = lastViolationDateTime;
    }

	public Set<String> getOwnedCarNumbers() {
		return ownedCarNumbers;
	}

	public void setOwnedCarNumbers(Set<String> ownedCarNumbers) {
		this.ownedCarNumbers = ownedCarNumbers;
	}

}
