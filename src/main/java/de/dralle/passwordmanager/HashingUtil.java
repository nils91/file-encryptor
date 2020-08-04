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
	public static final String ALGO_SHA_1="SHA-1";
	
	public static final String ALGO_SHA_256="SHA-256";
	public static final String ALGO_SHA_512="SHA-512";
	
	public byte[] getHash(byte[] input,String algorithm) throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance(algorithm);
		return sha.digest(input);
	}
	
	public byte[] getHashSHA256(byte[] input) {
		try {
			return getHash(input, ALGO_SHA_256);
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		}
		return null;
	}
}
