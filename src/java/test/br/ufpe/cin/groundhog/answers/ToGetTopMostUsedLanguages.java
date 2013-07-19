package br.ufpe.cin.groundhog.answers;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.groundhog.Language;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.http.HttpModule;
import br.ufpe.cin.groundhog.search.SearchGitHub;
import br.ufpe.cin.groundhog.search.SearchModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ToGetTopMostUsedLanguages {
	private SearchGitHub searchGitHub;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		searchGitHub = injector.getInstance(SearchGitHub.class);
	}
	
	@Test
	public void testGetTopTenMostUsedLanguages() {
		try {			
			List<Project> projects = searchGitHub.getProjects("facebook", 1, 10);
			Assert.assertNotNull(projects);
			
			List<Language> languages = Projects.getTopMostUsedLanguages(projects, 10);
			Assert.assertNotNull(languages);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
}