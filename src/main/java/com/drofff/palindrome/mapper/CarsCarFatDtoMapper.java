package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.dto.CarsCarFatDto;

@Component
public class CarsCarFatDtoMapper extends Mapper<Car, CarsCarFatDto> {

	@Autowired
	public CarsCarFatDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
