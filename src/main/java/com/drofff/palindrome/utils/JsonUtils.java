package com.drofff.palindrome.utils;

import java.io.InputStream;
import java.util.Optional;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.drofff.palindrome.exception.PalindromeException;

public class JsonUtils {

	private static final JSONParser JSON_PARSER = new JSONParser();

	private JsonUtils() {}

	public static JSONObject getObjectFromFileByKey(String filename, String key) {
		JSONObject json = loadJsonFileByName(filename);
		return (JSONObject) json.get(key);
	}

	private static JSONObject loadJsonFileByName(String filename) {
		try {
			return loadJsonFile(filename);
		} catch(ParseException e) {
			throw new PalindromeException(e.getMessage());
		}
	}

	private static JSONObject loadJsonFile(String filename) throws ParseException {
		String fileContent = loadContentOfFileWithName(filename);
		return (JSONObject) JSON_PARSER.parse(fileContent);
	}

	private static String loadContentOfFileWithName(String filename) {
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
