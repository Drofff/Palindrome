package com.drofff.palindrome.utils;

import java.util.Deque;
import java.util.List;

import org.springframework.data.util.Pair;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

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
			uriBuilder = uriBuilder.queryParam(param.getFirst(), param.getSecond());
		}
		return uriBuilder.build().toString();
	}

}
