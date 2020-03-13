package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.ViolationDto;

@Component
public class ViolationDtoMapper extends Mapper<Violation, ViolationDto> {

	@Autowired
	public ViolationDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
