package br.ufpe.cin.groundhog.http;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

/**
 * 
 * @author ghlp
 * @since 0.0.1
 */
public class HttpModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Requests.class).in(Singleton.class);
		bind(String.class)
				.annotatedWith(Names.named("githubOauthToken"))
				.toInstance("access_token=b0244ac0d27f7e0c227cedbd557d875869a59111");
	}
}