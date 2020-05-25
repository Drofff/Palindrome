package com.drofff.palindrome.utils;

import com.drofff.palindrome.exception.PalindromeException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static com.drofff.palindrome.utils.FileUtils.readFileFromClasspathAsStr;

public class JsonUtils {

	private static final JSONParser JSON_PARSER = new JSONParser();

	private JsonUtils() {}

	public static <T> T getValueOfClassFromFileByKey(String filename, String key, Class<T> valueClass) {
		Object value = getNodeFromFileByKey(filename, key);
		return valueClass.cast(value);
	}

	public static JSONObject getJSONObjectFromFileByKey(String filename, String key) {
		return (JSONObject) getNodeFromFileByKey(filename, key);
	}

	private static Object getNodeFromFileByKey(String filename, String key) {
		JSONObject json = loadJsonFileByName(filename);
		return json.get(key);
	}

	static JSONObject loadJsonFileByName(String filename) {
		try {
			return loadJsonFile(filename);
		} catch(ParseException e) {
			throw new PalindromeException(e.getMessage());
		}
	}

	private static JSONObject loadJsonFile(String filename) throws ParseException {
		String fileContent = readFileFromClasspathAsStr(filename);
		return (JSONObject) JSON_PARSER.parse(fileContent);
	}

}
