/**
 * 
 */
package de.dralle.util;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 * @author Nils Dralle
 *
 */
public class Base64Util {
	private static Encoder encoder = Base64.getEncoder();
	private static Decoder decoder = Base64.getDecoder();

	public static byte[] encodeBytes(byte[] bytes) {
		return encoder.encode(bytes);
	}

	public static byte[] decodeBytes(byte[] bytes) {
		return decoder.decode(bytes);
	}

	public static String encodeBytes2Str(byte[] bytes) {
		return StringUtil.byteArrToStr(encodeBytes(bytes));
	}

	public static String decodeBytes2Str(byte[] bytes) {
		return StringUtil.byteArrToStr(decodeBytes(bytes));
	}

	public static byte[] encodeString(String str) {
		return encodeBytes(StringUtil.strToByteArr(str));
	}

	public static byte[] decodeString(String str) {
		return decodeBytes(StringUtil.strToByteArr(str));
	}

	public static String encodeStr2Str(String str) {
		return StringUtil.byteArrToStr(encodeBytes(StringUtil.strToByteArr(str)));
	}

	public static String decodeStr2Str(String str) {
		return StringUtil.byteArrToStr(decodeBytes(StringUtil.strToByteArr(str)));
	}
}
