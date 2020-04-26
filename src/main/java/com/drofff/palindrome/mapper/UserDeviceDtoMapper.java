package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.UserDevice;
import com.drofff.palindrome.dto.UserDeviceDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDeviceDtoMapper extends Mapper<UserDevice, UserDeviceDto> {

    @Autowired
    public UserDeviceDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
