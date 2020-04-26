package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.TwoStepAuthException;
import com.drofff.palindrome.tool.ThreadTestTool;
import com.drofff.palindrome.type.ExternalAuthenticationOption;
import com.drofff.palindrome.type.Mail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.drofff.palindrome.enums.ExternalAuthType.EMAIL;
import static com.drofff.palindrome.enums.Role.POLICE;
import static com.drofff.palindrome.tool.CollectionTestTool.getFirstElement;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.AuthenticationUtils.setCurrentUser;
import static com.drofff.palindrome.utils.ReflectionUtils.*;
import static com.drofff.palindrome.utils.StringUtils.randomString;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailAuthenticatorTest {

	private static final Executor TEST_EXECUTOR = Executors.newFixedThreadPool(3);

	private static final Pattern TOKEN_PARAM_PATTERN = Pattern.compile("token=([A-Za-z0-9\\-]+),?");

	@Mock
	private PoliceService policeService;

	@Mock
	private MailService mailService;

	@InjectMocks
	private EmailAuthenticator authenticator;

	private String emailOptionId;

	@Before
	public void initTest() {
		User user = getDefaultUser();
		setCurrentUser(user);
		Police defaultPolice = getDefaultPoliceWithUserId(user.getId());
		when(policeService.getPoliceByUserId(user.getId()))
				.thenReturn(defaultPolice);
		emailOptionId = getEmailOptionId();
	}

	private User getDefaultUser() {
		User user = new User();
		user.setId(randomString());
		user.setUsername("test");
		user.setRole(POLICE);
		return user;
	}

	private Police getDefaultPoliceWithUserId(String userId) {
		Police police = new Police();
		police.setId(randomString());
		police.setFirstName("Test");
		police.setUserId(userId);
		police.setTwoStepAuthEnabled(true);
		return police;
	}

	private String getEmailOptionId() {
		Set<ExternalAuthenticationOption> options = authenticator.getAuthenticationOptions();
		return getFirstElement(options).getId();
	}

	@Test
	public void getAuthenticationOptionsTest() {
		Set<ExternalAuthenticationOption> optionSet = authenticator.getAuthenticationOptions();
		assertEquals(1, optionSet.size());
		ExternalAuthenticationOption option = getFirstElement(optionSet);
		assertEquals(EMAIL, option.getType());
		String expectedLabel = getCurrentUser().getUsername();
		assertEquals(expectedLabel, option.getLabel());
		assertNotNull(option.getId());
	}

	@Test
	public void hasOptionWithIdTest() {
		Set<ExternalAuthenticationOption> options = authenticator.getAuthenticationOptions();
		assertFalse(options.isEmpty());
		boolean hasAllIds = options.stream()
				.allMatch(option -> authenticator.hasOptionWithId(option.getId()));
		assertTrue(hasAllIds);
	}

	@Test
	public void externalAuthenticationTest() {
		CompletableFuture<String> resultFuture = authenticateExternalAsync();
		ThreadTestTool.sleep(500);
		Mail mail = getFirstMailSent();
		String token = getTokenFromTwoStepAuthMail(mail);
		authenticator.completeAuthenticationWithOptionId(emailOptionId, token);
		assertEquals(emailOptionId, resultFuture.join());
	}

	private Mail getFirstMailSent() {
		ArgumentCaptor<Mail> captor = ArgumentCaptor.forClass(Mail.class);
		verify(mailService).sendMailTo(captor.capture(), any());
		return captor.getValue();
	}

	private String getTokenFromTwoStepAuthMail(Mail mail) {
		String text = mail.getText();
		Matcher matcher = TOKEN_PARAM_PATTERN.matcher(text);
		if(matcher.find()) {
			return matcher.group(1);
		}
		throw new PalindromeException("Token is not found");
	}

	@Test
	public void externalAuthenticationInvalidTokenTest() {
		CompletableFuture<String> resultFuture = authenticateExternalAsync();
		ThreadTestTool.sleep(500);
		String invalidToken = "000000";
		authenticator.completeAuthenticationWithOptionId(emailOptionId, invalidToken);
		assertEquals("Token is invalid", resultFuture.join());
	}

	@Test
	public void externalAuthenticationTokenWaitTimeOutTest() {
		long originalMaxTokenWaitTime = getMaxTokenWaitTime();
		setMaxTokenWaitTime(10);
		CompletableFuture<String> resultFuture = authenticateExternalAsync();
		ThreadTestTool.sleep(100);
		assertEquals("Authentication confirmation has not been received", resultFuture.join());
		setMaxTokenWaitTime(originalMaxTokenWaitTime);
	}

	private long getMaxTokenWaitTime() {
		return (Long) getFieldValueFromObject(maxTokenWaitTimeField(), authenticator);
	}

	private void setMaxTokenWaitTime(long millis) {
		setFieldValueIntoObject(maxTokenWaitTimeField(), millis, authenticator);
	}

	private Field maxTokenWaitTimeField() {
		return getFieldFromClassByName("maxTokenWaitTime", EmailAuthenticator.class)
				.orElseThrow(() -> new PalindromeException("Can not reach maxTokenWaitTime field"));
	}

	@Test
	public void externalAuthenticationInvalidOptionIdTest() {
		String originalEmailOptionId = getEmailOptionId();
		emailOptionId = "invalidOptionId";
		CompletableFuture<String> resultFuture = authenticateExternalAsync();
		assertEquals("Unknown option id", resultFuture.join());
		emailOptionId = originalEmailOptionId;
	}

	private CompletableFuture<String> authenticateExternalAsync() {
		doNothing().when(mailService).sendMailTo(any(), any());
		User currentUser = getCurrentUser();
		CompletableFuture<String> resultFuture = new CompletableFuture<>();
		Runnable authenticateRunnable = () -> authenticateExternal(currentUser, resultFuture);
		TEST_EXECUTOR.execute(authenticateRunnable);
		return resultFuture;
	}

	private void authenticateExternal(User user, CompletableFuture<String> resultFuture)  {
		try {
			setCurrentUser(user);
			authenticator.authenticateUsingOptionWithId(emailOptionId);
			resultFuture.complete(emailOptionId);
		} catch(TwoStepAuthException e) {
			resultFuture.complete(e.getMessage());
		}
	}

}
