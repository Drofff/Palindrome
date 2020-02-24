package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.HomeViolationDto;

@Component
public class HomeViolationDtoMapper extends Mapper<Violation, HomeViolationDto> {

	@Autowired
	public HomeViolationDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
