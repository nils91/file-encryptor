/**
 * 
 */
package de.dralle.passwordmanager;

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
	private static final int AUTH_TAG_LENGTH = 128;
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
		try {
			SecureRandom.getInstanceStrong().nextBytes(salt);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	 * @param iv    The IV is randomly generated in this method. üIt should be 16
	 *              byte (to math the auth tag). Check the contents after calling
	 *              for the used iv. The same IV needs to be used for encryption and
	 *              decryption.
	 * @return
	 */
	public static byte[] encrypt(byte[] input, SecretKey key, byte[] iv) {
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
