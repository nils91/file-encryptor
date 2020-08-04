/**
 * 
 */
package de.dralle.passwordmanager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
	}public boolean checkHashSHA512(byte[] input, byte[] hash) {
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
}
