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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.dralle.util.AESUtil;
import de.dralle.util.Base64Util;

/**
 * @author Nils Dralle
 *
 */
public class FileEncryptorCLIApp {
	private boolean verbose;

	public void run(String[] args) {
		Options options = prepareOptions();
		CommandLine cmd = parseCLI(args, options);
		if (cmd.hasOption("t")) {
			verbose = true;
		}
		if (verbose) {
			try {
				System.out.print(
						FileEncryptorCLIApp.class.getProtectionDomain().getCodeSource().getLocation().getFile() + " ");
			} catch (Exception e) {
			}
			for (int i = 0; i < args.length; i++) {
				System.out.print(args[i] + " ");
			}
			System.out.println();
		}
		if (cmd.hasOption("h")) {
			showHelp(options);
		} else if (cmd.hasOption("e")) {
			encrypt(cmd);
		}
	}

	private void encrypt(CommandLine cmd) {
		if(verbose) {
			System.out.println("Encryption mode");
		}
		String inputFilePath=null;
		File inputFile=null;
		String outputFilePath=null;
		File outputFile=null;
		if(cmd.hasOption("i")) {
			inputFilePath=cmd.getOptionValue("i");
			if(verbose) {
				System.out.println("Specified input file: "+inputFilePath);
			}
			File f = new File(inputFilePath);
			if(!f.exists()) {
					System.out.println("Input file does not exist");
				System.exit(1);
			}
			if(!f.canRead()) {
					System.out.println("Input file can not be read");
				System.exit(1);
			}
			inputFile=f;
		}else {
			System.out.println("No input file specified");
		}
		if(cmd.hasOption("o")) {
			outputFilePath=cmd.getOptionValue("o");
			if(verbose) {
				System.out.println("Specified output file: "+outputFilePath);
			}
			File f = new File(outputFilePath);
			
			outputFile=f;
		}else {
			if(verbose) {
				System.out.println("No output file specified");
			}
			outputFilePath=inputFile.getAbsolutePath()+".enc";
			if(verbose) {
				System.out.println("Output file set to: "+outputFilePath);
			}
			File f = new File(outputFilePath);
			outputFile=f;
		}
		//Read file bytes
		InputStream in = null;
		try {
			in = new FileInputStream(inputFile);
		} catch (FileNotFoundException e) {
			System.out.println("Error while opening input file");
			if(verbose) {
				e.printStackTrace();
			}
			System.exit(1);
		}
		BufferedInputStream bin=new BufferedInputStream(in);
		List<Byte> allBytes=new ArrayList<Byte>();
		byte[] completeFile = null;
		try {
			while(bin.available()>0) {
				int nextByte=bin.read();
				allBytes.add(Byte.valueOf((byte) nextByte));
			}
		} catch (IOException e) {
			System.out.println("Error while reading input file");
			if(verbose) {
				e.printStackTrace();
			}
			System.exit(1);
		}
		completeFile=new byte[allBytes.size()];
		for (int i = 0; i < completeFile.length; i++) {
			completeFile[i]=allBytes.get(i);
		}
		if(verbose) {
			System.out.println(completeFile.length+" Bytes read");
		}
		try {
			bin.close();
		} catch (IOException e) {
			System.out.println("Error while closing input file");
			if(verbose) {
				e.printStackTrace();
			}
		}
		//Encrypt
		SecretKey key = AESUtil.generateRandomKey();
		byte[] iv=new byte[16];
		byte[] completeFileEncrypted=AESUtil.encrypt(completeFile, key, iv);
		byte[] completeFileEncryptedWithIVPrefix = AESUtil.addSaltAndIV(completeFileEncrypted, null, iv);
		//Write
		OutputStream out = null;
		try {
			out = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
			System.out.println("Error while opening output file");
			if(verbose) {
				e.printStackTrace();
			}
			System.exit(1);
		}
		BufferedOutputStream bout=new BufferedOutputStream(out);
		try {
			bout.write(completeFileEncryptedWithIVPrefix);
		} catch (IOException e) {
			System.out.println("Error while writing output file");
			if(verbose) {
				e.printStackTrace();
			}
			System.exit(1);
		}
		try {
			bout.close();
		} catch (IOException e) {
			System.out.println("Error while closing output file");
			if(verbose) {
				e.printStackTrace();
			}
		}
		if(verbose) {
			System.out.println(completeFileEncryptedWithIVPrefix.length+" Bytes written");
		}
		if(cmd.hasOption("w")) {
			System.out.println(Base64Util.encodeBytes2Str(key.getEncoded()));
		}

	}

	private CommandLine parseCLI(String[] args, Options options) {
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println("Arguments could not be parser. Use '-h' or '--help' to show correct usage.");
			System.exit(1);
		}
		return cmd;
	}

	/**
	 * @param options
	 */
	private void showHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("File Encryptor", options);
	}

	private Options prepareOptions() {
		Options options = new Options();
		options.addOption("e", "encrypt", false, "This flag sets the application to encryption mode");
		//options.addOption("d", "decrypt", false, "This flag sets the application to decryption mode");
		options.addOption("i", "in", true, "Input file");
		options.addOption("o", "out", true,
				"Output file name. If this parameter is not given, the output filename in encryption mode is just the input filename + '.enc' or '.dec' in decryption mode.");
		//options.addOption("r", "remove", false, "Remove the input file after the operation is complete");
		options.addOption("t", "verbose", false, "Switch on verbode mode");
		//options.addOption("c", "verify", false, "Verify the operation");
		//options.addOption("p", "password", true, "Encrypt/Decrypt using a password");
		//options.addOption("k", "key", true, "Encrypt/Decrypt using a key. Must be encoded in Base64");
		options.addOption("w", "writekey", false, "Write the key to the console once operation is complete");
		options.addOption("h", "help", false, "Show usage instructions");
		options.addOption("v", "version", false, "Show version information");
		//options.addOption("g", "gui", false, "Show GUI");
		//options.addOption("b", "blocks", true, "Encrypt the file in block instead of as a whole");
		return options;
	}

}
