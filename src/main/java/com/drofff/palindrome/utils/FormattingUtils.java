package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.StringUtils.removeAllNonDigits;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.util.Deque;
import java.util.List;

import org.springframework.data.util.Pair;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import com.drofff.palindrome.enums.ByteUnit;

public class FormattingUtils {

	private static final UriBuilderFactory URI_BUILDER_FACTORY = new DefaultUriBuilderFactory();

	private FormattingUtils() {}

	public static String putParamsIntoText(String text, Deque<Pair<String, String>> params) {
		if(params.isEmpty()) {
			return text;
		}
		Pair<String, String> param = params.pop();
		String textWithParam = putParamIntoText(text, param);
		return putParamsIntoText(textWithParam, params);
	}

	public static String putParamIntoText(String text, Pair<String, String> param) {
		return text.replace(param.getFirst(), param.getSecond());
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

}
