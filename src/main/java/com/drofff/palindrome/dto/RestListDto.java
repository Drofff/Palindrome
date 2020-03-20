package com.drofff.palindrome.dto;

import java.util.List;

public class RestListDto<T> implements RestResponseDto {

    private List<T> data;

    public RestListDto(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

}
