package br.ufpe.cin.groundhog.scmclient;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ScmModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GitClient.class).in(Singleton.class);
		bind(SVNClient.class).in(Singleton.class);
	}
}