/**
 * 
 */
package de.dralle.passwordmanager;

import static org.junit.jupiter.api.Assertions.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

/**
 * @author Nils Dralle
 *
 */
class HashingUtilTest {

	@Test
	void testHashingSHA1() throws NoSuchAlgorithmException {
		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes = HashingUtil.getHash(inputBytes, HashingUtil.ALGO_SHA_1);
		assertNotNull(hashBytes);
	}

	@Test
	void testHashNotSameAsInputSHA1() throws NoSuchAlgorithmException {
		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes = HashingUtil.getHash(inputBytes, HashingUtil.ALGO_SHA_1);
		boolean notsame = false;
		for (int i = 0; i < hashBytes.length; i++) {
			if (i >= inputBytes.length) {
				break;
			}
			if (inputBytes[i] != hashBytes[i]) {
				notsame = true;
			}
		}
		assertTrue(notsame);
	}

	@Test
	void testHashNotSameWhenInputChangeSHA1() throws NoSuchAlgorithmException {
		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes = HashingUtil.getHash(inputBytes, HashingUtil.ALGO_SHA_1);
		byte[] inputBytes2 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 8 };
		byte[] hashBytes2 = HashingUtil.getHash(inputBytes2, HashingUtil.ALGO_SHA_1);
		boolean notsame = false;
		for (int i = 0; i < hashBytes.length; i++) {
			if (i >= hashBytes2.length) {
				break;
			}
			if (hashBytes2[i] != hashBytes[i]) {
				notsame = true;
			}
		}
		assertTrue(notsame);
	}

	@Test
	void testHashSameWhenInputSameSHA1() throws NoSuchAlgorithmException {		
		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes = HashingUtil.getHash(inputBytes, HashingUtil.ALGO_SHA_1);
		byte[] inputBytes2 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes2 = HashingUtil.getHash(inputBytes2, HashingUtil.ALGO_SHA_1);
		assertArrayEquals(hashBytes, hashBytes2);
	}

	@Test
	void testHashingSHA256() {
		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes = HashingUtil.getHashSHA256(inputBytes);
		assertNotNull(hashBytes);
	}

	@Test
	void testHashNotSameAsInputSHA256() {

		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes = HashingUtil.getHashSHA256(inputBytes);
		boolean notsame = false;
		for (int i = 0; i < hashBytes.length; i++) {
			if (i >= inputBytes.length) {
				break;
			}
			if (inputBytes[i] != hashBytes[i]) {
				notsame = true;
			}
		}
		assertTrue(notsame);
	}

	@Test
	void testHashNotSameWhenInputChangeSHA256() {

		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		byte[] hashBytes = HashingUtil.getHashSHA256(inputBytes);
		byte[] inputBytes2 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 8 };
		byte[] hashBytes2 = HashingUtil.getHashSHA256(inputBytes2);
		boolean notsame = false;
		for (int i = 0; i < hashBytes.length; i++) {
			if (i >= hashBytes2.length) {
				break;
			}
			if (hashBytes2[i] != hashBytes[i]) {
				notsame = true;
			}
		}
		assertTrue(notsame);
	}

	@Test
	void testHashSameWhenInputSameSHA256() {

		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes = HashingUtil.getHashSHA256(inputBytes);
		byte[] inputBytes2 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes2 = HashingUtil.getHashSHA256(inputBytes2);
		assertArrayEquals(hashBytes, hashBytes2);
	}

	@Test
	void testHashingSHA512() {
		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes = HashingUtil.getHashSHA512(inputBytes);
		assertNotNull(hashBytes);
	}

	@Test
	void testHashNotSameAsInputSHA512() {	
		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes = HashingUtil.getHashSHA512(inputBytes);
		boolean notsame = false;
		for (int i = 0; i < hashBytes.length; i++) {
			if (i >= inputBytes.length) {
				break;
			}
			if (inputBytes[i] != hashBytes[i]) {
				notsame = true;
			}
		}
		assertTrue(notsame);
	}

	@Test
	void testHashNotSameWhenInputChangeSHA512() {
		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes = HashingUtil.getHashSHA512(inputBytes);
		byte[] inputBytes2 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 8 };
		byte[] hashBytes2 = HashingUtil.getHashSHA512(inputBytes2);
		boolean notsame = false;
		for (int i = 0; i < hashBytes.length; i++) {
			if (i >= hashBytes2.length) {
				break;
			}
			if (hashBytes2[i] != hashBytes[i]) {
				notsame = true;
			}
		}
		assertTrue(notsame);
	}

	@Test
	void testHashSameWhenInputSameSHA512() {	
		byte[] inputBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes = HashingUtil.getHashSHA512(inputBytes);
		byte[] inputBytes2 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] hashBytes2 = HashingUtil.getHashSHA512(inputBytes2);
		assertArrayEquals(hashBytes, hashBytes2);
	}

}
