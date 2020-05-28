package com.drofff.palindrome.enums;

import com.drofff.palindrome.exception.ValidationException;

import static java.util.Arrays.stream;

public enum AccessLevel {

    FULL(0), READ_ONLY(1);

    private final int levelCode;

    AccessLevel(int levelCode) {
        this.levelCode = levelCode;
    }

    public static AccessLevel ofCode(int accessLevelCode) {
        return stream(values())
                .filter(level -> level.hasCode(accessLevelCode))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Unknown access level code"));
    }

    public int getLevelCode() {
        return levelCode;
    }

    private boolean hasCode(int code) {
        return levelCode == code;
    }

}
