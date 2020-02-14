package com.drofff.palindrome.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.drofff.palindrome.interceptor.DriverInterceptor;
import com.drofff.palindrome.service.DriverServiceImpl;

@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

	@Autowired
	private DriverServiceImpl driverService;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new DriverInterceptor(driverService));
	}

}
