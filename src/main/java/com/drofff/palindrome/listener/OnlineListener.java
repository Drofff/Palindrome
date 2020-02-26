package com.drofff.palindrome.listener;

import static com.drofff.palindrome.cache.OnlineCache.decrementOnlineCounter;
import static com.drofff.palindrome.cache.OnlineCache.incrementOnlineCounter;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OnlineListener implements HttpSessionListener {

	private static final Logger LOG = LoggerFactory.getLogger(OnlineListener.class);

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		LOG.info("Detected new http session. Incrementing online counter");
		incrementOnlineCounter();
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		LOG.info("Detected http session removal. Decrementing online counter");
		decrementOnlineCounter();
	}

}
