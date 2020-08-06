/**
 * 
 */
package de.dralle.passwordmanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

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
	void testAESAvailability(){
		KeyGenerator keygen=null;
		try {
			keygen=KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			fail();
		}
		assertNotNull(keygen);
	}

}
