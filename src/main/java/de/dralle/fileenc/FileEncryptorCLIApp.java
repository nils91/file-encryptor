/**
 * 
 */
package de.dralle.fileenc;

import java.net.URISyntaxException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
		options.addOption("d", "decrypt", false, "This flag sets the application to decryption mode");
		options.addOption("i", "in", true, "Input file");
		options.addOption("o", "out", true,
				"Output file name. If this parameter is not given, the output filename in encryption mode is just the input filename + '.enc' or '.dec' in decryption mode.");
		options.addOption("r", "remove", false, "Remove the input file after the operation is complete");
		options.addOption("t", "verbose", false, "Switch on verbode mode");
		options.addOption("c", "verify", false, "Verify the operation");
		options.addOption("p", "password", true, "Encrypt/Decrypt using a password");
		options.addOption("k", "key", true, "Encrypt/Decrypt using a key. Must be encoded in Base64");
		options.addOption("w", "writekey", false, "Write the key to the console once operation is complete");
		options.addOption("h", "help", false, "Show usage instructions");
		options.addOption("v", "version", false, "Show version information");
		options.addOption("g", "gui", false, "Show GUI");
		options.addOption("b", "blocks", true, "Encrypt the file in block instead of as a whole");
		return options;
	}

}
