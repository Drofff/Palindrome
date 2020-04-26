package com.drofff.palindrome.tool;

import com.drofff.palindrome.exception.PalindromeException;

public class ThreadTestTool {

	private ThreadTestTool() {}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch(InterruptedException e) {
			throw new PalindromeException(e);
		}
	}

}
