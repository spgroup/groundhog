package br.ufpe.cin.groundhog.codehistory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class CodeHistoryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GitCodeHistory.class).in(Singleton.class);
		bind(SFCodeHistory.class).in(Singleton.class);
		bind(SvnCodeHistory.class).in(Singleton.class);
	}
}
