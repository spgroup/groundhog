package br.ufpe.cin.groundhog.database;

import java.net.UnknownHostException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.Commit;
import br.ufpe.cin.groundhog.http.HttpModule;
import br.ufpe.cin.groundhog.search.SearchGitHub;
import br.ufpe.cin.groundhog.search.SearchModule;

/**
 * The test interface for the {@link GroundhogDB} class
 * @author Rodrigo Alves
 *
 */
public class GroundhogDBTest {
	private GroundhogDB groundHogDB;
	private SearchGitHub searchGitHub;
	
	@Before
	public void setup() throws UnknownHostException {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		this.groundHogDB = new GroundhogDB("127.0.0.1", "myGitHubResearch");
		this.searchGitHub = injector.getInstance(SearchGitHub.class);
	}
	
	@Test
	public void testCommitsPersistence() {
		Project project = new Project("yahoo", "samoa");

		// Fetches all commits of the project and persists each one of them to the database
		List<Commit> commits = searchGitHub.getAllProjectCommits(project);

		for (Commit comm: commits) {
			this.groundHogDB.save(comm);
		    System.out.println(comm);
		}
	}
}