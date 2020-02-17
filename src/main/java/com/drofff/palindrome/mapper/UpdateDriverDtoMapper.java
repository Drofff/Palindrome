package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.dto.DriverDto;

@Component
public class UpdateDriverDtoMapper extends Mapper<Driver, DriverDto> {

	@Autowired
	public UpdateDriverDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
