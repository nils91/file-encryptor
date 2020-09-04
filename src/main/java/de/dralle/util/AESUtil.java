/**
 * 
 */
package de.dralle.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Nils Dralle
 *
 */
public class AESUtil {
	public static final String PADDING_MODE = "NoPadding";
	public static final String ALGO_SUBMODE = "GCM";
	public static final int AUTH_TAG_LENGTH = 128;
	public static final String ALGO_KEYGEN_PW = "PBKDF2WithHmacSHA256";
	public static final int KEYGEN_ITERATION_CNT = 65536;
	public static final String ALGO_ENCRYPTION = "AES";
	public static final int KEYSIZE_BITS = 256;
	public static final int KEYSIZE_BYTES = KEYSIZE_BITS / 8;

	public static SecretKey generateRandomKey() {
		KeyGenerator keygen = null;
		try {
			keygen = KeyGenerator.getInstance(ALGO_ENCRYPTION);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			keygen.init(KEYSIZE_BITS, SecureRandom.getInstanceStrong());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return keygen.generateKey();
	}

	/**
	 * 
	 * @param password
	 * @param salt     The salt is generated randomly and written to this array.
	 * @return
	 */
	public static SecretKey generateKeyFromPassword(String password, byte[] salt) {
		return generateKeyFromPassword(password, salt, false);
	}

	/**
	 * 
	 * @param password
	 * @param salt     The salt is generated randomly and written to this array.
	 * @return
	 */
	public static SecretKey generateKeyFromPassword(String password, byte[] salt, boolean skipRandomSaltGeneration) {
		if (!skipRandomSaltGeneration) {
			try {
				SecureRandom.getInstanceStrong().nextBytes(salt);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		SecretKeyFactory factory = null;
		try {
			factory = SecretKeyFactory.getInstance(ALGO_KEYGEN_PW);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, KEYGEN_ITERATION_CNT, KEYSIZE_BITS);
		SecretKey key = null;
		try {
			key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), ALGO_ENCRYPTION);
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	}

	public static SecretKey generateKeyFromByteArray(byte[] arr) {
		SecretKey key = new SecretKeySpec(arr, ALGO_ENCRYPTION);
		return key;
	}

	/**
	 * 
	 * @param input
	 * @param key
	 * @param iv    The IV is randomly generated in this method. It should be 16
	 *              byte (to match the auth tag). Check the contents after calling
	 *              for the used iv. The same IV needs to be used for encryption and
	 *              decryption.
	 * @return
	 */
	public static byte[] encrypt(byte[] input, SecretKey key, byte[] iv) {
		if (input == null) {
			return null;
		}
		try {
			SecureRandom.getInstanceStrong().nextBytes(iv);
		} catch (NoSuchAlgorithmException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(String.format("%s/%s/%s", ALGO_ENCRYPTION, ALGO_SUBMODE, PADDING_MODE));
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (NoSuchPaddingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(AUTH_TAG_LENGTH, iv));
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidAlgorithmParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] enc = null;
		try {
			enc = cipher.doFinal(input);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return enc;
	}

	/**
	 * Salt and IV are both added in front. Salt is optional and not added if null,
	 * in case the key wasn´t generated from a password.
	 * 
	 * @param encrypted
	 * @param salt
	 * @param iv
	 * @return
	 */
	public static byte[] addSaltAndIV(byte[] encrypted, byte[] salt, byte[] iv) {
		int ivLen = iv.length;
		int saltLen = -1;
		if (salt != null) {
			saltLen = salt.length;
		}
		byte[] ivWithLen = HashingUtil.concatenateArrays(new byte[] { (byte) ivLen }, iv);
		byte[] saltWithLen = null;
		if (saltLen > 0) {
			saltWithLen = HashingUtil.concatenateArrays(new byte[] { (byte) saltLen }, salt);
		}
		byte[] prefix = ivWithLen;
		if (saltWithLen != null) {
			// add salt to prefix if theres a salt
			prefix = HashingUtil.concatenateArrays(saltWithLen, ivWithLen);
		}
		return HashingUtil.concatenateArrays(prefix, encrypted);
	}

	public static byte[] decryptAssumingOnlyIVPrefix(byte[] input, SecretKey key) {
		int ivlen = input[0];
		byte[] iv = new byte[ivlen];
		for (int i = 0; i < iv.length; i++) {
			iv[i] = input[i + 1];
		}
		byte[] inputNoPrefix = new byte[input.length - ivlen - 1];
		for (int i = 0; i < inputNoPrefix.length; i++) { // remove iv prefix
			inputNoPrefix[i] = input[1 + ivlen + i];
		}
		return decrypt(inputNoPrefix, key, iv);

	}

	public static byte[] decryptAssumingSaltAndIVPrefix(byte[] input, String password) {
		int saltlen = input[0];
		byte[] salt = new byte[saltlen];
		for (int i = 0; i < salt.length; i++) {
			salt[i] = input[i + 1];
		}
		byte[] inputNoSaltPrefix = new byte[input.length - saltlen - 1];
		for (int i = 0; i < inputNoSaltPrefix.length; i++) { // remove salt prefix
			inputNoSaltPrefix[i] = input[1 + saltlen + i];
		}
		SecretKey key = generateKeyFromPassword(password, salt, true);
		return decryptAssumingOnlyIVPrefix(inputNoSaltPrefix, key);
	}

	public static byte[] decrypt(byte[] input, SecretKey key, byte[] iv) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(String.format("%s/%s/%s", ALGO_ENCRYPTION, ALGO_SUBMODE, PADDING_MODE));
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (NoSuchPaddingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(AUTH_TAG_LENGTH, iv));
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidAlgorithmParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] dec = null;
		try {
			dec = cipher.doFinal(input);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dec;
	}
}
