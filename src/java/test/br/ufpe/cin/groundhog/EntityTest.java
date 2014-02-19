package br.ufpe.cin.groundhog;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


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
	private Project projectTest;
	
	@Before
	public void setup() {
		projectTest = new Project("projectUserLogin", "Test Project");
		userTest = new User("testUserLogin");
		contributorTest = new Contributor("testUserLogin");
		releaseTest = new Release(projectTest, 1);
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
			
			if(!userTest.equals(secondUser)){
				Assert.fail();
			}
			
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
			if(!contributorTest.equals(secondContributor)){
				Assert.fail();
			}
		
			/*
			 * The Contributor URL must be equals to the URL from a User object that contains the same login.
			 * */
			String contributorUrl = contributorTest.getURL();
			String userUrl = userTest.getURL();
			if(!userUrl.equals(contributorUrl)){
				Assert.fail();				
			}
			
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
			Release secondRelease = new Release(projectTest, releaseTestId);
			if(!releaseTest.equals(secondRelease)){
				Assert.fail();
			}

			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	/**
	 * <p>Test all basic methods on {@link Project} class.</p>
	 * */
	@Test
	public void testProject(){
		try {
			String loginProjectTest = projectTest.getUser().getLogin();
			String nameProjectTest = projectTest.getName();
			Project secondProject = new Project(loginProjectTest, nameProjectTest);
			
			/*
			 * If two projects have the same ID, then they are equals.
			 * */
			if(!projectTest.equals(secondProject)){
				Assert.fail();
			}
			
			/*
			 * 
			 * If we have Project P that:
			 * P.wachtersCount > 4
			 * P.forks_count > 1
			 * P.commits.size() > 100
			 * P.issues.size() > 5
			 * Then, P.isMature == true
			 * */
			ArrayList<Commit> commitsTest = new ArrayList<>();
			ArrayList<Issue> issuesTest = new ArrayList<>();
			for(int i = 0; i < 105; i++){ // add a Commit List  that size is bigger than 100.
				commitsTest.add(new Commit("commitTest", projectTest));
			}
			for(int i = 0; i < 8; i++){ // add a Issue List that size is bigger than 8.
				issuesTest.add(new Issue(projectTest, i, "Open")); 
			}
			secondProject.setWatchersCount(4);
			secondProject.setForksCount(2);
			secondProject.setCommits(commitsTest);
			secondProject.setIssues(issuesTest);
			
			/*If all above conditions are met, then secondProject.isMature must be true*/
			Assert.assertEquals(true, secondProject.isMature());
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
			
		}
	}

	
}
