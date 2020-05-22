package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.Department;
import com.drofff.palindrome.dto.DepartmentDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepartmentDtoMapper extends Mapper<Department, DepartmentDto> {

    @Autowired
    public DepartmentDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
