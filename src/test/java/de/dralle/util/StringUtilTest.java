package de.dralle.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.dralle.util.StringUtil;

class StringUtilTest {

	@Test
	void testSimpleStringEncodingDecoding() {
		String input = "Hello World!";
		byte[] bytes = StringUtil.strToByteArr(input);
		String dStr = StringUtil.byteArrToStr(bytes);
		assertEquals(input, dStr);
	}

	@Test
	void testSimpleStringEncodingDecodingNumeric() {
		String input = "1234567890";
		byte[] bytes = StringUtil.strToByteArr(input);
		String dStr = StringUtil.byteArrToStr(bytes);
		assertEquals(input, dStr);
	}

	@Test
	void testSimpleStringEncodingDecodingLowercaseChars() {
		String input = "abcdefghijklmnopqrstuvwxyz";
		byte[] bytes = StringUtil.strToByteArr(input);
		String dStr = StringUtil.byteArrToStr(bytes);
		assertEquals(input, dStr);
	}

	@Test
	void testSimpleStringEncodingDecodingUppercaseChars() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		byte[] bytes = StringUtil.strToByteArr(input);
		String dStr = StringUtil.byteArrToStr(bytes);
		assertEquals(input, dStr);
	}

	@Test
	void testSimpleStringEncodingDecodingSpecialChars() {
		String input = "!\"§$%&/()=?{[]}\\´`@+*~#'-_<>|,.";
		byte[] bytes = StringUtil.strToByteArr(input);
		String dStr = StringUtil.byteArrToStr(bytes);
		assertEquals(input, dStr);
	}

}
