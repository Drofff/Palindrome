package com.drofff.palindrome.configuration;

import com.drofff.palindrome.interceptor.AuthorizationTokenInterceptor;
import com.drofff.palindrome.interceptor.BlockedUserInterceptor;
import com.drofff.palindrome.interceptor.DriverInterceptor;
import com.drofff.palindrome.interceptor.PoliceInterceptor;
import com.drofff.palindrome.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.drofff.palindrome.constants.EndpointConstants.VIOLATION_API_BASE_ENDPOINT;

@Configuration
@EnableWebMvc
@EnableScheduling
public class WebConfiguration implements WebMvcConfigurer {

	@Autowired
	private DriverService driverService;

	@Autowired
	private UserBlockService userBlockService;

	@Autowired
	private PoliceService policeService;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private AuthenticationService authenticationService;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new DriverInterceptor(driverService));
		registry.addInterceptor(new BlockedUserInterceptor(userBlockService));
		registry.addInterceptor(new PoliceInterceptor(policeService));
		registry.addInterceptor(new AuthorizationTokenInterceptor(authorizationService, authenticationService))
				.addPathPatterns(VIOLATION_API_BASE_ENDPOINT + "**", "/api/police-info");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/api/resources/img/**")
				.addResourceLocations("classpath:/static/img/");
	}

}
