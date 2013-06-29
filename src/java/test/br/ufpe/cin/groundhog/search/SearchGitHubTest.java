package br.ufpe.cin.groundhog.search;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.groundhog.Language;
import br.ufpe.cin.groundhog.Project;

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
	
	public void testSearchProjectsByUser() {
		try {
			List<Project> projects = searchGitHub.getProjects("groundhog", "spggroup", 1);
			Assert.assertNotNull(projects);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	public void testSimpleSearch() {
		try {
			long time = System.nanoTime();
			System.out.println(searchGitHub.getProjects("github api", 1,-1));
			System.out.printf("Elapsed: %.2f", (System.nanoTime() - time) / 1000000000.0);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testSearchMoreThenOneLanguage() {
		try {
			long time = System.nanoTime();
			System.out.println(searchGitHub.getProjectsWithMoreThanOneLanguage(1, 5));
			System.out.printf("Elapsed: %.2f", (System.nanoTime() - time) / 1000000000.0);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}