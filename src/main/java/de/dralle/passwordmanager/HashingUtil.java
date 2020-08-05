/**
 * 
 */
package de.dralle.passwordmanager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author Nils Dralle
 *
 */
public class HashingUtil {
	@Deprecated
	public static final String ALGO_SHA_1 = "SHA-1";

	public static final String ALGO_SHA_256 = "SHA-256";
	public static final String ALGO_SHA_512 = "SHA-512";

	public static byte[] getHash(String input, String algorithm) throws NoSuchAlgorithmException {
		return getHash(StringUtil.strToByteArr(input), algorithm);

	}

	public  static byte[] getHash(byte[] input, String algorithm) throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance(algorithm);
		return sha.digest(input);
	}

	public static  boolean checkHash(byte[] input, byte[] hash, String algorithm) throws NoSuchAlgorithmException {
		byte[] inHash = getHash(input, algorithm);
		return Arrays.equals(hash, inHash);
	}

	public static  boolean checkHashSHA256(byte[] input, byte[] hash) {
		byte[] inHash = getHashSHA256(input);
		return Arrays.equals(hash, inHash);
	}

	public static  boolean checkHashSHA512(byte[] input, byte[] hash) {
		byte[] inHash = getHashSHA512(input);
		return Arrays.equals(hash, inHash);
	}

	public static  boolean checkHash(String input, byte[] hash, String algorithm) throws NoSuchAlgorithmException {
		byte[] inHash = getHash(input, algorithm);
		return Arrays.equals(hash, inHash);
	}

	public  static boolean checkHashSHA256(String input, byte[] hash) {
		byte[] inHash = getHashSHA256(input);
		return Arrays.equals(hash, inHash);
	}

	public  static boolean checkHashSHA512(String input, byte[] hash) {
		byte[] inHash = getHashSHA512(input);
		return Arrays.equals(hash, inHash);
	}

	public  static byte[] getHashSHA256(byte[] input) {
		try {
			return getHash(input, ALGO_SHA_256);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public  static byte[] getHashSHA512(byte[] input) {
		try {
			return getHash(input, ALGO_SHA_512);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static  byte[] getHashSHA256(String input) {
		try {
			return getHash(input, ALGO_SHA_256);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public  static byte[] getHashSHA512(String input) {
		try {
			return getHash(input, ALGO_SHA_512);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param input
	 * @param salt      Salt will be generated randomly and be written into the
	 *                  given array. It will also be added to the front of the
	 *                  returned hash. Salt length is determined by the size of this
	 *                  array. If salt is null, just the hash of input will be
	 *                  returned.
	 * @param algorithm
	 * @return Salted hash. The salt will be added in front of the hash.
	 * @throws NoSuchAlgorithmException
	 */
	public  static byte[] getSaltedHash(byte[] input, byte[] salt, String algorithm) throws NoSuchAlgorithmException {
		if (salt == null) {
			return getHash(input, algorithm);
		}
		SecureRandom.getInstanceStrong().nextBytes(salt);
		byte[] saltedInput = concatenateArrays(salt, input);
		byte[] hash = getHash(saltedInput, algorithm);
		byte[] saltedHash = concatenateArrays(salt, hash);
		return saltedHash;
	}

	/**
	 * The first byte of the returned salted hash is the length of the salt.
	 * 
	 * @param input
	 * @param salt      Salt will be generated randomly and be written into the
	 *                  given array. It will also be added to the front of the
	 *                  returned hash. Salt length is determined by the size of this
	 *                  array. If salt is null, just the hash of input will be
	 *                  returned.
	 * @param algorithm
	 * @return Salted hash. The salt will be added in front of the hash.
	 * @throws NoSuchAlgorithmException
	 */
	public  static byte[] getSaltedHashIncludingSaltLen(byte[] input, byte[] salt, String algorithm)
			throws NoSuchAlgorithmException {
		if (salt == null) {
			return getHash(input, algorithm);
		}
		int saltLen = salt.length;
		byte[] saltedHash = getSaltedHash(input, salt, algorithm);
		byte[] saltedHashWSaltLen = concatenateArrays(new byte[] { (byte) saltLen }, saltedHash);
		return saltedHashWSaltLen;
	}

	public  static byte[] concatenateArrays(byte[] arr1, byte[] arr2) {
		byte[] newArr = new byte[arr1.length + arr2.length];
		for (int i = 0; i < newArr.length; i++) {
			if (i < arr1.length) {
				newArr[i] = arr1[i];
			} else {
				newArr[i] = arr2[i - arr1.length];
			}
		}
		return newArr;
	}

	/**
	 * 
	 * @param input
	 * @param salt      Byte array to be combined with input as the salt before
	 *                  hashing.
	 * @param hash      Not salted hash.
	 * @param algorithm
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public  static boolean checkSaltedHash(byte[] input, byte[] salt, byte[] hash, String algorithm)
			throws NoSuchAlgorithmException {
		if (salt == null) {
			return checkHash(input, hash, algorithm);
		}
		byte[] saltedInput = concatenateArrays(salt, hash);
		byte[] inHash = getHash(saltedInput, algorithm);
		return hash.equals(inHash);
	}

	public static  boolean checkSaltedHash(byte[] input, int saltLen, byte[] saltedHash, String algorithm)
			throws NoSuchAlgorithmException {
		byte[] salt = new byte[saltLen];
		for (int i = 0; i < salt.length; i++) {
			salt[i] = saltedHash[i];
		}
		byte[] hash = new byte[saltedHash.length - saltLen];
		for (int i = 0; i < hash.length; i++) {
			hash[i] = saltedHash[saltLen + i];
		}
		return checkSaltedHash(input, salt, hash, algorithm);
	}

	/**
	 * If no salt length is given, it is assumed that the first byte of saltedHash
	 * is the salt length.
	 * 
	 * @param input
	 * @param saltedHash
	 * @param algorithm
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static  boolean checkSaltedHash(byte[] input, byte[] saltedHash, String algorithm) throws NoSuchAlgorithmException {
		int saltLen = saltedHash[0];
		byte[] saltedHashWOSaltLen = new byte[saltedHash.length - 1];
		for (int i = 0; i < saltedHashWOSaltLen.length; i++) {
			saltedHashWOSaltLen[i] = saltedHash[i + 1];
		}
		return checkSaltedHash(input, saltLen, saltedHashWOSaltLen, algorithm);
	}

	/**
	 * 
	 * @param input
	 * @param salt      Salt will be generated randomly and be written into the
	 *                  given array. It will also be added to the front of the
	 *                  returned hash. Salt length is determined by the size of this
	 *                  array. If salt is null, just the hash of input will be
	 *                  returned.
	 * @param algorithm
	 * @return Salted hash. The salt will be added in front of the hash.
	 * @throws NoSuchAlgorithmException
	 */
	public  static byte[] getSaltedHash(String input, byte[] salt, String algorithm) throws NoSuchAlgorithmException {
		byte[] inputArr = StringUtil.strToByteArr(input);
		return getSaltedHash(inputArr, salt, algorithm);
	}

	/**
	 * The first byte of the returned salted hash is the length of the salt.
	 * 
	 * @param input
	 * @param salt      Salt will be generated randomly and be written into the
	 *                  given array. It will also be added to the front of the
	 *                  returned hash. Salt length is determined by the size of this
	 *                  array. If salt is null, just the hash of input will be
	 *                  returned.
	 * @param algorithm
	 * @return Salted hash. The salt will be added in front of the hash.
	 * @throws NoSuchAlgorithmException
	 */
	public  static byte[] getSaltedHashIncludingSaltLen(String input, byte[] salt, String algorithm)
			throws NoSuchAlgorithmException {
		byte[] inputArr = StringUtil.strToByteArr(input);
		return getSaltedHashIncludingSaltLen(inputArr, salt, algorithm);
	}

	/**
	 * 
	 * @param input
	 * @param salt      Byte array to be combined with input as the salt before
	 *                  hashing.
	 * @param hash      Not salted hash.
	 * @param algorithm
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public  static boolean checkSaltedHash(String input, byte[] salt, byte[] hash, String algorithm)
			throws NoSuchAlgorithmException {
		byte[] inputArr = StringUtil.strToByteArr(input);
		return checkSaltedHash(inputArr, salt, hash, algorithm);
	}

	public static boolean checkSaltedHash(String input, int saltLen, byte[] saltedHash, String algorithm)
			throws NoSuchAlgorithmException {
		byte[] inputArr = StringUtil.strToByteArr(input);
		return checkSaltedHash(inputArr, saltLen, saltedHash, algorithm);
	}

	/**
	 * If no salt length is given, it is assumed that the first byte of saltedHash
	 * is the salt length.
	 * 
	 * @param input
	 * @param saltedHash
	 * @param algorithm
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public  static boolean checkSaltedHash(String input, byte[] saltedHash, String algorithm) throws NoSuchAlgorithmException {
		byte[] inputArr = StringUtil.strToByteArr(input);
		return checkSaltedHash(inputArr, saltedHash, algorithm);
	}

	/**
	 * 
	 * @param input
	 * @param salt  Salt will be generated randomly and be written into the given
	 *              array. It will also be added to the front of the returned hash.
	 *              Salt length is determined by the size of this array. If salt is
	 *              null, just the hash of input will be returned.
	 * @return Salted hash. The salt will be added in front of the hash.
	 */
	public  static byte[] getSaltedHashSHA256(String input, byte[] salt) {
		try {
			return getSaltedHash(input, salt, ALGO_SHA_256);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * The first byte of the returned salted hash is the length of the salt.
	 * 
	 * @param input
	 * @param salt  Salt will be generated randomly and be written into the given
	 *              array. It will also be added to the front of the returned hash.
	 *              Salt length is determined by the size of this array. If salt is
	 *              null, just the hash of input will be returned.
	 * @return Salted hash. The salt will be added in front of the hash.
	 */
	public  static byte[] getSaltedHashIncludingSaltLenSHA256(String input, byte[] salt) {
		try {
			return getSaltedHashIncludingSaltLen(input, salt, ALGO_SHA_256);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param input
	 * @param salt  Byte array to be combined with input as the salt before hashing.
	 * @param hash  Not salted hash.
	 * @return
	 */
	public  static boolean checkSaltedHashSHA256(String input, byte[] salt, byte[] hash) {
		try {
			return checkSaltedHash(input, salt, hash, ALGO_SHA_256);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public  static boolean checkSaltedHashSHA256(String input, int saltLen, byte[] saltedHash) {
		try {
			return checkSaltedHash(input, saltLen, saltedHash, ALGO_SHA_256);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * If no salt length is given, it is assumed that the first byte of saltedHash
	 * is the salt length.
	 * 
	 * @param input
	 * @param saltedHash
	 * @return
	 */
	public  static boolean checkSaltedHashSHA256(String input, byte[] saltedHash) {
		try {
			return checkSaltedHash(input, saltedHash, ALGO_SHA_256);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param input
	 * @param salt  Salt will be generated randomly and be written into the given
	 *              array. It will also be added to the front of the returned hash.
	 *              Salt length is determined by the size of this array. If salt is
	 *              null, just the hash of input will be returned.
	 * @return Salted hash. The salt will be added in front of the hash.
	 */
	public  static byte[] getSaltedHashSHA512(String input, byte[] salt) {
		try {
			return getSaltedHash(input, salt, ALGO_SHA_512);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * The first byte of the returned salted hash is the length of the salt.
	 * 
	 * @param input
	 * @param salt  Salt will be generated randomly and be written into the given
	 *              array. It will also be added to the front of the returned hash.
	 *              Salt length is determined by the size of this array. If salt is
	 *              null, just the hash of input will be returned.
	 * @return Salted hash. The salt will be added in front of the hash.
	 */
	public  static byte[] getSaltedHashIncludingSaltLenSHA512(String input, byte[] salt) {
		try {
			return getSaltedHashIncludingSaltLen(input, salt, ALGO_SHA_512);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param input
	 * @param salt  Byte array to be combined with input as the salt before hashing.
	 * @param hash  Not salted hash.
	 * @return
	 */
	public static  boolean checkSaltedHashSHA512(String input, byte[] salt, byte[] hash) {
		try {
			return checkSaltedHash(input, salt, hash, ALGO_SHA_512);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static  boolean checkSaltedHashSHA512(String input, int saltLen, byte[] saltedHash) {
		try {
			return checkSaltedHash(input, saltLen, saltedHash, ALGO_SHA_512);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * If no salt length is given, it is assumed that the first byte of saltedHash
	 * is the salt length.
	 * 
	 * @param input
	 * @param saltedHash
	 * @return
	 */
	public static  boolean checkSaltedHashSHA512(String input, byte[] saltedHash) {
		try {
			return checkSaltedHash(input, saltedHash, ALGO_SHA_512);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
