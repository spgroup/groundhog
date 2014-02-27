package br.ufpe.cin.groundhog.metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

	public class CmdParseTest {
		
		public static void main(String args[]) throws FileNotFoundException{
			Scanner scanner = new Scanner(new File("/home/bruno/scm/github.com/groundhog2/src/java/main/br/ufpe/cin/groundhog/metrics/CmdParseTest.java"));
		    scanner.useDelimiter("\\Z");
		    String source=scanner.next();
		    scanner.close();
			Statistics st=Parsing.parsing(source);
			System.out.println(st.totalLine());
			System.out.println(st.maxDepth());
			System.out.println(st.avgMethodCall());
			System.out.println(st.totalSMethods());
			System.out.println(st.Interfaces());
			System.out.println(st.Classes());
			System.out.println(st.avgCycloComplexity());
		}
	}

