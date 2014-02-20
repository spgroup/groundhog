package br.ufpe.cin.groundhog.database;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.groundhog.Commit;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.User;
import br.ufpe.cin.groundhog.http.HttpModule;
import br.ufpe.cin.groundhog.search.SearchGitHub;
import br.ufpe.cin.groundhog.search.SearchModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import de.flapdoodle.embed.process.io.IStreamProcessor;
import de.flapdoodle.embed.process.io.NullProcessor;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * The test interface for the {@link GroundhogDB} class
 * @author Rodrigo Alves
 *
 */
public class GroundhogDBTest {
	private GroundhogDB groundHogDB;
	private SearchGitHub searchGitHub;
	
	private static final String PROCESS_ADDRESS = "localhost";
	private static final int PROCESS_PORT = 12345;
	   
    	private static MongodExecutable mongodExecutable = null;
    	private static MongodProcess mongodProcess = null;
    	private static MongoClient mongoClient = null;
	
	@Before
	public void setup() throws Exception {
	
	     	IStreamProcessor stream = new NullProcessor();
	     	MongodStarter runtime = MongodStarter.getInstance(new RuntimeConfigBuilder()
	            .defaults(Command.MongoD)
	            .processOutput(new ProcessOutput(stream, stream, stream))
	            .artifactStore(new ArtifactStoreBuilder()
	                .defaults(Command.MongoD)
	                .build())
	            .build());
	     	this.mongodExecutable = runtime.prepare(new MongodConfigBuilder()
	            .version(Version.Main.PRODUCTION)
	            .net(new Net(PROCESS_PORT, Network.localhostIsIPv6()))
	            .build());
	         
	     	this.mongodProcess = mongodExecutable.start();
	     	this.mongoClient = new MongoClient(PROCESS_ADDRESS, PROCESS_PORT);
		
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		this.groundHogDB = new GroundhogDB(this.mongoClient, "myGitHubResearch");
		this.searchGitHub = injector.getInstance(SearchGitHub.class);
		
	}
	
	@Test
	public void testCommitsPersistence() {
		Project project = new Project(new User("yahoo"), "samoa");

		// Fetches all commits of the project and persists each one of them to the database
		List<Commit> commits = searchGitHub.getAllProjectCommits(project);

		for (Commit comm: commits) {
			this.groundHogDB.save(comm);
		    System.out.println(comm);
		}
	}
	
	@AfterClass
	public static void tearDown() {
	        mongodProcess.stop();
	        mongodExecutable.stop();
	}
}
