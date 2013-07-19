package br.ufpe.cin.groundhog.search;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.groundhog.Language;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.User;
import br.ufpe.cin.groundhog.http.HttpModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SearchGitHubTest {
	
	private SearchGitHub searchGitHub;
	private Project fakeProject;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		searchGitHub = injector.getInstance(SearchGitHub.class);
		User user = new User("elixir-lang");
		fakeProject = new Project("elixir", "", SCM.GIT, "git@github.com:elixir-lang/elixir.git");
		fakeProject.setUser(user);
	}
	
	public void testSearchByProjectName() {
		try {			
			List<Project> projects = searchGitHub.getProjects("groundhog", 1, SearchGitHub.INFINITY);
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
			List<Language> langs = searchGitHub.fetchProjectLanguages(fakeProject);
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
}