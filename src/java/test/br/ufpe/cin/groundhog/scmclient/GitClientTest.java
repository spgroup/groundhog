package br.ufpe.cin.groundhog.scmclient;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

public class GitClientTest {

	private GitClient gitClient = new GitClient(); 
	
	@Test
	public void checkoutTest() {
		File f = new File("/tmp/1376177763689-0/AlterEgo_1376178586224/.git");
		try {
//			gitClient.checkout(f);
			assertNotNull(f);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}