/**
 * 
 */
package de.dralle.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Disabled;
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

	@Test
	void testSHA256HashStringLC() {
		String input = "abcdefghijklmnopqrstuvwxyz";
		byte[] hash = HashingUtil.getHashSHA256(input);
		assertNotNull(hash);
	}

	@Test
	void testSHA256HashStringUC() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		byte[] hash = HashingUtil.getHashSHA256(input);
		assertNotNull(hash);
	}

	@Test
	void testSHA256HashStringSC() {
		String input = "!\"§$%&/()=?{[]}\\´`@+*~#'-_<>|,.";
		byte[] hash = HashingUtil.getHashSHA256(input);
		assertNotNull(hash);
	}

	@Test
	void testSHA256HashStringNumeric() {
		String input = "1234567890";
		byte[] hash = HashingUtil.getHashSHA256(input);
		assertNotNull(hash);
	}

	@Test
	void testSHA512HashStringLC() {
		String input = "abcdefghijklmnopqrstuvwxyz";
		byte[] hash = HashingUtil.getHashSHA512(input);
		assertNotNull(hash);
	}

	@Test
	void testSHA512HashStringUC() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		byte[] hash = HashingUtil.getHashSHA512(input);
		assertNotNull(hash);
	}

	@Test
	void testSHA512HashStringSC() {
		String input = "!\"§$%&/()=?{[]}\\´`@+*~#'-_<>|,.";
		byte[] hash = HashingUtil.getHashSHA512(input);
		assertNotNull(hash);
	}

	@Test
	void testSHA512HashStringNumeric() {
		String input = "1234567890";
		byte[] hash = HashingUtil.getHashSHA512(input);
		assertNotNull(hash);
	}

	@Test
	void testCheckSHA256HashStringLC() {
		String input = "abcdefghijklmnopqrstuvwxyz";
		byte[] hash = HashingUtil.getHashSHA256(input);
		assertTrue(HashingUtil.checkHashSHA256(input, hash));
	}

	@Test
	void testCheckSHA256HashStringUC() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		byte[] hash = HashingUtil.getHashSHA256(input);
		assertTrue(HashingUtil.checkHashSHA256(input, hash));
	}

	@Test
	void testCheckSHA256HashStringSC() {
		String input = "!\"§$%&/()=?{[]}\\´`@+*~#'-_<>|,.";
		byte[] hash = HashingUtil.getHashSHA256(input);
		assertTrue(HashingUtil.checkHashSHA256(input, hash));
	}

	@Test
	void testCheckSHA256HashStringNumeric() {
		String input = "1234567890";
		byte[] hash = HashingUtil.getHashSHA256(input);
		assertTrue(HashingUtil.checkHashSHA256(input, hash));
	}

	@Test
	void testCheckSHA512HashStringLC() {
		String input = "abcdefghijklmnopqrstuvwxyz";
		byte[] hash = HashingUtil.getHashSHA512(input);
		assertTrue(HashingUtil.checkHashSHA512(input, hash));
	}

	@Test
	void testCheckSHA512HashStringUC() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		byte[] hash = HashingUtil.getHashSHA512(input);
		assertTrue(HashingUtil.checkHashSHA512(input, hash));
	}

	@Test
	void testCheckSHA512HashStringSC() {
		String input = "!\"§$%&/()=?{[]}\\´`@+*~#'-_<>|,.";
		byte[] hash = HashingUtil.getHashSHA512(input);
		assertTrue(HashingUtil.checkHashSHA512(input, hash));
	}

	@Test
	void testCheckSHA512HashStringNumeric() {
		String input = "1234567890";
		byte[] hash = HashingUtil.getHashSHA512(input);
		assertTrue(HashingUtil.checkHashSHA512(input, hash));
	}

	@Test
	void testConcatArrays() {
		byte[] arr1 = new byte[] { 0, 1, 2, 3, 4 };
		byte[] arr2 = new byte[] { 5, 6, 7, 8, 9 };
		byte[] expected = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		byte[] actual = HashingUtil.concatenateArrays(arr1, arr2);
		assertArrayEquals(expected, actual);
	}

	@Test
	void testFullSaltedHashSHA256() {
		String input = "Hello World!";
		byte[] hash = HashingUtil.getSaltedHashIncludingSaltLenSHA256(input, new byte[4]);
		assertTrue(HashingUtil.checkSaltedHashSHA256(input, hash));
	}

	@Test
	void testFullSaltedHashSHA512() {
		String input = "Hello World!";
		byte[] hash = HashingUtil.getSaltedHashIncludingSaltLenSHA512(input, new byte[4]);
		assertTrue(HashingUtil.checkSaltedHashSHA512(input, hash));
	}

	@Disabled
	@Test
	void testOutput() {
		String input = "Hello World!";
		for (int i = 0; i < 10000; i++) {
			String b64Hash = Base64Util
					.encodeBytes2Str(HashingUtil.getSaltedHashIncludingSaltLenSHA512(input, new byte[4]));
			System.out.println(b64Hash);
		}

	}

	@Test
	void testCheckFailSHA256HashStringLC() {
		String input = "abcdefghijklmnopqrstuvwxyz";
		byte[] hash = HashingUtil.getHashSHA256(input);
		assertFalse(HashingUtil.checkHashSHA256(input + " ", hash));
	}

	@Test
	void testCheckFailSHA256HashStringUC() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		byte[] hash = HashingUtil.getHashSHA256(input);
		assertFalse(HashingUtil.checkHashSHA256(input + " ", hash));
	}

	@Test
	void testCheckFailSHA256HashStringSC() {
		String input = "!\"§$%&/()=?{[]}\\´`@+*~#'-_<>|,.";
		byte[] hash = HashingUtil.getHashSHA256(input);
		assertFalse(HashingUtil.checkHashSHA256(input + " ", hash));
	}

	@Test
	void testCheckFailSHA256HashStringNumeric() {
		String input = "1234567890";
		byte[] hash = HashingUtil.getHashSHA256(input);
		assertFalse(HashingUtil.checkHashSHA256(input + " ", hash));
	}

	@Test
	void testCheckFailSHA512HashStringLC() {
		String input = "abcdefghijklmnopqrstuvwxyz";
		byte[] hash = HashingUtil.getHashSHA512(input);
		assertFalse(HashingUtil.checkHashSHA512(input + " ", hash));
	}

	@Test
	void testCheckFailSHA512HashStringUC() {
		String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		byte[] hash = HashingUtil.getHashSHA512(input);
		assertFalse(HashingUtil.checkHashSHA512(input + " ", hash));
	}

	@Test
	void testCheckFailSHA512HashStringSC() {
		String input = "!\"§$%&/()=?{[]}\\´`@+*~#'-_<>|,.";
		byte[] hash = HashingUtil.getHashSHA512(input);
		assertFalse(HashingUtil.checkHashSHA512(input + " ", hash));
	}

	@Test
	void testCheckFailSHA512HashStringNumeric() {
		String input = "1234567890";
		byte[] hash = HashingUtil.getHashSHA512(input);
		assertFalse(HashingUtil.checkHashSHA512(input + " ", hash));
	}

	@Test
	void testFullSaltedHashSHA256CheckFail() {
		String input = "Hello World!";
		byte[] hash = HashingUtil.getSaltedHashIncludingSaltLenSHA256(input, new byte[4]);
		assertFalse(HashingUtil.checkSaltedHashSHA256(input + " ", hash));
	}

	@Test
	void testFullSaltedHashSHA512CheckFail() {
		String input = "Hello World!";
		byte[] hash = HashingUtil.getSaltedHashIncludingSaltLenSHA512(input, new byte[4]);
		assertFalse(HashingUtil.checkSaltedHashSHA512(input + " ", hash));
	}
}
