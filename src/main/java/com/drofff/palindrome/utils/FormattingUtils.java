package com.drofff.palindrome.utils;

import com.drofff.palindrome.collector.PairDequeCollector;
import com.drofff.palindrome.enums.ByteUnit;
import org.springframework.data.util.Pair;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import static com.drofff.palindrome.utils.StringUtils.removeAllNonDigits;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

public class FormattingUtils {

	private static final UriBuilderFactory URI_BUILDER_FACTORY = new DefaultUriBuilderFactory();

	private static final String PARAM_PREFIX = "${";
	private static final String PARAM_SUFFIX = "}";

	private static final String PATH_DELIMITER = "/";

	private FormattingUtils() {}

	public static String putParamsIntoText(String text, String ... params) {
		return putParamsIntoText(text, dequeOfParams(params));
	}

	private static Deque<Pair<String, String>> dequeOfParams(String ... params) {
		return Arrays.stream(params)
				.collect(new PairDequeCollector<>());
	}

	private static String putParamsIntoText(String text, Deque<Pair<String, String>> params) {
		if(params.isEmpty()) {
			return text;
		}
		Pair<String, String> param = params.pop();
		String textWithParam = putParamIntoText(text, param);
		return putParamsIntoText(textWithParam, params);
	}

	private static String putParamIntoText(String text, Pair<String, String> param) {
		String paramKey = PARAM_PREFIX + param.getFirst() + PARAM_SUFFIX;
		return text.replace(paramKey, param.getSecond());
	}

	public static String uriWithQueryParams(String uri, List<Pair<String, String>> queryParams) {
		UriBuilder uriBuilder = URI_BUILDER_FACTORY.uriString(uri);
		for(Pair<String, String> param : queryParams) {
			uriBuilder = uriBuilder.replaceQueryParam(param.getFirst(), param.getSecond());
		}
		return uriBuilder.build().toString();
	}

	public static long parseBytesFromStr(String bytesStr) {
		validateNotNull(bytesStr, "Bytes string is null");
		ByteUnit byteUnit = ByteUnit.parseFromStr(bytesStr);
		long relativeValue = getDigitsFromStr(bytesStr);
		return relativeValue * byteUnit.getSizeInBytes();
	}

	private static long getDigitsFromStr(String str) {
		String digitsStr = removeAllNonDigits(str);
		return Long.parseLong(digitsStr);
	}

	public static String concatPathSegments(String ... segments) {
		return String.join(PATH_DELIMITER, segments);
	}

	public static String appendUrlPathSegment(String filePath, String segment) {
		File file = new File(filePath, segment);
		return file.getAbsolutePath();
	}

}
