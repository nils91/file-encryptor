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
class BasicBase64Tests {

	@Test
	void testByteEncodingDecoding() {
		Encoder encoder = Base64.getEncoder();
		Decoder decoder = Base64.getDecoder();
		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] encodedBytes = encoder.encode(inputBytes);
		byte[] decodedBytes = decoder.decode(encodedBytes);
		assertArrayEquals(inputBytes, decodedBytes);
	}
	@Test
	void testSimpleStringEncodingDecoding() {
		String input="Hello World!";
		Encoder encoder = Base64.getEncoder();
		Decoder decoder = Base64.getDecoder();
		byte[] inputBytes = input.getBytes();
		byte[] encodedBytes = encoder.encode(inputBytes);
		byte[] decodedBytes = decoder.decode(encodedBytes);
		String decoded=new String(decodedBytes);
		assertEquals(input, decoded);
	}
	@Test
	void testSimpleStringEncodingDecodingExplicitEncoding() {
		String input="Hello World!";
		Encoder encoder = Base64.getEncoder();
		Decoder decoder = Base64.getDecoder();
		byte[] inputBytes = input.getBytes(Charset.forName("UTF-8"));
		byte[] encodedBytes = encoder.encode(inputBytes);
		byte[] decodedBytes = decoder.decode(encodedBytes);
		String decoded=new String(decodedBytes,Charset.forName("UTF-8"));
		assertEquals(input, decoded);
	}
	@Test
	void testSimpleStringEncodingDecodingNumeric() {
		String input="1234567890";
		Encoder encoder = Base64.getEncoder();
		Decoder decoder = Base64.getDecoder();
		byte[] inputBytes = input.getBytes(Charset.forName("UTF-8"));
		byte[] encodedBytes = encoder.encode(inputBytes);
		byte[] decodedBytes = decoder.decode(encodedBytes);
		String decoded=new String(decodedBytes,Charset.forName("UTF-8"));
		assertEquals(input, decoded);
	}
	@Test
	void testSimpleStringEncodingDecodingLowercaseChars() {
		String input="abcdefghijklmnopqrstuvwxyz";
		Encoder encoder = Base64.getEncoder();
		Decoder decoder = Base64.getDecoder();
		byte[] inputBytes = input.getBytes(Charset.forName("UTF-8"));
		byte[] encodedBytes = encoder.encode(inputBytes);
		byte[] decodedBytes = decoder.decode(encodedBytes);
		String decoded=new String(decodedBytes,Charset.forName("UTF-8"));
		assertEquals(input, decoded);
	}
	@Test
	void testSimpleStringEncodingDecodingUppercaseChars() {
		String input="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Encoder encoder = Base64.getEncoder();
		Decoder decoder = Base64.getDecoder();
		byte[] inputBytes = input.getBytes(Charset.forName("UTF-8"));
		byte[] encodedBytes = encoder.encode(inputBytes);
		byte[] decodedBytes = decoder.decode(encodedBytes);
		String decoded=new String(decodedBytes,Charset.forName("UTF-8"));
		assertEquals(input, decoded);
	}
	@Test
	void testSimpleStringEncodingDecodingSpecialChars() {
		String input="!\"§$%&/()=?{[]}\\´`@+*~#'-_<>|,.";
		Encoder encoder = Base64.getEncoder();
		Decoder decoder = Base64.getDecoder();
		byte[] inputBytes = input.getBytes(Charset.forName("UTF-8"));
		byte[] encodedBytes = encoder.encode(inputBytes);
		byte[] decodedBytes = decoder.decode(encodedBytes);
		String decoded=new String(decodedBytes,Charset.forName("UTF-8"));
		assertEquals(input, decoded);
	}
}
