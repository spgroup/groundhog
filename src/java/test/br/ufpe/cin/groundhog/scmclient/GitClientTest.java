package br.ufpe.cin.groundhog.scmclient;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import br.ufpe.cin.groundhog.util.Dates;

public class GitClientTest {

	private GitClient gitClient = new GitClient(); 
	
	@Test
	public void checkoutTest() {
		File f = new File("/tmp/1376177763689-0/AlterEgo_1376178586224");
		try {
			gitClient.checkout(f, new Dates("yyyy-MM-dd").format("2012-11-19"));
			assertNotNull(f);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}