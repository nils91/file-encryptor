/**
 * 
 */
package de.dralle.passwordmanager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Nils Dralle
 *
 */
public class HashingUtil {
	@Deprecated
	public static final String ALGO_SHA_1 = "SHA-1";

	public static final String ALGO_SHA_256 = "SHA-256";
	public static final String ALGO_SHA_512 = "SHA-512";

	public byte[] getHash(String input, String algorithm) throws NoSuchAlgorithmException {
		return getHash(StringUtil.strToByteArr(input), algorithm);

	}

	public byte[] getHash(byte[] input, String algorithm) throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance(algorithm);
		return sha.digest(input);
	}

	public boolean checkHash(byte[] input, byte[] hash, String algorithm) throws NoSuchAlgorithmException {
		byte[] inHash = getHash(input, algorithm);
		return hash.equals(inHash);
	}

	public boolean checkHashSHA256(byte[] input, byte[] hash) {
		byte[] inHash = getHashSHA256(input);
		return hash.equals(inHash);
	}

	public boolean checkHashSHA512(byte[] input, byte[] hash) {
		byte[] inHash = getHashSHA512(input);
		return hash.equals(inHash);
	}

	public boolean checkHash(String input, byte[] hash, String algorithm) throws NoSuchAlgorithmException {
		byte[] inHash = getHash(input, algorithm);
		return hash.equals(inHash);
	}

	public boolean checkHashSHA256(String input, byte[] hash) {
		byte[] inHash = getHashSHA256(input);
		return hash.equals(inHash);
	}

	public boolean checkHashSHA512(String input, byte[] hash) {
		byte[] inHash = getHashSHA512(input);
		return hash.equals(inHash);
	}

	public byte[] getHashSHA256(byte[] input) {
		try {
			return getHash(input, ALGO_SHA_256);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] getHashSHA512(byte[] input) {
		try {
			return getHash(input, ALGO_SHA_512);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] getHashSHA256(String input) {
		try {
			return getHash(input, ALGO_SHA_256);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] getHashSHA512(String input) {
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
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public byte[] getSaltedHash(byte[] input, byte[] salt, String algorithm) throws NoSuchAlgorithmException {
		if (salt == null) {
			return getHash(input, algorithm);
		}
		SecureRandom.getInstanceStrong().nextBytes(salt);
		byte[] saltedInput = new byte[salt.length + input.length];
		for (int i = 0; i < saltedInput.length; i++) {
			if (i < salt.length) {
				saltedInput[i] = salt[i];
			} else {
				saltedInput[i] = input[i - salt.length];
			}
		}
		byte[] hash = getHash(saltedInput, algorithm);
		byte[] saltedHash = new byte[salt.length + hash.length];
		for (int i = 0; i < saltedHash.length; i++) {
			if (i < salt.length) {
				saltedHash[i] = salt[i];
			} else {
				saltedHash[i] = hash[i - salt.length];
			}
		}
		return saltedHash;
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
	public boolean checkSaltedHash(byte[] input, byte[] salt, byte[] hash, String algorithm)
			throws NoSuchAlgorithmException {
		if (salt == null) {
			return checkHash(input, hash, algorithm);
		}
		byte[] saltedInput = new byte[salt.length + input.length];
		for (int i = 0; i < saltedInput.length; i++) {
			if (i < salt.length) {
				saltedInput[i] = salt[i];
			} else {
				saltedInput[i] = input[i - salt.length];
			}
		}
		byte[] inHash = getHash(saltedInput, algorithm);
		return hash.equals(inHash);
	}

	public boolean checkSaltedHash(byte[] input, int saltLen, byte[] saltedHash, String algorithm)
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
}
