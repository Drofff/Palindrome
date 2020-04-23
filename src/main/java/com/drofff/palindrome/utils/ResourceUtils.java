package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.StringUtils.removePartFromStr;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drofff.palindrome.exception.PalindromeException;

public class ResourceUtils {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceUtils.class);

	private static final Pattern FILE_SCHEME_PATTERN = Pattern.compile("^(file:[/\\\\])");

	private ResourceUtils() {}

	public static byte[] loadClasspathResource(String resourceUri) {
		String resourceUrl = getUrlOfClasspathResource(resourceUri);
		File resource = new File(resourceUrl);
		return readFile(resource);
	}

	public static String getUrlOfClasspathResource(String resourceUri) {
		ClassLoader classLoader = ReflectionUtils.class.getClassLoader();
		LOG.info("Resources are at {}", classLoader.getResource("").toString());
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

	private static byte[] readFile(File file) {
		try {
			Path pathToFile = file.toPath();
			return Files.readAllBytes(pathToFile);
		} catch(IOException e) {
			throw new PalindromeException(e.getMessage());
		}
	}

}
