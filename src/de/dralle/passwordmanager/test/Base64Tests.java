/**
 * 
 */
package de.dralle.passwordmanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.junit.jupiter.api.Test;

/**
 * @author Nils Dralle
 *
 */
class Base64Tests {

	@Test
	void test(){
	Encoder encoder = Base64.getEncoder();
	Decoder decoder = Base64.getDecoder();
byte[] inputBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
byte[] encodedBytes = encoder.encode(inputBytes);
byte[] decodedBytes = decoder.decode(encodedBytes);assertArrayEquals(inputBytes, decodedBytes);
	}

}
