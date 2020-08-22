/**
 * 
 */
package de.dralle.fileenc;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Random;

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
	private static Path tmpFolder = null;
	private static final String TEMPORARY_FILE_NAME = "file";
	private static Path tmpFile = null;

	private static boolean folderExistedPreTest;
	private static boolean fileExistedPreTest;
	private static Path keyFile;
	private static Path decFile;
	private static Path encFile;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		tmpFolder = Paths.get(TEMPORARY_FOLDER_NAME);
		folderExistedPreTest = Files.exists(tmpFolder);
		if (!folderExistedPreTest) {
			tmpFolder = Files.createDirectory(tmpFolder);
		}
		if (Files.exists(tmpFolder)) {
			tmpFile = Paths.get(TEMPORARY_FOLDER_NAME, TEMPORARY_FILE_NAME);
			fileExistedPreTest = Files.exists(tmpFile);
			if (!fileExistedPreTest) {
				tmpFile = Files.createFile(tmpFile);
				if(Files.exists(tmpFile)) {
					FileOutputStream tmpFileOut = new FileOutputStream(new File(tmpFile.toUri()));
					BufferedOutputStream tmpFileBOut = new BufferedOutputStream(tmpFileOut);
					byte[] fileContents = new byte[128];
					Random rnd = new Random();
					rnd.nextBytes(fileContents);
					tmpFileBOut.write(fileContents);
					tmpFileBOut.close();
				}else {
					throw new Exception("File not created");
				}
			}
		} else {
			throw new Exception("Folder not created");
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		cleanupTmpFolderFile();
		cleanupOtherExpectedFiles();
		checkIfAllFilesDeletedForCleanup();
	}

	private void cleanupOtherExpectedFiles() throws IOException {
		keyFile = Paths.get(TEMPORARY_FOLDER_NAME, TEMPORARY_FILE_NAME + ".key");
		encFile = Paths.get(TEMPORARY_FOLDER_NAME, TEMPORARY_FILE_NAME + ".enc");
		decFile = Paths.get(TEMPORARY_FOLDER_NAME, TEMPORARY_FILE_NAME + ".enc.dec");
		Files.deleteIfExists(keyFile);
		Files.deleteIfExists(encFile);
		Files.deleteIfExists(decFile);

	}

	/**
	 * @throws IOException
	 */
	private void cleanupTmpFolderFile() throws IOException {
		if (tmpFile != null && !fileExistedPreTest) { // clean up file
			Files.delete(tmpFile);
		}
		if (tmpFolder != null && !folderExistedPreTest) { // clean up folder
			Files.delete(tmpFolder);
		}
	}

	private void checkIfAllFilesDeletedForCleanup() throws Exception {
		tmpFolder = Paths.get(TEMPORARY_FOLDER_NAME);
		tmpFile = Paths.get(TEMPORARY_FOLDER_NAME, TEMPORARY_FILE_NAME);
		keyFile = Paths.get(TEMPORARY_FOLDER_NAME, TEMPORARY_FILE_NAME + ".key");
		encFile = Paths.get(TEMPORARY_FOLDER_NAME, TEMPORARY_FILE_NAME + ".enc");
		decFile = Paths.get(TEMPORARY_FOLDER_NAME, TEMPORARY_FILE_NAME + ".enc.dec");
		if (!folderExistedPreTest) {
			if (Files.exists(tmpFolder)) {
				throw new Exception("Cleanup not complete(" + tmpFolder + ")");
			}
			if (Files.exists(tmpFile)) {
				throw new Exception("Cleanup not complete(" + tmpFile + ")");
			}
			if (Files.exists(keyFile)) {
				throw new Exception("Cleanup not complete(" + keyFile + ")");
			}
			if (Files.exists(encFile)) {
				throw new Exception("Cleanup not complete(" + encFile + ")");
			}
			if (Files.exists(decFile)) {
				throw new Exception("Cleanup not complete(" + decFile + ")");
			}
		}
	}

	@Test
	void testTemporaryTestFolder() {
		Path tmp = Paths.get(TEMPORARY_FOLDER_NAME);
		assertTrue(Files.exists(tmp));

	}

	@Test
	void testTemporaryTestFolderIsFolder() {
		Path tmp = Paths.get(TEMPORARY_FOLDER_NAME);
		assertTrue(Files.exists(tmp));
		assertTrue(Files.isDirectory(tmp));

	}

	@Test
	void testTemporaryTestFile() {
		Path tmp = Paths.get(TEMPORARY_FOLDER_NAME, TEMPORARY_FILE_NAME);
		assertTrue(Files.exists(tmp));
	}
	
	@Test
	void testTemporaryTestFileSize() {
		Path tmp = Paths.get(TEMPORARY_FOLDER_NAME, TEMPORARY_FILE_NAME);
		File f=new File(tmp.toUri());
		assertTrue(f.length()>0);
	}
	@Test
	void testTemporaryTestFileNotFolder() {
		Path tmp = Paths.get(TEMPORARY_FOLDER_NAME, TEMPORARY_FILE_NAME);
		assertTrue(Files.exists(tmp));
		assertFalse(Files.isDirectory(tmp));
	}
	
	@Test
	void testNoParamNoActionNull() {
		File tmpFolderFileInstance = new File(tmpFolder.toUri());
		String[] folderContentsBefore = tmpFolderFileInstance.list();
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		feApp.run(null);
		String[] folderContentsAfter = tmpFolderFileInstance.list();
		assertArrayEquals(folderContentsBefore, folderContentsAfter);
	}
	@Test
	void testNoParamNoAction() {
		File tmpFolderFileInstance = new File(tmpFolder.toUri());
		String[] folderContentsBefore = tmpFolderFileInstance.list();
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		feApp.run(new String[0]);
		String[] folderContentsAfter = tmpFolderFileInstance.list();
		assertArrayEquals(folderContentsBefore, folderContentsAfter);
	}
	@Test
	void testNoInputFileEncryptionNoAction() {
		String[] params=new String[] {"-e"};
		File tmpFolderFileInstance = new File(tmpFolder.toUri());
		String[] folderContentsBefore = tmpFolderFileInstance.list();
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		feApp.run(params);
		String[] folderContentsAfter = tmpFolderFileInstance.list();
		assertArrayEquals(folderContentsBefore, folderContentsAfter);
	}
	@Test
	void testNoInputFileDecryptionNoAction() {
		String[] params=new String[] {"-d"};
		File tmpFolderFileInstance = new File(tmpFolder.toUri());
		String[] folderContentsBefore = tmpFolderFileInstance.list();
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		feApp.run(params);
		String[] folderContentsAfter = tmpFolderFileInstance.list();
		assertArrayEquals(folderContentsBefore, folderContentsAfter);
	}
}
