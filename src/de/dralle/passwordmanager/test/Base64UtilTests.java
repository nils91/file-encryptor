/**
 * 
 */
package de.dralle.passwordmanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.junit.jupiter.api.Test;

import de.dralle.passwordmanager.Base64Util;

/**
 * @author Nils Dralle
 *
 */
class Base64UtilTests {

	
	@Test
	void testByteEncodingDecoding() {
		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] encodedBytes = Base64Util.encodeByte(inputBytes);
		byte[] decodedBytes = Base64Util.decodeBytes(encodedBytes);
		assertArrayEquals(inputBytes, decodedBytes);
	}

	@Test
	void testSimpleStringEncodingDecoding() {
		String input = "Hello World!";
		String encoded = Base64Util.encodeStr2Str(input);
		String decoded = Base64Util.decodeStr2Str(encoded);
		assertEquals(input, decoded);
	}

	@Test
	void testSimpleStringEncodingDecodingNumeric() {
		String input = "1234567890";
		String encoded = Base64Util.encodeStr2Str(input);
		String decoded = Base64Util.decodeStr2Str(encoded);
		assertEquals(input, decoded);
	}

	@Test
	void testSimpleStringEncodingDecodingLowercaseChars() {
		String input = "abcdefghijklmnopqrstuvwxyz";
		String encoded = Base64Util.encodeStr2Str(input);
		String decoded = Base64Util.decodeStr2Str(encoded);
		assertEquals(input, decoded);
	}

	@Test
	void testSimpleStringEncodingDecodingUppercaseChars() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String encoded = Base64Util.encodeStr2Str(input);
		String decoded = Base64Util.decodeStr2Str(encoded);
		assertEquals(input, decoded);
	}

	@Test
	void testSimpleStringEncodingDecodingSpecialChars() {
		String input = "!\"§$%&/()=?{[]}\\´`@+*~#'-_<>|,.";
		String encoded = Base64Util.encodeStr2Str(input);
		String decoded = Base64Util.decodeStr2Str(encoded);
		assertEquals(input, decoded);
	}
}
