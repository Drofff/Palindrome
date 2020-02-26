package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.dto.CarsDriverDto;

@Component
public class CarsDriverDtoMapper extends Mapper<Driver, CarsDriverDto> {

	@Autowired
	public CarsDriverDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
