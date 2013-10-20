package br.ufpe.cin.groundhog;



import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ReleaseTest {

	private Project project;
	private String name;
	private User user;
	private Release relase;
	private int id;

	@Before
	public void setup(){ /*All examples*/
		this.user = new User("userTest");
		this.name = "testRelease";
		this.project = new Project(user, name);
		this.id = 1; 
		this.relase = new Release(project, id);
	}

	@Test
	public void testReleaseTest() {
		/*Test for url method*/
		try {
			String url = "https://api.github.com/";
			/* repos/:owner/:repo/releases/:id*/
			url += "repos/" + this.project.getOwner().getLogin(); // owner
			url += "/" + this.project.getName(); // repo
			url += "/releases/" + this.id;
			Assert.assertEquals(url, this.relase.getURL());
		}
		catch (Exception e ) {
			Assert.fail();
		}

	}

}
