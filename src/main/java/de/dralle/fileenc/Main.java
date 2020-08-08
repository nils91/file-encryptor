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
		FileEncryptorCLIApp app=new FileEncryptorCLIApp();
		app.run(args);		
	}

}
