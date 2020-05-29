package com.drofff.palindrome.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.cors.CorsConfiguration;

import static com.drofff.palindrome.constants.EndpointConstants.*;

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
		String apiEndpointsPattern = API_ENDPOINTS + PATH_ANY_SEGMENTS;
		String userAppEndpointsPattern = USER_APP_ENDPOINTS_BASE + PATH_ANY_SEGMENTS;
		http.cors()
				.and()
				.authorizeRequests()
				.antMatchers(HOME_ENDPOINT, REGISTRATION_ENDPOINT, ACTIVATE_ACCOUNT_ENDPOINT,
						FORGOT_PASS_ENDPOINT, PASS_RECOVERY_ENDPOINT, ERROR_ENDPOINT, FAVICON_ENDPOINT,
						apiEndpointsPattern, userAppEndpointsPattern)
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
				.csrf()
				.ignoringAntMatchers(apiEndpointsPattern);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider);
	}

	@Bean
	public CorsConfiguration corsConfiguration() {
		CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		corsConfiguration.addAllowedOrigin("http://localhost:4200");
		return corsConfiguration;
	}

}