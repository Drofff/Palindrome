package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.FileUtils.readFile;
import static com.drofff.palindrome.utils.StringUtils.removePartFromStr;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.drofff.palindrome.exception.PalindromeException;

public class ResourceUtils {

	private static final Pattern FILE_SCHEME_PATTERN = Pattern.compile("^(file:[/\\\\])");

	private ResourceUtils() {}

	public static String getClasspathResourcesRootUrl() {
		ClassLoader classLoader = ReflectionUtils.class.getClassLoader();
		URL rootPath = classLoader.getResource("");
		return Optional.ofNullable(rootPath)
				.map(URL::toString)
				.orElseThrow(() -> new PalindromeException("Can not reach classpath resources root url"));
	}

	public static byte[] loadClasspathResource(String resourceUri) {
		String resourceUrl = getUrlOfClasspathResource(resourceUri);
		File resource = new File(resourceUrl);
		return readFile(resource);
	}

	public static String getUrlOfClasspathResource(String resourceUri) {
		ClassLoader classLoader = ReflectionUtils.class.getClassLoader();
		URL resourceUrl = classLoader.getResource(resourceUri);
		return Optional.ofNullable(resourceUrl)
				.map(URL::toString)
				.map(ResourceUtils::removeFileSchemeIfPresent)
				.orElseThrow(() -> new PalindromeException("Resource " + resourceUri + " was not found"));
	}

	private static String removeFileSchemeIfPresent(String url) {
		if(hasFileScheme(url)) {
			return removeFileScheme(url);
		}
		return url;
	}

	private static boolean hasFileScheme(String url) {
		return FILE_SCHEME_PATTERN.matcher(url).find();
	}

	private static String removeFileScheme(String url) {
		Matcher matcher = FILE_SCHEME_PATTERN.matcher(url);
		if(matcher.find()) {
			String fileScheme = matcher.group(1);
			return removePartFromStr(fileScheme, url);
		}
		throw new PalindromeException("URL has no file scheme");
	}

}
