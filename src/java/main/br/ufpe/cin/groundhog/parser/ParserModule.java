package br.ufpe.cin.groundhog.parser;

import br.ufpe.cin.groundhog.parser.java.JavaParser;
import br.ufpe.cin.groundhog.parser.license.LicenseParser;

import com.google.inject.AbstractModule;

public class ParserModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Parser.class).to(JavaParser.class);
		bind(Parser.class).to(LicenseParser.class);
	}
}
