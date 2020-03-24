package com.drofff.palindrome.grep.pattern;

import static java.util.Objects.isNull;

import com.drofff.palindrome.annotation.Pattern;
import com.drofff.palindrome.annotation.Strategy;
import com.drofff.palindrome.annotation.TargetField;
import com.drofff.palindrome.dto.RestDriverDto;
import com.drofff.palindrome.grep.strategy.ContainsComparisonStrategy;

@Pattern(forClass = RestDriverDto.class)
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
