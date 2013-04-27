package br.ufpe.cin.groundhog.search;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class SearchModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SearchGitHub.class).in(Singleton.class);
		bind(SearchGoogleCode.class).in(Singleton.class);
		bind(SearchSourceForge.class).in(Singleton.class);
	}
}