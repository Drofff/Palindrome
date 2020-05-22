package com.drofff.palindrome.dto;

import com.drofff.palindrome.enums.Currency;

public class ViolationTypeDto {

    private String name;

    private Long feeAmount;

    private Currency feeCurrency;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Long feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Currency getFeeCurrency() {
        return feeCurrency;
    }

    public void setFeeCurrency(Currency feeCurrency) {
        this.feeCurrency = feeCurrency;
    }

}
