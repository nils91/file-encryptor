/**
 * 
 */
package de.dralle.fileenc;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Nils Dralle
 *
 */
class FileEncryptorCLIAppTest {

	private static final String TEMPORARY_FOLDER_NAME = "tmp";
	private static Path TEMPORARY_FOLDER = null;
	private static boolean folderExistedPreTest;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		TEMPORARY_FOLDER=Paths.get(TEMPORARY_FOLDER_NAME);
		folderExistedPreTest = Files.exists(TEMPORARY_FOLDER);
		if(!folderExistedPreTest) {
			TEMPORARY_FOLDER=Files.createDirectory(TEMPORARY_FOLDER);
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		if(TEMPORARY_FOLDER!=null&&!folderExistedPreTest) { //clean up folder
			Files.delete(TEMPORARY_FOLDER);
		}
	}

	@Test
	void testTemporaryTestFolder() {
		Path tmp=Paths.get(TEMPORARY_FOLDER_NAME);
		assertTrue(Files.exists(tmp));
	
	}
	@Test
	void testTemporaryTestFolderIsFolder(
	) {
		Path tmp=Paths.get(TEMPORARY_FOLDER_NAME);
		assertTrue(Files.exists(tmp));
		assertTrue(Files.isDirectory(tmp));
	
	}

}
