package br.ufpe.cin.groundhog.answers;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.groundhog.Language;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.search.SearchGitHub;
import br.ufpe.cin.groundhog.search.SearchModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class getTopMostUsedLanguages {
	private SearchGitHub searchGitHub;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule());
		searchGitHub = injector.getInstance(SearchGitHub.class);
		this.searchGitHub.setGitHubOauthAcessToken("269a932b28b54cc4520a6f042a0da5f8e149da34");
	}
	
	@Test
	public void testGetTopTenMostUsedLanguages() {
		try {			
			List<Project> projects = searchGitHub.getProjects("facebook", 1, 10);
			List<Language> languages = Project.getTopMostUsedLanguages(projects, 10);
			Assert.assertNotNull(projects);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
}