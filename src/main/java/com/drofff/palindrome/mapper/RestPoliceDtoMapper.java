package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.dto.RestPoliceDto;

@Component
public class RestPoliceDtoMapper extends Mapper<Police, RestPoliceDto> {

	@Autowired
	public RestPoliceDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
