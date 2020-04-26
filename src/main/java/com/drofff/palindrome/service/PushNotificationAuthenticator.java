package com.drofff.palindrome.service;

import com.drofff.palindrome.document.UserDevice;
import com.drofff.palindrome.type.ExternalAuthenticationOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class PushNotificationAuthenticator extends TokenAuthenticator {

	private final UserDeviceService userDeviceService;

	@Value("${external.auth.max-token-wait-time}")
	private long maxTokenWaitTime;

	@Autowired
	public PushNotificationAuthenticator(UserDeviceService userDeviceService) {
		this.userDeviceService = userDeviceService;
	}

	@Override
	public Set<ExternalAuthenticationOption> getAuthenticationOptions() {
		return userDeviceService.getActiveDevices().stream()
				.map(UserDevice::toAuthOption)
				.collect(toSet());
	}

	@Override
	protected void sendTokenToAuthenticatorOfOptionWithId(String token, String optionId) {
		UserDevice optionAuthenticator = userDeviceService.getUserDeviceById(optionId);
		userDeviceService.requestExternalAuthThroughDeviceUsingToken(optionAuthenticator, token);
	}

	@Override
	protected long getMaxTokenWaitTime() {
		return maxTokenWaitTime;
	}

}
