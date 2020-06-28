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
	private static Charset cs=Charset.forName("UTF-8");
	public static byte[] encodeByte(byte[] bytes) {
		return encoder.encode(bytes);
	}
	public static byte[] decodeBytes(byte[] bytes) {
		return decoder.decode(bytes);
	}
	
	public static Charset getCharset() {
		return cs;
	}
	public static String byteArrToStr(byte[] arr) {
		return new String(arr, cs);
	}
	public static byte[] strToByteArr(String str) {
		if(str==null) {
			return null;
		}
		return str.getBytes(cs);
	}
	public static byte[] encodeString(String str) {
		return encodeByte(strToByteArr(str));
	}
	public static byte[] decodeString(String str) {
		return decodeBytes(strToByteArr(str));
	}
	public static String encodeStr2Str(String str) {
		return byteArrToStr(encodeByte(strToByteArr(str)));
	}
	public static String decodeStr2Str(String str) {
		return byteArrToStr(decodeBytes(strToByteArr(str)));
	}
}
