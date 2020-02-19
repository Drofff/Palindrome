package com.drofff.palindrome.utils;

import static com.drofff.palindrome.constants.EndpointConstants.ERROR_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.ERROR_MESSAGE_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.PAGES_COUNT_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.PAGE_NUMBER_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.PAGE_PAYLOAD_PARAM;
import static com.drofff.palindrome.utils.FormattingUtils.uriWithQueryParams;
import static com.drofff.palindrome.utils.PageableUtils.countPagesOfCollection;
import static com.drofff.palindrome.utils.PageableUtils.getCollectionPageOfSize;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.data.util.Pair;
import org.springframework.ui.Model;

import com.drofff.palindrome.exception.ValidationException;

public class ModelUtils {

	private ModelUtils() {}

	public static void putValidationExceptionIntoModel(ValidationException e, Model model) {
		model.addAttribute(ERROR_MESSAGE_PARAM, e.getMessage());
		model.mergeAttributes(e.getFieldErrors());
	}

	public static String errorPageWithMessage(String message) {
		return redirectToWithMessage(ERROR_ENDPOINT, message);
	}

	public static String redirectToWithMessage(String uri, String message) {
		Pair<String, String> messageParam = Pair.of(MESSAGE_PARAM, message);
		String uriWithMessage = uriWithQueryParams(uri, Collections.singletonList(messageParam));
		return redirectTo(uriWithMessage);
	}

	private static String redirectTo(String uri) {
		return "redirect:" + uri;
	}

	public static void putPageOfCollectionIntoModel(int pageNumber, int pageSize, Collection<?> collection, Model model) {
		List<?> page = getCollectionPageOfSize(collection, pageNumber, pageSize);
		model.addAttribute(PAGE_PAYLOAD_PARAM, page);
		model.addAttribute(PAGE_NUMBER_PARAM, pageNumber);
		int pagesCount = countPagesOfCollection(collection, pageSize);
		model.addAttribute(PAGES_COUNT_PARAM, pagesCount);
	}

}
