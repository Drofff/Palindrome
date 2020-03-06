package com.drofff.palindrome.utils;

import java.io.File;

public class FileUtils {

	private FileUtils() {}

	public static String getAbsolutePathOfFileWithName(String filename) {
		return new File(filename).getAbsolutePath();
	}

}
