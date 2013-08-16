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
	
	@Test
	public void testSearchByProjectName() {
		try {			
			List<Project> projects = searchGitHub.getProjects("groundhog", 1, SearchGitHub.INFINITY);
			searchGitHub.getProjectLanguages(projects.get(0));
			Assert.assertNotNull(projects);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testFetchByProjectLanguages() {
		try {
			List<Language> langs = searchGitHub.getProjectLanguages(fakeProject);
			Assert.assertNotNull(langs);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testGetAllProjects() {
		try {
			List<Project> projects = searchGitHub.getAllProjects(0, 5);
			Assert.assertNotNull(projects);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testGetProjectsByLanguage() {
		try {
			List<Project> projects = searchGitHub.getAllProjectsByLanguage("java");
			Assert.assertNotNull(projects);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testGetProjectsActiveByYear() {
		try {
			
			List<Project> projects = searchGitHub.getProjectActiveByYear("2012-01-01", "2012-12-31", 500);
			double percent = (projects.size()/500.0)*100.0;
			System.out.println( percent + "% of the projects java");
			Assert.assertNotNull(projects);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
}