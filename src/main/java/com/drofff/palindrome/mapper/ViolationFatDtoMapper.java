package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.ViolationFatDto;

@Component
public class ViolationFatDtoMapper extends Mapper<Violation, ViolationFatDto> {

	@Autowired
	public ViolationFatDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
