package com.drofff.palindrome.dto;

import com.drofff.palindrome.document.Police;

public class RestPoliceDto implements RestResponseDto {

    private Police police;

    public RestPoliceDto(Police police) {
        this.police = police;
    }

    public Police getPolice() {
        return police;
    }

}
