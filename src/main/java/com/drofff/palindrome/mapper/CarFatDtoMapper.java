package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.dto.CarFatDto;

@Component
public class CarFatDtoMapper extends Mapper<Car, CarFatDto> {

	@Autowired
	public CarFatDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
