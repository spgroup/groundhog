package br.ufpe.cin.groundhog.answers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import br.ufpe.cin.groundhog.Language;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.http.HttpModule;
import br.ufpe.cin.groundhog.search.SearchGitHub;
import br.ufpe.cin.groundhog.search.SearchModule;

public class ToGetProjectsWithMoreThanOneLanguage {

	private SearchGitHub searchGitHub;
	
	@Before
	public void init() {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		searchGitHub = injector.getInstance(SearchGitHub.class);
	}
	
	@Test
	public void testProjectsWIthMoreThanOneLanguage() throws IOException{
		List<Project> rawData = searchGitHub.getAllProjects(1, 8);
		
		List<Project> projects = new ArrayList<Project>();
		for (Project project : rawData) {
			List<Language> languages = searchGitHub.fetchProjectLanguages(project);
			
			if(languages.size() > 1){
				projects.add(project);
			}
		}
		
		float percent = ((Float.intBitsToFloat(projects.size())/Float.intBitsToFloat(rawData.size()))*100);
		
		String result = "There are " + rawData.size() + " projects in github \n" +
				"There are " + projects.size() +" projects with more than one language \n" +
				"This is " + percent + "% of the total";
		
		System.out.println(result);
	}
}