package br.ufpe.cin.groundhog;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>A Suit Test to <i>Groundhog</i> entity classes.
 * 	  If any basic class get some change that entails a very strong difference of behavior, maybe other classes on project can 
 *    feel this change and their jobs will be affected. So this Suit Test contains all sanity tests that protect a strong 
 *    behavior difference on classes after some code change.
 * </p> 
 * @author Marlon Reghert - mras & Tomer Simis - tls
 * */
public class EntityTest {

	private User userTest;
	private Contributor contributorTest;
	private Release releaseTest;
	private Project fakeProject;
	
	@Before
	public void setup() {
		fakeProject = new Project("fakeUserLogin", "Fake Project");
		userTest = new User("tls");
		contributorTest = new Contributor("tls");
		releaseTest = new Release(fakeProject, 1);
	}
	
	/**
	 * <p>Test all basic methods on {@link User} class.</p>
	 * */
	@Test
	public void testUser() {
		try {
			/*
			 * If two Users have same login, then they are equals.
			 * */
			String userTestLogin = userTest.getLogin();
			User secondUser = new User(userTestLogin);
			Assert.assertEquals(userTest, secondUser);
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	/**
	 * <p>Test all basic methods on {@link Contributor} class.</p>
	 * */
	@Test
	public void testContributor(){
		try {
			/*
			 * If two Contributors have the same login, then they are equals.
			 * */
			String contributorTestLogin = contributorTest.getLogin();
			Contributor secondContributor = new Contributor(contributorTestLogin);
			Assert.assertEquals(contributorTest, secondContributor);
			
			/*
			 * The Contributor URL must be equals to the URL from a User object that contains the same login.
			 * */
			String contributorUrl = contributorTest.getURL();
			String userUrl = userTest.getURL();
			Assert.assertEquals(contributorUrl, userUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	/**
	 * <p>Test all basic methods on {@link Release} class.</p>
	 * */
	@Test
	public void testRelease(){
		try {
			/*
			 * If two Releases have the same ID, then they are equals.
			 **/
			int releaseTestId = releaseTest.getId();
			Release secondRelease = new Release(fakeProject, releaseTestId);
			Assert.assertEquals(releaseTest, secondRelease);
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	

}
