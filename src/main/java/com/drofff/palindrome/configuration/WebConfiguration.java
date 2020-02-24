package com.drofff.palindrome.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.drofff.palindrome.interceptor.BlockedUserInterceptor;
import com.drofff.palindrome.interceptor.DriverInterceptor;
import com.drofff.palindrome.service.DriverServiceImpl;
import com.drofff.palindrome.service.UserBlockService;

@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

	@Autowired
	private DriverServiceImpl driverService;

	@Autowired
	private UserBlockService userBlockService;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new DriverInterceptor(driverService));
		registry.addInterceptor(new BlockedUserInterceptor(userBlockService));
	}

}
