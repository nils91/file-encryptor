/**
 * 
 */
package de.dralle.util;

import java.nio.charset.Charset;

/**
 * @author Nils Dralle
 *
 */
public class StringUtil {

	private static Charset cs = Charset.forName("UTF-8");

	public static Charset getCharset() {
		return cs;
	}

	public static String byteArrToStr(byte[] arr) {
		return new String(arr, cs);
	}

	public static byte[] strToByteArr(String str) {
		if (str == null) {
			return null;
		}
		return str.getBytes(cs);
	}
}
