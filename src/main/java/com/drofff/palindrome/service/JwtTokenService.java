package com.drofff.palindrome.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.enums.AccessLevel;
import com.drofff.palindrome.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

import static com.drofff.palindrome.utils.DateUtils.localDateTimeToDate;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class JwtTokenService implements AuthorizationTokenService {

	private static final Duration JWT_TIME_TO_LIVE = Duration.of(2, DAYS);

	private static final String ACCESS_LEVEL_PARAM = "acc_lev";

	private final UserService userService;

	@Value("${spring.security.jwt.secret}")
	private String jwtSecret;

	@Autowired
	public JwtTokenService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public String generateAuthorizationTokenForUser(User user) {
		return generateJwtForUserWithAccessLevel(user, AccessLevel.FULL);
	}

	@Override
	public String generateAuthorizationTokenOfAccessLevelForUser(AccessLevel accessLevel, User user) {
		return generateJwtForUserWithAccessLevel(user, accessLevel);
	}

	private String generateJwtForUserWithAccessLevel(User user, AccessLevel accessLevel) {
		Date expirationDate = getNextTokenExpirationDate();
		Algorithm signatureAlgorithm = getSignatureAlgorithm();
		return JWT.create()
				.withSubject(user.getId())
				.withClaim(ACCESS_LEVEL_PARAM, accessLevel.getLevelCode())
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
	public User getUserByAuthorizationToken(String token) {
		String userId = decodeJwtToken(token).getSubject();
		return userService.getUserById(userId);
	}

	@Override
	public AccessLevel getAccessLevelOfAuthorizationToken(String token) {
		validateNotNull(token, "Token should not be null");
		Claim accessLevelClaim = decodeJwtToken(token).getClaim(ACCESS_LEVEL_PARAM);
		int accessLevelCode = accessLevelClaim.asInt();
		return AccessLevel.ofCode(accessLevelCode);
	}

	private DecodedJWT decodeJwtToken(String token) {
		try {
			return JWT.decode(token);
		} catch(JWTDecodeException e) {
			throw new ValidationException("Invalid JWT token");
		}
	}

}
