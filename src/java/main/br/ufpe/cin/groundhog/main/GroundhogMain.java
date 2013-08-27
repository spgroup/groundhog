package br.ufpe.cin.groundhog.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufpe.cin.groundhog.GroundhogException;
import br.ufpe.cin.groundhog.util.FileUtil;

public abstract class GroundhogMain {
	private final static Logger logger = LoggerFactory.getLogger(GroundhogMain.class);
	public abstract void run(JsonInputFile input);

	public static void main(String[] args) {
		JsonInputFile input = null;
		CmdOptions opt = new CmdOptions();
		CmdLineParser parser = new CmdLineParser(opt);
		PrintStream errorStream = null;

		// Redirect System.err to file
		try {
			errorStream = new PrintStream("error.log");
			System.setErr(errorStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		try {

			logger.info("Groundhog was initialized!");
			parser.parseArgument(args);

			input = checkArguments(opt);

			logger.info("We received the following parameters: " + input);
			new CmdMain().run(input);

			// Free resources and delete temporary directories
			logger.info("Disposing resources...");
			freeResources(errorStream);
			logger.info("Done!");

		} catch (CmdLineException | GroundhogException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	private static JsonInputFile checkArguments(CmdOptions opt) {
		if (opt.getInputFile() != null) {
			return opt.getInputFile();
		}
		return new JsonInputFile(opt);
	}

	/**
	 * Deletes the temporary directories and closes the log streams
	 * 
	 * @param errorStream
	 *            the error stream to be closed
	 */
	public static void freeResources(OutputStream errorStream) {
		try {
			FileUtil.getInstance().deleteTempDirs();
		} catch (IOException e) {
			logger.warn("Could not delete temporary folders (but they will eventually be deleted)");
		}

		try {
			if (errorStream != null) {
				errorStream.close();
			}
		} catch (IOException e) {
			logger.error("Unable to close error.log stream", e);
		}
	}
}