package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.JsonUtils.getValueOfClassFromFileByKey;

public class TicketUtils {

	private static final String TICKET_PARAMS_FILE = "ticket_params.json";

	private static final String CHARGE_ID_TITLE_PARAM = "charge-id";
	private static final String VIOLATION_ID_TITLE_PARAM = "violation-id";
	private static final String SUM_TITLE_PARAM = "sum";
	private static final String PAYER_TITLE_PARAM = "payer";

	private TicketUtils() {}

	public static String getChargeIdTitle() {
		return getTitleByKey(CHARGE_ID_TITLE_PARAM);
	}

	public static String getViolationIdTitle() {
		return getTitleByKey(VIOLATION_ID_TITLE_PARAM);
	}

	public static String getSumTitle() {
		return getTitleByKey(SUM_TITLE_PARAM);
	}

	public static String getPayerTitle() {
		return getTitleByKey(PAYER_TITLE_PARAM);
	}

	private static String getTitleByKey(String key) {
		return getValueOfClassFromFileByKey(TICKET_PARAMS_FILE, key, String.class);
	}

}
