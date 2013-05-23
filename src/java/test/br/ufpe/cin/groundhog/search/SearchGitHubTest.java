package br.ufpe.cin.groundhog.search;


import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
			List<Project> projects = searchGitHub.getProjects("groundhog", 1);
			Assert.assertNotNull(projects);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testSearchProjectsByUser() {
		try {
			List<Project> projects = searchGitHub.getProjects("groundhog", "spg", 1);
			Assert.assertNotNull(projects);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
