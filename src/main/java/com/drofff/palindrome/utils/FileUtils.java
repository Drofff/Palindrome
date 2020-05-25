package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.StringUtils.removePartFromStr;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;

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

	public static String readFileFromClasspathAsStr(String filename) {
		InputStream fileInputStream = getResourceFromClasspathByName(filename);
		return readAsStr(fileInputStream);
	}

	private static InputStream getResourceFromClasspathByName(String name) {
		InputStream inputStream = JsonUtils.class.getClassLoader()
				.getResourceAsStream(name);
		return Optional.ofNullable(inputStream)
				.orElseThrow(() -> new PalindromeException("Can not load resource " + name + " from classpath"));
	}

	private static String readAsStr(InputStream inputStream) {
		Scanner scanner = new Scanner(inputStream);
		StringBuilder stringBuilder = new StringBuilder();
		while(scanner.hasNext()) {
			String line = scanner.nextLine();
			stringBuilder.append(line);
		}
		return stringBuilder.toString();
	}

}
