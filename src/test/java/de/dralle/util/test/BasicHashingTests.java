package de.dralle.util.test;

import static org.junit.jupiter.api.Assertions.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

class BasicHashingTests {

	@Test
	void testSHA1Availabilty() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-1 not available");
		}	
		assertNotNull(sha);
	}
	
	@Test
	void testSHA256Availabilty() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-256 not available");
		}	
		assertNotNull(sha);
	}
	@Test
	void testSHA512Availabilty() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-512 not available");
		}	
		assertNotNull(sha);
	}
	@Test
	void testBasicHashingSHA1() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-1 not available");
		}	
		byte[] inputBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes = sha.digest(inputBytes);
		assertNotNull(hashBytes);
	}
	@Test
	void testHashNotSameAsInputSHA1() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-1 not available");
		}	
		byte[] inputBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes = sha.digest(inputBytes);
		boolean notsame=false;
		for (int i = 0; i < hashBytes.length; i++) {
			if(i>=inputBytes.length) {
				break;
			}
			if(inputBytes[i]!=hashBytes[i]) {
				notsame=true;
			}
		}
		assertTrue(notsame);
	}
	@Test
	void testHashNotSameWhenInputChangeSHA1() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-1 not available");
		}	
		byte[] inputBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes = sha.digest(inputBytes);
		byte[] inputBytes2 = new byte[] {0,1,2,3,4,5,6,7,8,8};
		byte[] hashBytes2 = sha.digest(inputBytes2);
		boolean notsame=false;
		for (int i = 0; i < hashBytes.length; i++) {
			if(i>=hashBytes2.length) {
				break;
			}
			if(hashBytes2[i]!=hashBytes[i]) {
				notsame=true;
			}
		}
		assertTrue(notsame);
	}
	@Test
	void testHashSameWhenInputSameSHA1() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-1 not available");
		}	
		byte[] inputBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes = sha.digest(inputBytes);
		byte[] inputBytes2 = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes2 = sha.digest(inputBytes2);
		assertArrayEquals(hashBytes, hashBytes2);
	}
	
	@Test
	void testBasicHashingSHA256() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-256 not available");
		}	
		byte[] inputBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes = sha.digest(inputBytes);
		assertNotNull(hashBytes);
	}
	@Test
	void testHashNotSameAsInputSHA256() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-256 not available");
		}	
		byte[] inputBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes = sha.digest(inputBytes);
		boolean notsame=false;
		for (int i = 0; i < hashBytes.length; i++) {
			if(i>=inputBytes.length) {
				break;
			}
			if(inputBytes[i]!=hashBytes[i]) {
				notsame=true;
			}
		}
		assertTrue(notsame);
	}
	@Test
	void testHashNotSameWhenInputChangeSHA256() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-256 not available");
		}	
		byte[] inputBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes = sha.digest(inputBytes);
		byte[] inputBytes2 = new byte[] {0,1,2,3,4,5,6,7,8,8};
		byte[] hashBytes2 = sha.digest(inputBytes2);
		boolean notsame=false;
		for (int i = 0; i < hashBytes.length; i++) {
			if(i>=hashBytes2.length) {
				break;
			}
			if(hashBytes2[i]!=hashBytes[i]) {
				notsame=true;
			}
		}
		assertTrue(notsame);
	}
	@Test
	void testHashSameWhenInputSameSHA256() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-256 not available");
		}	
		byte[] inputBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes = sha.digest(inputBytes);
		byte[] inputBytes2 = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes2 = sha.digest(inputBytes2);
		assertArrayEquals(hashBytes, hashBytes2);
	}
	
	@Test
	void testBasicHashingSHA512() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-512 not available");
		}	
		byte[] inputBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes = sha.digest(inputBytes);
		assertNotNull(hashBytes);
	}
	@Test
	void testHashNotSameAsInputSHA512() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-512 not available");
		}	
		byte[] inputBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes = sha.digest(inputBytes);
		boolean notsame=false;
		for (int i = 0; i < hashBytes.length; i++) {
			if(i>=inputBytes.length) {
				break;
			}
			if(inputBytes[i]!=hashBytes[i]) {
				notsame=true;
			}
		}
		assertTrue(notsame);
	}
	@Test
	void testHashNotSameWhenInputChangeSHA512() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-512 not available");
		}	
		byte[] inputBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes = sha.digest(inputBytes);
		byte[] inputBytes2 = new byte[] {0,1,2,3,4,5,6,7,8,8};
		byte[] hashBytes2 = sha.digest(inputBytes2);
		boolean notsame=false;
		for (int i = 0; i < hashBytes.length; i++) {
			if(i>=hashBytes2.length) {
				break;
			}
			if(hashBytes2[i]!=hashBytes[i]) {
				notsame=true;
			}
		}
		assertTrue(notsame);
	}
	@Test
	void testHashSameWhenInputSameSHA512() {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			fail("SHA-512 not available");
		}	
		byte[] inputBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes = sha.digest(inputBytes);
		byte[] inputBytes2 = new byte[] {0,1,2,3,4,5,6,7,8,9};
		byte[] hashBytes2 = sha.digest(inputBytes2);
		assertArrayEquals(hashBytes, hashBytes2);
	}
}
