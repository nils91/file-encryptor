/**
 * 
 */
package de.dralle.passwordmanager;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 * @author Nils Dralle
 *
 */
public class Base64Util {
	private static Encoder encoder=Base64.getEncoder();
	private static Decoder decoder=Base64.getDecoder();
	public static byte[] encodeByte(byte[] bytes) {
		return encoder.encode(bytes);
	}
	public static byte[] decodeBytes(byte[] bytes) {
		return decoder.decode(bytes);
	}
	
	
	public static byte[] encodeString(String str) {
		return encodeByte(StringUtil.strToByteArr(str));
	}
	public static byte[] decodeString(String str) {
		return decodeBytes(StringUtil.strToByteArr(str));
	}
	public static String encodeStr2Str(String str) {
		return StringUtil.byteArrToStr(encodeByte(StringUtil.strToByteArr(str)));
	}
	public static String decodeStr2Str(String str) {
		return StringUtil.byteArrToStr(decodeBytes(StringUtil.strToByteArr(str)));
	}
}
