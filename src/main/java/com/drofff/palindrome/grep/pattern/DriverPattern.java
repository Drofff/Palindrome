package com.drofff.palindrome.grep.pattern;

import com.drofff.palindrome.annotation.Pattern;
import com.drofff.palindrome.annotation.Strategy;
import com.drofff.palindrome.annotation.TargetField;
import com.drofff.palindrome.dto.RestFindDriverDto;
import com.drofff.palindrome.grep.strategy.ContainsComparisonStrategy;

import static java.util.Objects.isNull;

@Pattern(forClass = RestFindDriverDto.class)
public class DriverPattern {

    private String licenceNumber;

    @TargetField(name = "ownedCarNumbers")
    @Strategy(ContainsComparisonStrategy.class)
    private String carNumber;

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public boolean isEmpty() {
        return isNull(licenceNumber) && isNull(carNumber);
    }

}
