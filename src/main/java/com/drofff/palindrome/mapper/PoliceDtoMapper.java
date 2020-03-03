package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.dto.PoliceDto;

@Component
public class PoliceDtoMapper extends Mapper<Police, PoliceDto> {

	@Autowired
	public PoliceDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
