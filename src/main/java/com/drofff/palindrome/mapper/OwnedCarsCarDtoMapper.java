package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.dto.OwnedCarsCarDto;

@Component
public class OwnedCarsCarDtoMapper extends Mapper<Car, OwnedCarsCarDto> {

	@Autowired
	public OwnedCarsCarDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
