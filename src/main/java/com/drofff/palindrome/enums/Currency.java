package com.drofff.palindrome.enums;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public enum Currency {

	USD, EUR, UAH;

	public static Set<Currency> getAvailableCurrencies() {
		Currency[] currencies = Currency.values();
		return Arrays.stream(currencies)
				.collect(toSet());
	}

}
