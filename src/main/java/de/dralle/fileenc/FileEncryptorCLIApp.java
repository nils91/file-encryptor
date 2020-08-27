/**
 * 
 */
package de.dralle.fileenc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.dralle.util.AESUtil;
import de.dralle.util.Base64Util;

/**
 * @author Nils Dralle
 *
 */
public class FileEncryptorCLIApp {
	public final static String VERSION = "0.0.2";
	private boolean verbose;
	private PrintStream stdout;

	public FileEncryptorCLIApp() {
		stdout = System.out;
	}

	public void run(String[] args) {
		Options options = prepareOptions();
		CommandLine cmd = parseCLI(args, options);
		if (cmd.hasOption("t")) {
			verbose = true;
		}
		if (verbose) {
			printFullCommandString(args);
		}
		if (cmd.hasOption("h")) {
			showHelp(options);
		} else if (cmd.hasOption("e")) {
			encrypt(cmd);
		} else if (cmd.hasOption("d")) {
			decrypt(cmd);
		} else if (cmd.hasOption("v")) {
			showVersion();
		} else {
			stdout.println("Use option '-h' to show usage help");
		}

	}

	/**
	 * @param args
	 */
	private void printFullCommandString(String[] args) {
		try {
			stdout.print(FileEncryptorCLIApp.class.getProtectionDomain().getCodeSource().getLocation().getFile() + " ");
		} catch (Exception e) {
		}
		for (int i = 0; i < args.length; i++) {
			stdout.print(args[i] + " ");
		}
		stdout.println();
	}

	private void showVersion() {
		stdout.println(String.format("Version %s running on Java %s", VERSION, System.getProperty("java.version")));

	}

	private void decrypt(CommandLine cmd) {
		if (verbose) {
			stdout.println("Decryption mode");
		}
		File inputFile = null;
		File outputFile = null;
		if (cmd.hasOption("i")) {
			inputFile = getInputFileFromCLIArguments(cmd);
		} else {
			stdout.println("No input file specified");
			System.exit(1);
		}
		if (cmd.hasOption("o")) {
			outputFile = getOutputFilePathFromCLIArguments(cmd);
		} else {
			if (verbose) {
				stdout.println("No output file specified");
			}
			outputFile = generateOutputFileNameFromInputFile(inputFile, ".dec");
		}
		// Read key
		SecretKey key = null;
		if (cmd.hasOption("k")) {
			key = getKeyFromCliArguments(cmd);
		} else {
			stdout.println("No key provided");
			System.exit(1);
		}
		// Read input file bytes
		byte[] completeFile = readAllBytesFromInputFile(inputFile);
		// Decrypt
		byte[] completeFileDecrypted = AESUtil.decryptAssumingOnlyIVPrefix(completeFile, key);
		if (completeFileDecrypted == null) {
			System.out.println("Decryption failed");
			System.exit(1);
		}
		// Write
		writeOutputFile(outputFile, completeFileDecrypted);
		if (verbose) {
			this.stdout.println(completeFileDecrypted.length + " Bytes written");
		}
		if (cmd.hasOption("w")) {
			this.stdout.println(Base64Util.encodeBytes2Str(key.getEncoded()));
		}
		if (cmd.hasOption("r")) {
			boolean deleted = inputFile.delete();
			if (verbose) {
				if (deleted) {
					this.stdout.println("Input file deletion successfull");
				} else {
					this.stdout.println("Input file deletion not successfull");
				}
			}
		}
	}

	/**
	 * @param outputFile
	 * @param content
	 */
	private void writeOutputFile(File outputFile, byte[] content) {
		if (outputFile == null) {
			throw new NullPointerException();
		}
		OutputStream out = null;
		try {
			out = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
			this.stdout.println("Error while opening output file");
			if (verbose) {
				e.printStackTrace();
			}
			System.exit(1);
		}
		BufferedOutputStream bout = new BufferedOutputStream(out);
		try {
			bout.write(content);
		} catch (IOException e) {
			this.stdout.println("Error while writing output file");
			if (verbose) {
				e.printStackTrace();
			}
			System.exit(1);
		}
		try {
			bout.close();
		} catch (IOException e) {
			this.stdout.println("Error while closing output file");
			if (verbose) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param file
	 * @return
	 */
	private byte[] readAllBytesFromFile(File file) {
		if (file == null) {
			return null;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			stdout.println("Error while opening file");
			if (verbose) {
				e.printStackTrace();
			}
			System.exit(1);
		}
		BufferedInputStream bin = new BufferedInputStream(in);
		byte[] completeFile = readAllBytes(bin);
		if (verbose) {
			stdout.println(completeFile.length + " Bytes read");
		}
		try {
			bin.close();
		} catch (IOException e) {
			stdout.println("Error while closing file");
			if (verbose) {
				e.printStackTrace();
			}
		}
		return completeFile;
	}

	/**
	 * Deprecated. Use readAllBytesFromFile instead.
	 * 
	 * @param inputFile
	 * @return
	 */
	@Deprecated
	private byte[] readAllBytesFromInputFile(File inputFile) {
		return readAllBytesFromFile(inputFile);
	}

	/**
	 * Deprecated. Use readAllBytesFromFile instead.
	 * 
	 * @param file
	 * @return
	 */
	@Deprecated
	private byte[] readKeyFile(File file) {
		return readAllBytesFromFile(file);
	}

	/**
	 * @param bin
	 * @return
	 */
	private byte[] readAllBytes(BufferedInputStream bin) {
		List<Byte> allBytes = new ArrayList<Byte>();
		byte[] completeFile = null;
		try {
			while (bin.available() > 0) {
				int nextByte = bin.read();
				allBytes.add(Byte.valueOf((byte) nextByte));
			}
		} catch (IOException e) {
			stdout.println("Error while reading bytes from file");
			if (verbose) {
				e.printStackTrace();
			}
			System.exit(1);
		}
		completeFile = new byte[allBytes.size()];
		for (int i = 0; i < completeFile.length; i++) {
			completeFile[i] = allBytes.get(i);
		}
		return completeFile;
	}

	/**
	 * @param cmd
	 * @return
	 */
	private SecretKey getKeyFromCliArguments(CommandLine cmd) {
		SecretKey key;
		byte[] keyBytes = null;
		if (verbose) {
			stdout.println("Key provided with option -k");
		}
		String keyValue = cmd.getOptionValue("k");
		File keyFileTrial = new File(keyValue);
		if (keyFileTrial.exists()) {
			if (verbose) {
				stdout.println("Provided key is a file");
			}
			// Read file bytes
			keyBytes = readKeyFile(keyFileTrial);
		} else {
			keyBytes = Base64Util.decodeString(keyValue);
		}
		key = AESUtil.generateKeyFromByteArray(keyBytes);
		return key;
	}

	/**
	 * @param inputFile
	 * @return
	 */
	private File generateOutputFileNameFromInputFile(File inputFile, String fileExt) {
		if (inputFile == null) {
			return null;
		}
		String outputFilePath;
		File outputFile;
		outputFilePath = inputFile.getAbsolutePath() + fileExt;
		if (verbose) {
			stdout.println("Output file set to: " + outputFilePath);
		}
		File f = new File(outputFilePath);
		outputFile = f;
		return outputFile;
	}

	/**
	 * @param cmd
	 * @return
	 */
	private File getOutputFilePathFromCLIArguments(CommandLine cmd) {
		String outputFilePath;
		File outputFile;
		outputFilePath = cmd.getOptionValue("o");
		if (verbose) {
			stdout.println("Specified output file: " + outputFilePath);
		}
		File f = new File(outputFilePath);

		outputFile = f;
		return outputFile;
	}

	/**
	 * @param cmd
	 * @return
	 */
	private File getInputFileFromCLIArguments(CommandLine cmd) {
		File inputFile;
		String inputFilePath = cmd.getOptionValue("i");
		if (verbose) {
			stdout.println("Specified input file: " + inputFilePath);
		}
		File f = new File(inputFilePath);
		if (!f.exists()) {
			stdout.println("Input file does not exist");
			System.exit(1);
		}
		if (!f.canRead()) {
			stdout.println("Input file can not be read");
			System.exit(1);
		}
		inputFile = f;
		return inputFile;
	}

	private void encrypt(CommandLine cmd) {
		if (verbose) {
			stdout.println("Encryption mode");
		}
		File inputFile = null;
		File outputFile = null;
		if (cmd.hasOption("i")) {
			inputFile = getInputFileFromCLIArguments(cmd);
		} else {
			stdout.println("No input file specified");
			System.exit(1);
		}
		if (cmd.hasOption("o")) {
			outputFile = getOutputFilePathFromCLIArguments(cmd);
		} else {
			outputFile = generateOutputFileNameFromInputFile(inputFile, ".enc");
		}
		byte[] completeFile = readAllBytesFromInputFile(inputFile);
		// Encrypt
		SecretKey key = null;
		if (cmd.hasOption("k")) {
			key = getKeyFromCliArguments(cmd);
		} else {

			key = AESUtil.generateRandomKey();
			if (verbose) {
				stdout.println("Using random key: " + Base64Util.encodeBytes2Str(key.getEncoded()));
			}
		}
		byte[] iv = new byte[16];
		byte[] completeFileEncrypted = AESUtil.encrypt(completeFile, key, iv);
		byte[] completeFileEncryptedWithIVPrefix = AESUtil.addSaltAndIV(completeFileEncrypted, null, iv);
		writeOutputFile(outputFile, completeFileEncryptedWithIVPrefix);
		if (verbose) {
			this.stdout.println(completeFileEncryptedWithIVPrefix.length + " Bytes written");
		}
		if (cmd.hasOption("w")) {
			String writeKeyFile = cmd.getOptionValue("w");
			File tgtKeyFile = null;
			if (writeKeyFile == null) {
				if (verbose) {
					this.stdout.println("Parameter '-w' has no argument. Writing key to console.");
				}
				this.stdout.println(Base64Util.encodeBytes2Str(key.getEncoded()));
			} else if (writeKeyFile.equalsIgnoreCase("yes")) {
				tgtKeyFile = new File(outputFile.getAbsoluteFile() + ".key");
				if (verbose) {
					this.stdout.println(String.format(
							"Parameter '-w' has argument '%s'. Writing key to filename generated from output filename.",
							writeKeyFile));
				}
			} else {
				tgtKeyFile = new File(writeKeyFile);
				if (verbose) {
					this.stdout.println(String.format("Parameter '-w' has argument '%s'. Trying to write key there.",
							writeKeyFile));
				}
			}
			if (tgtKeyFile != null) {
				if (verbose) {
					this.stdout.println(String.format("Writing key to %s.", tgtKeyFile.getAbsolutePath()));
				}
				// Write key
				OutputStream keyout = null;
				try {
					keyout = new FileOutputStream(tgtKeyFile);
				} catch (FileNotFoundException e) {
					this.stdout.println("Error while opening key file");
					if (verbose) {
						e.printStackTrace();
					}
					System.exit(1);
				}
				BufferedOutputStream keybout = new BufferedOutputStream(keyout);
				try {
					keybout.write(key.getEncoded());
				} catch (IOException e) {
					this.stdout.println("Error while writing key file");
					if (verbose) {
						e.printStackTrace();
					}
					System.exit(1);
				}
				try {
					keybout.close();
				} catch (IOException e) {
					this.stdout.println("Error while closing key file");
					if (verbose) {
						e.printStackTrace();
					}
				}

				if (verbose) {
					this.stdout.println("Key written to " + tgtKeyFile.getAbsolutePath());
				}
			}

		}
		if (cmd.hasOption("r")) {
			boolean deleted = inputFile.delete();
			if (verbose) {
				if (deleted) {
					this.stdout.println("Input file deletion successfull");
				} else {
					this.stdout.println("Input file deletion not successfull");
				}
			}
		}

	}

	private CommandLine parseCLI(String[] args, Options options) {
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			if (verbose) {
				e.printStackTrace();
			}
			stdout.println("Arguments could not be parser. Use '-h' or '--help' to show correct usage.");
			System.exit(1);
		}
		return cmd;
	}

	/**
	 * @param options
	 */
	private void showHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("file-encryptor", options);
	}

	private Options prepareOptions() {
		Options options = new Options();
		options.addOption("e", "encrypt", false, "This flag sets the application to encryption mode");
		options.addOption("d", "decrypt", false, "This flag sets the application to decryption mode");
		options.addOption("i", "in", true, "Input file");
		options.addOption("o", "out", true,
				"Output file name. If this parameter is not given, the output filename in encryption mode is just the input filename + '.enc' or '.dec' in decryption mode.");
		options.addOption("r", "remove", false, "Remove the input file after the operation is complete");
		options.addOption("t", "verbose", false, "Switch on verbode mode");
		// options.addOption("c", "verify", false, "Verify the operation");
		// options.addOption("p", "password", true, "Encrypt/Decrypt using a password");
		Option optReadKey = new Option("k", "key", true,
				"Encrypt/Decrypt using a key. Must be encoded in Base64 when read from console.");
		options.addOption(optReadKey);

		Option optWritekey = new Option("w", "writekey", true,
				"Write the key to the console (or a file) once operation is complete. The argument is optional. If the argument is 'yes', the key will be written to  a filename generated from the output file name. Alternativly a filepath can be provided.");
		optWritekey.setOptionalArg(true);
		options.addOption(optWritekey);

		options.addOption("h", "help", false, "Show usage instructions");
		options.addOption("v", "version", false, "Show version information");
		// options.addOption("g", "gui", false, "Show GUI");
		// options.addOption("b", "blocks", true, "Encrypt the file in block instead of
		// as a whole");
		return options;
	}

}
