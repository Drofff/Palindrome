package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.FileUtils.readFile;
import static com.drofff.palindrome.utils.StringUtils.removePartFromStr;

import java.io.File;
import java.net.URL;
import java.util.Optional;

import com.drofff.palindrome.exception.PalindromeException;

public class ResourceUtils {

	private ResourceUtils() {}

	public static byte[] loadClasspathResource(String resourceUri) {
		String resourceUrl = getUrlOfClasspathResource(resourceUri);
		File resource = new File(resourceUrl);
		return readFile(resource);
	}

	public static String getUrlOfClasspathResource(String resourceUri) {
		return getClasspathResourceUrlIfPresent(resourceUri)
				.orElseThrow(() -> new PalindromeException("Resource " + resourceUri + " was not found"));
	}

	public static String getClasspathResourcesRootUrl() {
		return getClasspathResourceUrlIfPresent("")
				.orElseThrow(() -> new PalindromeException("Can not reach classpath resources root url"));
	}

	private static Optional<String> getClasspathResourceUrlIfPresent(String resourceUri) {
		ClassLoader classLoader = ReflectionUtils.class.getClassLoader();
		URL resourceUrl = classLoader.getResource(resourceUri);
		return Optional.ofNullable(resourceUrl)
				.map(URL::toString)
				.map(ResourceUtils::removeFileSchemeIfPresent);
	}

	private static String removeFileSchemeIfPresent(String url) {
		return removePartFromStr("file:", url);
	}

}
