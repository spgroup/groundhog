package br.ufpe.cin.groundhog.search;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.groundhog.Language;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.http.HttpModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SearchGitHubTest {
	
	private SearchGitHub searchGitHub;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		searchGitHub = injector.getInstance(SearchGitHub.class);
	}
	
	@Test
	public void testSearchByProjectName() {
		try {			
			List<Project> projects = searchGitHub.getProjects("groundhog", 1, 3);
			searchGitHub.fetchProjectLanguages(projects.get(0));
			Assert.assertNotNull(projects);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testFetchByProjectLanguages() {
		try {
			List<Project> projects = searchGitHub.getProjects("groundhog", 1, 3);
			List<Language> langs = searchGitHub.fetchProjectLanguages(projects.get(0));
			Assert.assertNotNull(langs);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testSearchProjectsByUser() {
		try {
			List<Project> projects = searchGitHub.getProjects("groundhog", "spggroup", 1);
			Assert.assertNotNull(projects);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testSimpleSearch() {
		try {
			List<Project> projects = searchGitHub.getProjects("github api", 1,-1);
			Assert.assertNotNull(projects);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testSearchMoreThenOneLanguage() {
		try {
			String result = searchGitHub.getProjectsWithMoreThanOneLanguageString(1, 8);
			Assert.assertNotNull(result);
			
			// System.out.println(result);
			// This line will get all the projects in raw and print them
			// System.out.println(searchGitHub.getProjectsWithMoreThanOneLanguage(1, 5));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}