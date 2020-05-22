package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.ViolationType;
import com.drofff.palindrome.dto.ViolationTypeDto;
import com.drofff.palindrome.enums.Currency;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static com.drofff.palindrome.enums.Currency.UAH;
import static org.junit.Assert.assertEquals;

public class ViolationTypeDtoMapperTest {

    private ViolationTypeDtoMapper violationTypeDtoMapper;

    @Before
    public void init() {
        violationTypeDtoMapper = initViolationTypeDtoMapper();
    }

    private ViolationTypeDtoMapper initViolationTypeDtoMapper() {
        ModelMapper modelMapper = new ModelMapper();
        return new ViolationTypeDtoMapper(modelMapper);
    }

    @Test
    public void toEntityTest() {
        ViolationTypeDto testViolationTypeDto = defaultViolationTypeDto();
        ViolationType resultViolationType = violationTypeDtoMapper.toEntity(testViolationTypeDto);
        Long resultFeeAmount = resultViolationType.getFee().getAmount();
        assertEquals(testViolationTypeDto.getFeeAmount(), resultFeeAmount);
        Currency resultCurrency = resultViolationType.getFee().getCurrency();
        assertEquals(testViolationTypeDto.getFeeCurrency(), resultCurrency);
    }

    private ViolationTypeDto defaultViolationTypeDto() {
        ViolationTypeDto violationTypeDto = new ViolationTypeDto();
        violationTypeDto.setName("violationType");
        violationTypeDto.setFeeAmount(200_00L);
        violationTypeDto.setFeeCurrency(UAH);
        return violationTypeDto;
    }

}
