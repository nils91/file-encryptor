/**
 * 
 */
package de.dralle.passwordmanager;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * @author Nils Dralle
 *
 */
public class AESUtil {
	public static final String ALGO_ENCRYPTION="AES";
	public static final int KEYSIZE_BITTS=256;
	public static final int KEYSIZE_BYTES=8;
	public static SecretKey generateRandomKey() {
		KeyGenerator keygen = null;
		try {
			keygen = KeyGenerator.getInstance(ALGO_ENCRYPTION);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			keygen.init(256,SecureRandom.getInstanceStrong());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return keygen.generateKey();
	}
}
