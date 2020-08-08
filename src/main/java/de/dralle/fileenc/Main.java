/**
 * 
 */
package de.dralle.fileenc;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Nils Dralle
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		options.addOption("g","gui",false,"Show GUI");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println("Arguments could not be parser. Use '-h' or '--help' to show correct usage.");
			System.exit(1);
		}
		if (cmd.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("File Encryptor", options);
			System.exit(0);
		}
	}

}
