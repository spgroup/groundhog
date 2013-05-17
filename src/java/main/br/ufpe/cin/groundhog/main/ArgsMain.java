package br.ufpe.cin.groundhog.main;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class ArgsMain {

	public static void main(String[] args) {
		Options opt = new Options();
		CmdLineParser parser = new CmdLineParser(opt);
		try {
			parser.parseArgument(args);
			System.out.println(opt.getInputFile());
		} catch (CmdLineException e) {
			e.printStackTrace();
			System.err.println("java -jar myprogram.jar [options...] arguments...");
			parser.printUsage(System.err);
			return;
		}
	}
}
