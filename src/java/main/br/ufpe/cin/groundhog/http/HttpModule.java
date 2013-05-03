package br.ufpe.cin.groundhog.http;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class HttpModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Requests.class).in(Singleton.class);
	}

}