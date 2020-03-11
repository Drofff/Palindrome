package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.dto.PoliceFatDto;

@Component
public class PoliceFatDtoMapper extends Mapper<Police, PoliceFatDto> {

	@Autowired
	public PoliceFatDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
