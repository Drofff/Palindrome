package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.StringUtils.removePartFromStr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.drofff.palindrome.exception.PalindromeException;

public class FileUtils {

	private FileUtils() {}

	public static byte[] readAndDeleteFileAtPath(Path path) {
		byte[] content = readFileAtPath(path);
		deleteFileAtPath(path);
		return content;
	}

	private static byte[] readFileAtPath(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch(IOException e) {
			throw new PalindromeException(e.getMessage());
		}
	}

	private static void deleteFileAtPath(Path path) {
		try {
			Files.delete(path);
		} catch(IOException e) {
			throw new PalindromeException(e.getMessage());
		}
	}

	public static void createDirectoryAtPath(Path path) {
		try {
			Files.createDirectory(path);
		} catch(IOException e) {
			throw new PalindromeException(e.getMessage());
		}
	}

	public static String relativeToAbsolutePath(String relativePath) {
		File fileAtPath = new File(relativePath);
		String absolutePath = fileAtPath.getAbsolutePath();
		return removeFileScheme(absolutePath);
	}

	private static String removeFileScheme(String url) {
		return removePartFromStr("file:", url);
	}

}
