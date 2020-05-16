package com.drofff.palindrome.utils;

import com.drofff.palindrome.document.UserBlock;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.type.CollectionPage;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static com.drofff.palindrome.constants.EndpointConstants.ERROR_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.*;
import static com.drofff.palindrome.utils.CollectionPageUtils.countPages;
import static com.drofff.palindrome.utils.CollectionPageUtils.getContentOfPage;
import static com.drofff.palindrome.utils.DateUtils.dateTimeToStr;
import static com.drofff.palindrome.utils.FormattingUtils.uriWithQueryParams;
import static com.drofff.palindrome.utils.ListUtils.applyToEachListElement;

public class ModelUtils {

	private static final String REFERER_HEADER = "referer";

	private ModelUtils() {}

	public static void putValidationExceptionIntoModel(ValidationException e, Model model) {
		model.addAttribute(ERROR_MESSAGE_PARAM, e.getMessage());
		model.mergeAttributes(e.getFieldErrors());
	}

	public static String errorPageWithMessage(String message) {
		return redirectToWithMessage(ERROR_ENDPOINT, message);
	}

	public static String redirectToReferrerOfRequestWithMessage(HttpServletRequest request, String message) {
		String refererUri = request.getHeader(REFERER_HEADER);
		return redirectToWithMessage(refererUri, message);
	}

	public static String redirectToWithMessage(String uri, String message) {
		Pair<String, String> messageParam = Pair.of(MESSAGE_PARAM, message);
		String uriWithMessage = uriWithQueryParams(uri, Collections.singletonList(messageParam));
		return redirectTo(uriWithMessage);
	}

	public static String redirectTo(String uri) {
		return "redirect:" + uri;
	}

	public static void putCollectionPageIntoModel(CollectionPage<?> collectionPage, Model model) {
		model.addAttribute(PAGE_PAYLOAD_PARAM, getContentOfPage(collectionPage));
		model.addAttribute(PAGE_NUMBER_PARAM, collectionPage.getPageNumber());
		model.addAttribute(PAGES_COUNT_PARAM, countPages(collectionPage));
	}

	public static void putPageIntoModel(Page<?> page, Model model) {
		model.addAttribute(PAGE_PAYLOAD_PARAM, page.getContent());
		model.addAttribute(PAGE_NUMBER_PARAM, page.getNumber());
		model.addAttribute(PAGES_COUNT_PARAM, page.getTotalPages());
	}

	public static void putUserBlockIntoModel(UserBlock userBlock, Model model) {
		String dateTimeStr = dateTimeToStr(userBlock.getDateTime());
		model.addAttribute("dateTime", dateTimeStr);
		model.addAttribute("reason", userBlock.getReason());
	}

	public static <E, D> List<D> applyToPageContent(Function<E, D> function, Page<E> page) {
		return applyToEachListElement(function, page.getContent());
	}

}
