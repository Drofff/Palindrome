package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.DateUtils.localDateTimeToDate;
import static java.time.temporal.ChronoUnit.DAYS;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.ValidationException;

@Service
public class JwtAuthorizationService implements AuthorizationService {

	private static final Duration JWT_TIME_TO_LIVE = Duration.of(2, DAYS);

	private final AuthenticationService authenticationService;

	@Value("${spring.security.jwt.secret}")
	private String jwtSecret;

	@Autowired
	public JwtAuthorizationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public String generateTokenForUser(User user) {
		Date expirationDate = getNextTokenExpirationDate();
		Algorithm signatureAlgorithm = getSignatureAlgorithm();
		return JWT.create()
				.withSubject(user.getId())
				.withExpiresAt(expirationDate)
				.sign(signatureAlgorithm);
	}

	private Date getNextTokenExpirationDate() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expirationDateTime = now.plus(JWT_TIME_TO_LIVE);
		return localDateTimeToDate(expirationDateTime);
	}

	private Algorithm getSignatureAlgorithm() {
		return Algorithm.HMAC512(jwtSecret);
	}

	@Override
	public User getUserByToken(String token) {
		String userId = getUserIdFromJwtToken(token);
		return authenticationService.getUserById(userId);
	}

	private String getUserIdFromJwtToken(String token) {
		try {
			return JWT.decode(token).getSubject();
		} catch(JWTDecodeException e) {
			throw new ValidationException("Invalid JWT token");
		}
	}

}
