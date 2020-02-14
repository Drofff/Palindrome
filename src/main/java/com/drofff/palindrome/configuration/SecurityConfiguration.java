package com.drofff.palindrome.configuration;

import static com.drofff.palindrome.constants.EndpointConstants.ACTIVATE_ACCOUNT_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.ERROR_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.FORGOT_PASS_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.LOGIN_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.PASS_RECOVERY_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.REGISTRATION_ENDPOINT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Autowired
	private UserDetailsService userDetailsService;

	@Value("${spring.security.cookie.key}")
	private String cookieKey;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers(HOME_ENDPOINT, REGISTRATION_ENDPOINT, ACTIVATE_ACCOUNT_ENDPOINT,
						FORGOT_PASS_ENDPOINT, PASS_RECOVERY_ENDPOINT, ERROR_ENDPOINT)
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.formLogin()
				.loginPage(LOGIN_ENDPOINT)
				.permitAll()
				.and()
				.logout()
				.logoutUrl("/logout")
				.permitAll()
				.and()
				.rememberMe()
				.key(cookieKey)
				.userDetailsService(userDetailsService)
				.and()
				.csrf();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider);
	}

}
