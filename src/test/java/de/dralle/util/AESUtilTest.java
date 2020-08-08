/**
 * 
 */
package de.dralle.util;

import static org.junit.jupiter.api.Assertions.*;

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

import de.dralle.util.AESUtil;

/**
 * @author Nils Dralle
 *
 */
class AESUtilTest {
	@Disabled
	@Test
	void test() {
		fail("Not yet implemented");
	}

	@Test
	void testAESAvailability() {
		KeyGenerator keygen = null;
		try {
			keygen = KeyGenerator.getInstance(AESUtil.ALGO_ENCRYPTION);
		} catch (NoSuchAlgorithmException e) {
			fail();
		}
		assertNotNull(keygen);
	}

	@Test
	void testAESRandomKeyGeneration() {
		SecretKey key = AESUtil.generateRandomKey();
		assertNotNull(key);
	}

	@Test
	void testAESRandomKeyGenerationKeyLength() {
		SecretKey key = AESUtil.generateRandomKey();
		assertEquals(256 / 8, key.getEncoded().length);
	}

	@Test
	void testAESKeyGenerationFromPW() {
		String pw = "Passwort123";
		SecretKey key = AESUtil.generateKeyFromPassword(pw, new byte[8]);
		assertNotNull(key);
	}

	@Test
	void testAESKeyGenerationFromPWKeyLength() {
		String pw = "Passwort123";
		SecretKey key = AESUtil.generateKeyFromPassword(pw, new byte[8]);
		assertEquals(256 / 8, key.getEncoded().length);
	}

	@Test
	void testAESKeyGenerationFromCorrectLengthByteArray() {
		SecretKey key = AESUtil.generateKeyFromByteArray(new byte[32]);
		assertNotNull(key);
	}

	@Test
	void testAESKeyGenerationFromCorrectLengthByteArrayKeyLength() {
		SecretKey key = AESUtil.generateKeyFromByteArray(new byte[32]);
		assertEquals(256 / 8, key.getEncoded().length);
	}

	@Test
	void testEncryptionDecryptionEasy() {
		byte[] plain = new byte[] { 1, 2, 3, 4 };
		byte[] iv = new byte[16];
		SecretKey key = AESUtil.generateRandomKey();
		byte[] enc = AESUtil.encrypt(plain, key, iv);
		byte[] dec = AESUtil.decrypt(enc, key, iv);
		assertArrayEquals(plain, dec);
	}	
	@Test
	void testEncryptionDecryptionEasyIVinENC() {
		byte[] plain = new byte[] { 1, 2, 3, 4 };
		byte[] iv = new byte[16];
		SecretKey key = AESUtil.generateRandomKey();
		byte[] enc = AESUtil.addSaltAndIV(AESUtil.encrypt(plain, key, iv),null,iv);
		byte[] dec = AESUtil.decryptAssumingOnlyIVPrefix(enc, key);
		assertArrayEquals(plain, dec);
	}
	@Test
	void testEncryptionDecryptionEasyIVandSaltInENC() {
		String password="Passwort123";
		byte[] plain = new byte[] { 1, 2, 3, 4 };
		byte[] iv = new byte[16];
		byte[] salt=new byte[8];
		SecretKey key = AESUtil.generateKeyFromPassword(password, salt);
		byte[] enc = AESUtil.addSaltAndIV(AESUtil.encrypt(plain, key, iv),salt,iv);
		byte[] dec = AESUtil.decryptAssumingSaltAndIVPrefix(enc, password);
		assertArrayEquals(plain, dec);
	}
	@Test
	void testEncryptionDecryptionBlockSmallerKey() {

		byte[] plain = new byte[255];
		byte[] iv = new byte[16];
		SecretKey key = AESUtil.generateRandomKey();
		byte[] enc = AESUtil.encrypt(plain, key, iv);
		byte[] dec = AESUtil.decrypt(enc, key, iv);

		assertArrayEquals(plain, dec);
	}

	@Test
	void testEncryptionDecryptionBlockBiggerKey() {

		byte[] plain = new byte[257];
		byte[] iv = new byte[16];
		SecretKey key = AESUtil.generateRandomKey();
		byte[] enc = AESUtil.encrypt(plain, key, iv);
		byte[] dec = AESUtil.decrypt(enc, key, iv);

		assertArrayEquals(plain, dec);
	}

	@Test
	void testEncryptionDecryptionBlockBiggerBiggerKey() {

		byte[] plain = new byte[2000000];
		byte[] iv = new byte[16];
		SecretKey key = AESUtil.generateRandomKey();
		byte[] enc = AESUtil.encrypt(plain, key, iv);
		byte[] dec = AESUtil.decrypt(enc, key, iv);

		assertArrayEquals(plain, dec);
	}
}
