package com.drofff.palindrome.configuration;

import com.drofff.palindrome.configuration.properties.MailProperties;
import com.drofff.palindrome.filter.AccessLevelFilter;
import com.drofff.palindrome.filter.AuthorizationFilter;
import com.drofff.palindrome.filter.TwoStepAuthFilter;
import com.drofff.palindrome.repository.UserRepository;
import com.drofff.palindrome.service.AuthorizationTokenService;
import com.drofff.palindrome.service.PoliceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Properties;

import static com.drofff.palindrome.constants.EndpointConstants.API_ENDPOINTS;
import static java.util.Collections.singletonList;

@Configuration
public class BeanConfiguration {

	private static final String SERVLET_PATH_WILDCARD = "/*";

	@Bean
	@Autowired
	public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Bean
	@Autowired
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return username -> userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with such username doesn't exist"));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		return modelMapper;
	}

	@Bean
	@Autowired
	public JavaMailSender javaMailSender(MailProperties mailProperties) {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(mailProperties.getHost());
		javaMailSender.setPort(mailProperties.getPort());
		javaMailSender.setUsername(mailProperties.getUsername());
		javaMailSender.setPassword(mailProperties.getPassword());
		javaMailSender.setJavaMailProperties(mailConnectionProperties());
		return javaMailSender;
	}

	private Properties mailConnectionProperties() {
		Properties connectionProperties = new Properties();
		connectionProperties.put("mail.smtp.auth", true);
		connectionProperties.put("mail.smtp.starttls.enable", true);
		return connectionProperties;
	}

	@Bean
	@Autowired
	public FilterRegistrationBean<AuthorizationFilter> authorizationFilter(AuthorizationTokenService authorizationService) {
		AuthorizationFilter filter = new AuthorizationFilter(authorizationService);
		FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(filter);
		List<String> urlPatterns = singletonList(apiEndpointsPattern());
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setOrder(1);
		return registrationBean;
	}

	@Bean
	@Autowired
	public FilterRegistrationBean<AccessLevelFilter> accessLevelFilter(AuthorizationTokenService authorizationTokenService) {
		AccessLevelFilter accessLevelFilter = new AccessLevelFilter(authorizationTokenService);
		FilterRegistrationBean<AccessLevelFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(accessLevelFilter);
		List<String> urlPatterns = singletonList(apiEndpointsPattern());
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setOrder(2);
		return registrationBean;
	}

	private String apiEndpointsPattern() {
		return API_ENDPOINTS + SERVLET_PATH_WILDCARD;
	}

	@Bean
	@Autowired
	public FilterRegistrationBean<TwoStepAuthFilter> twoStepAuthFilter(PoliceService policeService) {
		TwoStepAuthFilter authFilter = new TwoStepAuthFilter(policeService);
		FilterRegistrationBean<TwoStepAuthFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(authFilter);
		registrationBean.setOrder(3);
		return registrationBean;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}