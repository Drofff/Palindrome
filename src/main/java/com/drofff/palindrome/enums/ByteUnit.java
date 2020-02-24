package com.drofff.palindrome.enums;

import static com.drofff.palindrome.utils.StringUtils.removeAllDigits;

import java.util.Arrays;

import com.drofff.palindrome.exception.PalindromeException;

public enum ByteUnit {

	BYTE(1, "", "B"), KILOBYTE(1024, "K", "KB"),
	MEGABYTE(1_048_576, "M", "MB"), GIGABYTE(1_073_741_824, "G", "GB");

	private final int sizeInBytes;
	private final String[] suffixes;

	ByteUnit(int sizeInBytes, String ... suffixes) {
		this.sizeInBytes = sizeInBytes;
		this.suffixes = suffixes;
	}

	public static ByteUnit parseFromStr(String bytesStr) {
		String byteUnitSuffix = extractByteUnitSuffix(bytesStr);
		return getByteUnitBySuffix(byteUnitSuffix);
	}

	private static String extractByteUnitSuffix(String bytesStr) {
		return removeAllDigits(bytesStr);
	}

	private static ByteUnit getByteUnitBySuffix(String suffix) {
		return Arrays.stream(ByteUnit.values())
				.filter(unit -> unit.hasSuffix(suffix))
				.findFirst()
				.orElseThrow(() -> new PalindromeException("Can not resolve byte unit suffix " + suffix));
	}

	private boolean hasSuffix(String suffix) {
		return Arrays.stream(suffixes)
				.anyMatch(sfx -> sfx.equalsIgnoreCase(suffix));
	}

	public int getSizeInBytes() {
		return sizeInBytes;
	}

}
