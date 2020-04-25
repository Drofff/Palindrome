package com.drofff.palindrome.service;

import static com.drofff.palindrome.enums.ExternalAuthType.EMAIL;
import static com.drofff.palindrome.enums.Role.POLICE;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.AuthenticationUtils.setCurrentUser;
import static com.drofff.palindrome.utils.StringUtils.randomString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.tool.ThreadTestTool;
import com.drofff.palindrome.type.ExternalAuthenticationOption;
import com.drofff.palindrome.type.Mail;

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

	@Before
	public void initTest() {
		User user = getDefaultUser();
		setCurrentUser(user);
		Police defaultPolice = getDefaultPoliceWithUserId(user.getId());
		when(policeService.getPoliceByUserId(user.getId()))
				.thenReturn(defaultPolice);
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

	@Test
	public void getAuthenticationOptionsTest() {
		Set<ExternalAuthenticationOption> optionSet = authenticator.getAuthenticationOptions();
		assertEquals(1, optionSet.size());
		ExternalAuthenticationOption option = getFirst(optionSet);
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

	//TODO: externalAuth time out test. ExternalAuth incorrect token test

	@Test
	public void externalAuthenticationTest() {
		doNothing().when(mailService).sendMailTo(any(), any());
		String emailOptionId = getEmailOptionId();
		CompletableFuture<String> resultFuture = new CompletableFuture<>();
		User currentUser = getCurrentUser();
		TEST_EXECUTOR.execute(authenticateUsingOptionWithIdRunnable(currentUser, resultFuture));
		ThreadTestTool.sleep(500);
		TEST_EXECUTOR.execute(completeAuthenticationWithOptionIdRunnable(currentUser));
		assertEquals(emailOptionId, resultFuture.join());
	}

	private String getEmailOptionId() {
		Set<ExternalAuthenticationOption> options = authenticator.getAuthenticationOptions();
		return getFirst(options).getId();
	}

	private Runnable authenticateUsingOptionWithIdRunnable(User user, CompletableFuture<String> resultFuture)  {
		return () -> {
			setCurrentUser(user);
			String emailOptionId = getEmailOptionId();
			authenticator.authenticateUsingOptionWithId(emailOptionId);
			resultFuture.complete(emailOptionId);
		};
	}

	private <T> T getFirst(Collection<T> collection) {
		return collection.stream()
				.findFirst()
				.orElseThrow(() -> new PalindromeException("Collections is empty"));
	}

	private Runnable completeAuthenticationWithOptionIdRunnable(User user) {
		return () -> {
			setCurrentUser(user);
			String emailOptionId = getEmailOptionId();
			Mail mail = getFirstMailSent();
			String token = getTokenFromTwoStepAuthMail(mail);
			authenticator.completeAuthenticationWithOptionId(emailOptionId, token);
		};

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

}
