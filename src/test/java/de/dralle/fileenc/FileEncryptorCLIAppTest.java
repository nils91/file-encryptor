/**
 * 
 */
package de.dralle.fileenc;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.util.AESUtil;
import de.dralle.util.Base64Util;
import de.dralle.util.testing.ExceptionInsteadExitSecurityManager;
import de.dralle.util.testing.ExitException;

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

	private static String sampleAesKey; // base64 formatted aes key used for some of the tests

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		sampleAesKey = Base64Util.encodeBytes2Str(AESUtil.generateRandomKey().getEncoded());
	}

	private byte[] fileContents;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		System.setSecurityManager(new ExceptionInsteadExitSecurityManager());
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
				if (Files.exists(tmpFile)) {
					FileOutputStream tmpFileOut = new FileOutputStream(new File(tmpFile.toUri()));
					BufferedOutputStream tmpFileBOut = new BufferedOutputStream(tmpFileOut);
					fileContents = new byte[128];
					Random rnd = new Random();
					rnd.nextBytes(fileContents);
					tmpFileBOut.write(fileContents);
					tmpFileBOut.close();
				} else {
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
		cleanupOtherExpectedFiles();
		cleanupTmpFolderFile();
		checkIfAllFilesDeletedForCleanup();
		System.setSecurityManager(null);
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
			Files.deleteIfExists(tmpFile);
		}
		if (tmpFolder != null && !folderExistedPreTest) { // clean up folder
			File tmpFolderFileInstance = tmpFolder.toFile();
			File[] folderContents = tmpFolderFileInstance.listFiles();
			for (int i = 0; i < folderContents.length; i++) {
				folderContents[i].delete();
			}
			Files.deleteIfExists(tmpFolder);
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
		File f = new File(tmp.toUri());
		assertTrue(f.length() > 0);
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
		try {
			feApp.run(null);
		} catch (ExitException e) {
			assertTrue(e.getStatus() > 0);
		}
		String[] folderContentsAfter = tmpFolderFileInstance.list();
		assertArrayEquals(folderContentsBefore, folderContentsAfter);
	}

	@Test
	void testNoParamNoAction() {
		File tmpFolderFileInstance = new File(tmpFolder.toUri());
		String[] folderContentsBefore = tmpFolderFileInstance.list();
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(new String[0]);
		} catch (ExitException e) {
			assertTrue(e.getStatus() > 0);
		}
		String[] folderContentsAfter = tmpFolderFileInstance.list();
		assertArrayEquals(folderContentsBefore, folderContentsAfter);
	}

	@Test
	void testNoInputFileEncryptionNoAction() {
		String[] params = new String[] { "-e" };
		File tmpFolderFileInstance = new File(tmpFolder.toUri());
		String[] folderContentsBefore = tmpFolderFileInstance.list();
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			assertTrue(e.getStatus() > 0);
		}
		String[] folderContentsAfter = tmpFolderFileInstance.list();
		assertArrayEquals(folderContentsBefore, folderContentsAfter);
	}

	@Test
	void testNoInputFileDecryptionNoAction() {
		String[] params = new String[] { "-d" };
		File tmpFolderFileInstance = new File(tmpFolder.toUri());
		String[] folderContentsBefore = tmpFolderFileInstance.list();
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			assertTrue(e.getStatus() > 0);
		}
		String[] folderContentsAfter = tmpFolderFileInstance.list();
		assertArrayEquals(folderContentsBefore, folderContentsAfter);
	}

	@Test
	void testEncryptionEncryptedFileExists() {
		String[] params = new String[] { "-e", "-i", tmpFile.toAbsolutePath().toString() };
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			fail();
		}
		encFile = Paths.get(tmpFile.toAbsolutePath() + ".enc");
		assertTrue(Files.exists(encFile));
	}

	@Test
	void testEncryptionEncryptedFileExistsOutputPathSpecified() throws IOException {
		String[] params = new String[] { "-e", "-i", tmpFile.toAbsolutePath().toString(), "-o",
				tmpFile.toAbsolutePath().toString() + ".encrypted" };
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			assertEquals(0, e.getStatus());
		}
		encFile = Paths.get(tmpFile.toAbsolutePath() + ".encrypted");
		assertTrue(Files.exists(encFile));
		Files.deleteIfExists(encFile);

	}

	@Test
	void testDecryptionDecryptedFileExists() {
		// Encrypt
		String[] params = new String[] { "-e", "-k",sampleAesKey, "-i", tmpFile.toAbsolutePath().toString() };
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			// assertEquals(0, e.getStatus());
		}
		encFile = Paths.get(tmpFile.toAbsolutePath() + ".enc");
		// assertTrue(Files.exists(encFile));
		// Decrypt
		params = new String[] { "-d", "-k",sampleAesKey, "-i", encFile.toAbsolutePath().toString() };
		feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			assertEquals(0, e.getStatus());
		}
		decFile = Paths.get(encFile.toAbsolutePath() + ".dec");
		assertTrue(Files.exists(decFile));
	}

	@Test
	void testDecryptionDecryptedFileExistsOutputPathSpecified() throws IOException {
		// Encrypt
		String[] params = new String[] { "-e", "-k",sampleAesKey, "-i", tmpFile.toAbsolutePath().toString(), "-o",
				tmpFile.toAbsolutePath().toString() + ".encrypted" };
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			// assertEquals(0, e.getStatus());
		}
		encFile = Paths.get(tmpFile.toAbsolutePath() + ".encrypted");
		// assertTrue(Files.exists(encFile));

		// Decrypt
		params = new String[] { "-d", "-k",sampleAesKey, "-i", encFile.toAbsolutePath().toString(), "-o",
				encFile.toAbsolutePath().toString() + ".decrypted" };
		feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			assertEquals(0, e.getStatus(), "Terminated unexpectedly");
		}
		decFile = Paths.get(encFile.toAbsolutePath() + ".decrypted");
		assertTrue(Files.exists(decFile));
		Files.deleteIfExists(encFile);
		Files.deleteIfExists(decFile);

	}/**
	 * @param bin
	 * @return
	 * @throws IOException 
	 */
	private byte[] readAllBytes(BufferedInputStream bin) throws IOException {
		List<Byte> allBytes = new ArrayList<Byte>();
		byte[] completeFile = null;
			while (bin.available() > 0) {
				int nextByte = bin.read();
				allBytes.add(Byte.valueOf((byte) nextByte));
			}
	
		completeFile = new byte[allBytes.size()];
		for (int i = 0; i < completeFile.length; i++) {
			completeFile[i] = allBytes.get(i);
		}
		return completeFile;
	}
	/**
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	private byte[] readAllBytesFromFile(File file) throws IOException {
		if(file==null) {
			return null;
		}
		InputStream in = null;
			in = new FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(in);
		byte[] completeFile = readAllBytes(bin);
			bin.close();
		
		return completeFile;
	}
	@Test
	void testDecryptionDecryptedFileMatchesInputFileKeyFromeConsole() throws IOException {
		// Encrypt
		String[] params = new String[] { "-e", "-k",sampleAesKey, "-i", tmpFile.toAbsolutePath().toString(), "-o",
				tmpFile.toAbsolutePath().toString() + ".encrypted" };
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			// assertEquals(0, e.getStatus());
		}
		encFile = Paths.get(tmpFile.toAbsolutePath() + ".encrypted");
		// assertTrue(Files.exists(encFile));

		// Decrypt
		params = new String[] { "-d", "-k",sampleAesKey, "-i", encFile.toAbsolutePath().toString(), "-o",
				encFile.toAbsolutePath().toString() + ".decrypted" };
		feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			//assertEquals(0, e.getStatus(), "Terminated unexpectedly");
		}
		decFile = Paths.get(encFile.toAbsolutePath() + ".decrypted");
		//assertTrue(Files.exists(decFile));
		byte[] decryptedFileContents=readAllBytesFromFile(decFile.toFile());
		assertArrayEquals(fileContents, decryptedFileContents);
		
		Files.deleteIfExists(encFile);
		Files.deleteIfExists(decFile);

	}
	@Test
	void testDecryptionDecryptedFileMatchesInputFileKeyFromFileDefaultFilename() throws IOException {
		// Encrypt
		String[] params = new String[] { "-e", "-w","yes", "-i", tmpFile.toAbsolutePath().toString(), "-o",
				tmpFile.toAbsolutePath().toString() + ".encrypted" };
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			// assertEquals(0, e.getStatus());
		}
		
		encFile = Paths.get(tmpFile.toAbsolutePath() + ".encrypted");
		keyFile=Paths.get(encFile.toAbsolutePath() + ".key");
		// assertTrue(Files.exists(encFile));

		// Decrypt
		params = new String[] { "-d", "-k",keyFile.toAbsolutePath().toString(), "-i", encFile.toAbsolutePath().toString(), "-o",
				encFile.toAbsolutePath().toString() + ".decrypted" };
		feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			//assertEquals(0, e.getStatus(), "Terminated unexpectedly");
		}
		decFile = Paths.get(encFile.toAbsolutePath() + ".decrypted");
		//assertTrue(Files.exists(decFile));
		byte[] decryptedFileContents=readAllBytesFromFile(decFile.toFile());
		assertArrayEquals(fileContents, decryptedFileContents);
		
		Files.deleteIfExists(encFile);
		Files.deleteIfExists(decFile);
		Files.deleteIfExists(keyFile);

	}@Test
	void testDecryptionDecryptedFileMatchesInputFileKeyFromFileCustomFilename() throws IOException {
		// Encrypt
		keyFile=Paths.get(tmpFile.toAbsolutePath() + ".keyfile");
		String[] params = new String[] { "-e", "-w",keyFile.toAbsolutePath().toString(), "-i", tmpFile.toAbsolutePath().toString(), "-o",
				tmpFile.toAbsolutePath().toString() + ".encrypted" };
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			// assertEquals(0, e.getStatus());
		}
		
		encFile = Paths.get(tmpFile.toAbsolutePath() + ".encrypted");
		
		// assertTrue(Files.exists(encFile));

		// Decrypt
		params = new String[] { "-d", "-k",keyFile.toAbsolutePath().toString(), "-i", encFile.toAbsolutePath().toString(), "-o",
				encFile.toAbsolutePath().toString() + ".decrypted" };
		feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			//assertEquals(0, e.getStatus(), "Terminated unexpectedly");
		}
		decFile = Paths.get(encFile.toAbsolutePath() + ".decrypted");
		//assertTrue(Files.exists(decFile));
		byte[] decryptedFileContents=readAllBytesFromFile(decFile.toFile());
		assertArrayEquals(fileContents, decryptedFileContents);
		
		Files.deleteIfExists(encFile);
		Files.deleteIfExists(decFile);
		Files.deleteIfExists(keyFile);

	}@Test
	void testKeyfileWrittenDefaultFilename() throws IOException {
		// Encrypt
		String[] params = new String[] { "-e", "-w","yes", "-i", tmpFile.toAbsolutePath().toString(), "-o",
				tmpFile.toAbsolutePath().toString() + ".encrypted" };
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			// assertEquals(0, e.getStatus());
		}
		
		encFile = Paths.get(tmpFile.toAbsolutePath() + ".encrypted");
		keyFile=Paths.get(encFile.toAbsolutePath() + ".key");
		assertTrue(Files.exists(keyFile));

	
		
		Files.deleteIfExists(encFile);
		Files.deleteIfExists(keyFile);

	}@Test
	void testKeyfileWrittenCustomFilename() throws IOException {
		// Encrypt
		keyFile=Paths.get(tmpFile.toAbsolutePath() + ".keyfile");
		String[] params = new String[] { "-e", "-w",keyFile.toAbsolutePath().toString(), "-i", tmpFile.toAbsolutePath().toString(), "-o",
				tmpFile.toAbsolutePath().toString() + ".encrypted" };
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			// assertEquals(0, e.getStatus());
		}
		
		encFile = Paths.get(tmpFile.toAbsolutePath() + ".encrypted");
		
		 assertTrue(Files.exists(keyFile));

		
		Files.deleteIfExists(encFile);
		Files.deleteIfExists(keyFile);

	}	@Test
	void testEncryptionInputFileNotDeleted() {
		String[] params = new String[] { "-e", "-i", tmpFile.toAbsolutePath().toString() };
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			//fail();
		}
		assertTrue(Files.exists(tmpFile));
	}	@Test
	void testEncryptionInputFileDeleted() {
		String[] params = new String[] { "-e", "-i", tmpFile.toAbsolutePath().toString() ,"-r"};
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			//fail();
		}
		assertFalse(Files.exists(tmpFile));
	}@Test
	void testDecryptionInputFileDeleted() {
		// Encrypt
		String[] params = new String[] { "-e", "-k",sampleAesKey, "-i", tmpFile.toAbsolutePath().toString() };
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			// assertEquals(0, e.getStatus());
		}
		encFile = Paths.get(tmpFile.toAbsolutePath() + ".enc");
		// assertTrue(Files.exists(encFile));
		// Decrypt
		params = new String[] { "-d", "-k",sampleAesKey, "-i", encFile.toAbsolutePath().toString() ,"-r"};
		feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			//assertEquals(0, e.getStatus());
		}
		assertFalse(Files.exists(encFile));
	}@Test
	void testDecryptionInputFileNotDeleted() {
		// Encrypt
		String[] params = new String[] { "-e", "-k",sampleAesKey, "-i", tmpFile.toAbsolutePath().toString() };
		FileEncryptorCLIApp feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			// assertEquals(0, e.getStatus());
		}
		encFile = Paths.get(tmpFile.toAbsolutePath() + ".enc");
		// assertTrue(Files.exists(encFile));
		// Decrypt
		params = new String[] { "-d", "-k",sampleAesKey, "-i", encFile.toAbsolutePath().toString() };
		feApp = new FileEncryptorCLIApp();
		try {
			feApp.run(params);
		} catch (ExitException e) {
			//assertEquals(0, e.getStatus());
		}
		assertTrue(Files.exists(encFile));
	}
}
