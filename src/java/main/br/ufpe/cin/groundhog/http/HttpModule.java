package br.ufpe.cin.groundhog.http;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class HttpModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Requests.class).in(Singleton.class);
		bind(String.class)
				.annotatedWith(Names.named("githubOauthToken"))
				.toInstance("&access_token=269a932b28b54cc4520a6f042a0da5f8e149da34");
	}
}