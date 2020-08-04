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
	
	public byte[] getHash(byte[] input,String alghirthm) throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance(alghirthm);
		return sha.digest(input);
	}
	
	public byte[] getHash(byte[] input) {
		try {
			return getHash(input, "SHA-256");
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		}
		return null;
	}
}
