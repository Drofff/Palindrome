package com.drofff.palindrome.configuration;

import com.drofff.palindrome.interceptor.BlockedUserInterceptor;
import com.drofff.palindrome.interceptor.DriverInterceptor;
import com.drofff.palindrome.interceptor.PoliceInterceptor;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.PoliceService;
import com.drofff.palindrome.service.UserBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.drofff.palindrome.constants.EndpointConstants.*;

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

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new DriverInterceptor(driverService));
		registry.addInterceptor(new BlockedUserInterceptor(userBlockService));
		registry.addInterceptor(new PoliceInterceptor(policeService));
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String apiResourcesPattern = API_RESOURCE_ENDPOINTS_BASE + PATH_ANY_SEGMENTS;
		registry.addResourceHandler(apiResourcesPattern)
				.addResourceLocations("classpath:/static/img/");
		registry.addResourceHandler(FAVICON_ENDPOINT)
				.addResourceLocations("classpath:/static/favicon.ico");
	}

}
