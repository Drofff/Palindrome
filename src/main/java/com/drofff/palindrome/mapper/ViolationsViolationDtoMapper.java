package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.ViolationsViolationDto;

@Component
public class ViolationsViolationDtoMapper extends Mapper<Violation, ViolationsViolationDto> {

	@Autowired
	public ViolationsViolationDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
