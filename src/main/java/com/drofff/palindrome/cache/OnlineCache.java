package com.drofff.palindrome.cache;

import java.util.concurrent.atomic.AtomicLong;

public class OnlineCache {

	private static AtomicLong onlineCounter = new AtomicLong(0);

	private OnlineCache() {}

	public static void incrementOnlineCounter() {
		onlineCounter.incrementAndGet();
	}

	public static void decrementOnlineCounter() {
		onlineCounter.decrementAndGet();
	}

	public static long getOnlineCounterValue() {
		return onlineCounter.get();
	}

}
