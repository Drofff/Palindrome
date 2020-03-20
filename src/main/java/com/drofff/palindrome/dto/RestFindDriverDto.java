package com.drofff.palindrome.dto;

import java.util.HashSet;
import java.util.Set;

public class RestFindDriverDto {

    private String id;

    private String firstName;

    private String lastName;

    private String middleName;

    private String address;

    private String licenceNumber;

    private byte[] photo;

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Set<String> getOwnedCarNumbers() {
        return ownedCarNumbers;
    }

    public void setOwnedCarNumbers(Set<String> ownedCarNumbers) {
        this.ownedCarNumbers = ownedCarNumbers;
    }

}
