package br.ufpe.cin.groundhog.search;

import org.junit.Assert;
import org.junit.Test;

import br.ufpe.cin.groundhog.search.UrlBuilder.GithubAPI;

public class UrlBuilderTest {

	private UrlBuilder builder = new UrlBuilder("&fake");
	
	@Test
	public void getProjectUrlV2() {
		String searchUrl = builder.uses(GithubAPI.LEGACY_V2)
				  .withParam("java")
				  .withParam("start_page", 2)
				  .withParam("language", "java")
				  .build();
		
		Assert.assertEquals("https://api.github.com/legacy/repos/search/java?start_page=2&language=java&fake", searchUrl);
	}
	
	@Test 
	public void getRepositoriesV3(){
		String searchUrl = builder.uses(GithubAPI.REPOSITORIES)
								.withParam("language", "java")
								.withParam("since", 2)
								.build();
		
		Assert.assertEquals("https://api.github.com/repositories?language=java&since=2&fake", searchUrl);
	}
	
	@Test
	public void getSpecificURL() {
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", "spgroup")
				  .withSimpleParam("/", "groundhog")
				  .withParam("/languages")
				  .build();
		
		Assert.assertEquals("https://api.github.com/repos/spgroup/groundhog/languages&fake", searchUrl);
	}
	
	@Test
	public void getSpecificURLIssues() {
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", "spgroup")
				  .withSimpleParam("/", "groughog")
				  .withParam("/issues")
				  .build();
		
		Assert.assertEquals("https://api.github.com/repos/spgroup/groughog/issues&fake", searchUrl);
	}
	
	@Test
	public void getAllCommitsByDate(){
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", "spgroup")
				  .withSimpleParam("/", "groundhog")
				  .withSimpleParam("/", "commits")
				  .withParam("since", 1)
				  .withParam("until", 10)
				  .build();

		System.out.println(searchUrl);
	}
}
