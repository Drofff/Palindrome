package com.drofff.palindrome.type;

import com.drofff.palindrome.document.ViolationType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ViolationsStatistic {

    private long violationsCount;

    private long unpaidViolationsCount;

    private LocalDateTime longestUnpaidViolationDateTime;

    private Map<ViolationType, Integer> mostFrequentViolationTypes = new HashMap<>();

    private Map<LocalDate, Integer> violationsCountPerLastMonths = new HashMap<>();

    public long getViolationsCount() {
        return violationsCount;
    }

    public void setViolationsCount(long violationsCount) {
        this.violationsCount = violationsCount;
    }

    public long getUnpaidViolationsCount() {
        return unpaidViolationsCount;
    }

    public void setUnpaidViolationsCount(long unpaidViolationsCount) {
        this.unpaidViolationsCount = unpaidViolationsCount;
    }

    public LocalDateTime getLongestUnpaidViolationDateTime() {
        return longestUnpaidViolationDateTime;
    }

    public void setLongestUnpaidViolationDateTime(LocalDateTime longestUnpaidViolationDateTime) {
        this.longestUnpaidViolationDateTime = longestUnpaidViolationDateTime;
    }

    public Map<ViolationType, Integer> getMostFrequentViolationTypes() {
        return mostFrequentViolationTypes;
    }

    public void setMostFrequentViolationTypes(Map<ViolationType, Integer> mostFrequentViolationTypes) {
        this.mostFrequentViolationTypes = mostFrequentViolationTypes;
    }

    public Map<LocalDate, Integer> getViolationsCountPerLastMonths() {
        return violationsCountPerLastMonths;
    }

    public void setViolationsCountPerLastMonths(Map<LocalDate, Integer> violationsCountPerLastMonths) {
        this.violationsCountPerLastMonths = violationsCountPerLastMonths;
    }

    public static class Builder {

        private final ViolationsStatistic violationsStatistic = new ViolationsStatistic();

        public Builder violationsCount(long violationsCount) {
            violationsStatistic.violationsCount = violationsCount;
            return this;
        }

        public Builder unpaidViolationsCount(long unpaidViolationsCount) {
            violationsStatistic.unpaidViolationsCount = unpaidViolationsCount;
            return this;
        }

        public Builder longestUnpaidViolationDateTime(LocalDateTime dateTime) {
            violationsStatistic.longestUnpaidViolationDateTime = dateTime;
            return this;
        }

        public Builder mostFrequentViolationTypes(Map<ViolationType, Integer> violationTypes) {
            violationsStatistic.mostFrequentViolationTypes = violationTypes;
            return this;
        }

        public Builder violationsCountPerLastMonths(Map<LocalDate, Integer> violationsCount) {
            violationsStatistic.violationsCountPerLastMonths = violationsCount;
            return this;
        }

        public ViolationsStatistic build() {
            return violationsStatistic;
        }

    }

}
