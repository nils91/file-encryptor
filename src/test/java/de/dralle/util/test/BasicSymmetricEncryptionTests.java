/**
 * 
 */
package de.dralle.util.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Nils Dralle
 *
 */
class BasicSymmetricEncryptionTests {

	@Disabled
	@Test
	void test() {
		fail("Not yet implemented");
	}

	@Test
	void testAESAvailability() {
		KeyGenerator keygen = null;
		try {
			keygen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			fail();
		}
		assertNotNull(keygen);
	}

	@Test
	void testAESRandomKeyGeneration() {
		KeyGenerator keygen = null;
		try {
			keygen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			fail();
		}
		try {
			keygen.init(256, SecureRandom.getInstanceStrong());
		} catch (NoSuchAlgorithmException e) {
			fail();
		}
		SecretKey key = keygen.generateKey();
		assertNotNull(key);
	}

	@Test
	void testAESRandomKeyGenerationKeyLength() {
		KeyGenerator keygen = null;
		try {
			keygen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			fail();
		}
		try {
			keygen.init(256, SecureRandom.getInstanceStrong());
		} catch (NoSuchAlgorithmException e) {
			fail();
		}
		SecretKey key = keygen.generateKey();
		assertEquals(256 / 8, key.getEncoded().length);
	}

	@Test
	void testAESKeyGenerationFromPW() {
		String pw = "Passwort123";
		byte[] salt = new byte[] { 0, 0, 0, 0 };
		SecretKeyFactory factory = null;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		KeySpec spec = new PBEKeySpec(pw.toCharArray(), salt, 65536, 256);
		SecretKey key = null;
		try {
			key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		assertNotNull(key);
	}

	@Test
	void testAESKeyGenerationFromPWKeyLength() {
		String pw = "Passwort123";
		byte[] salt = new byte[] { 0, 0, 0, 0 };
		SecretKeyFactory factory = null;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		KeySpec spec = new PBEKeySpec(pw.toCharArray(), salt, 65536, 256);
		SecretKey key = null;
		try {
			key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		assertEquals(256 / 8, key.getEncoded().length);
	}

	@Test
	void testAESKeyGenerationFromCorrectLengthByteArray() {
		SecretKey key = null;
		key = new SecretKeySpec(new byte[32], "AES");
		assertNotNull(key);
	}

	@Test
	void testAESKeyGenerationFromCorrectLengthByteArrayKeyLength() {
		SecretKeySpec key;
		key = new SecretKeySpec(new byte[32], "AES");
		assertEquals(256 / 8, key.getEncoded().length);
	}

	@Test
	void testCipherAvailability() {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/GCM/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		assertNotNull(cipher);
	}

	@Test
	void testEncryptionDecryptionEasy() {
		SecretKeySpec key;
		key = new SecretKeySpec(new byte[32], "AES");
		byte[] iv = new byte[12];
		Cipher cipher = null;
		byte[] plain = new byte[] { 1, 2, 3, 4 };
		try {
			cipher = Cipher.getInstance("AES/GCM/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		byte[] enc = null;
		try {
			enc = cipher.doFinal(plain);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		byte[] dec = null;
		try {
			dec = cipher.doFinal(enc);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertArrayEquals(plain, dec);
	}

	@Test
	void testEncryptionDecryptionBlockSmallerKey() {
		SecretKeySpec key;
		key = new SecretKeySpec(new byte[32], "AES");
		byte[] iv = new byte[12];
		Cipher cipher = null;
		byte[] plain = new byte[255];
		try {
			cipher = Cipher.getInstance("AES/GCM/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		byte[] enc = null;
		try {
			enc = cipher.doFinal(plain);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		byte[] dec = null;
		try {
			dec = cipher.doFinal(enc);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertArrayEquals(plain, dec);
	}

	@Test
	void testEncryptionDecryptionBlockBiggerKey() {
		SecretKeySpec key;
		key = new SecretKeySpec(new byte[32], "AES");
		byte[] iv = new byte[12];
		Cipher cipher = null;
		byte[] plain = new byte[257];
		try {
			cipher = Cipher.getInstance("AES/GCM/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		byte[] enc = null;
		try {
			enc = cipher.doFinal(plain);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		byte[] dec = null;
		try {
			dec = cipher.doFinal(enc);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertArrayEquals(plain, dec);
	}

	@Test
	void testEncryptionDecryptionBlockBiggerBiggerKey() {
		SecretKeySpec key;
		key = new SecretKeySpec(new byte[32], "AES");
		byte[] iv = new byte[12];
		Cipher cipher = null;
		byte[] plain = new byte[2000000];
		try {
			cipher = Cipher.getInstance("AES/GCM/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		byte[] enc = null;
		try {
			enc = cipher.doFinal(plain);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		byte[] dec = null;
		try {
			dec = cipher.doFinal(enc);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertArrayEquals(plain, dec);
	}
}
