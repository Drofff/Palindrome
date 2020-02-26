package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.CreateUserDto;

@Component
public class CreateUserDtoMapper extends Mapper<User, CreateUserDto> {

	@Autowired
	public CreateUserDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
