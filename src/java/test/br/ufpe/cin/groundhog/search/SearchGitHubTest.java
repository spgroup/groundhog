package br.ufpe.cin.groundhog.search;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SearchGitHubTest {

	private SearchGitHub searchGitHub;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule());
		searchGitHub = injector.getInstance(SearchGitHub.class);
	}

	@Test
	public void testSimpleSearch() {
		try {
			
			long time = System.nanoTime();
			System.out.println(searchGitHub.getProjects("github api", 1));
			System.out.printf("Elapsed: %.2f",
					(System.nanoTime() - time) / 1000000000.0);
		} catch (Exception e) {
			Assert.fail();
		}
	}
}
