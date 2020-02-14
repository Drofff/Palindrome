package com.drofff.palindrome.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.drofff.palindrome.configuration.properties.MailProperties;
import com.drofff.palindrome.repository.UserRepository;

@Configuration
public class BeanConfiguration {

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
		return new ModelMapper();
	}

	@Bean
	@Autowired
	public JavaMailSender javaMailSender(MailProperties mailProperties) {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(mailProperties.getHost());
		javaMailSender.setPort(mailProperties.getPort());
		javaMailSender.setUsername(mailProperties.getUsername());
		javaMailSender.setPassword(mailProperties.getPassword());
		return javaMailSender;
	}

}
