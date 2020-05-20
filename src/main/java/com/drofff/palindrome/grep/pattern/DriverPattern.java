package com.drofff.palindrome.grep.pattern;

import com.drofff.palindrome.annotation.Pattern;
import com.drofff.palindrome.annotation.Strategy;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.grep.strategy.StartsWithComparisonStrategy;

@Pattern(forClass = Driver.class)
public class DriverPattern {

    @Strategy(StartsWithComparisonStrategy.class)
    private String address;

    @Strategy(StartsWithComparisonStrategy.class)
    private String licenceNumber;

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

}
