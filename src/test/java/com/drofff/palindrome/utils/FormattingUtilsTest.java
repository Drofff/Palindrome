package com.drofff.palindrome.utils;

import static com.drofff.palindrome.utils.FormattingUtils.parseBytesFromStr;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.drofff.palindrome.enums.ByteUnit;
import com.drofff.palindrome.exception.ValidationException;

public class FormattingUtilsTest {

	@Test
	public void parseFileSizeBytesFromStrTest() {
		long expectedFileSize = 10 * ByteUnit.MEGABYTE.getSizeInBytes();
		String fileSizeStr = "10MB";
		long fileSizeBytes = parseBytesFromStr(fileSizeStr);
		assertEquals(expectedFileSize, fileSizeBytes);
	}

	@Test(expected = ValidationException.class)
	public void parseFileSizeBytesFromStrNullTest() {
		parseBytesFromStr(null);
	}

}
