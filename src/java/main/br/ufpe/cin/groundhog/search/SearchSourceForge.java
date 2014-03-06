package br.ufpe.cin.groundhog.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import br.ufpe.cin.groundhog.Issue;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.http.ParamBuilder;
import br.ufpe.cin.groundhog.http.Requests;

import com.google.inject.Inject;

/**
 * Performs the search for projects on SourceForge
 * @author fjsj, gustavopinto, Rodrigo Alves
 * @deprecated
 */
public class SearchSourceForge implements ForgeSearch {
	
	private final Requests requests;
	
	@Inject
	public SearchSourceForge(Requests requests) {	
		this.requests = requests;
	}
	
	public List<Project> getProjects(String term, int page, int limit ) throws SearchException {
		if (term == null) {
			return getAllForgeProjects(page, limit);
		}
		
		try {
			String paramsStr = new ParamBuilder()
									.add("q", term)
									.add("sort", "popular")
									.add("page", String.valueOf(page))
									.build();
			
			Document doc = Jsoup.parse(requests.get("http://sourceforge.net/directory/language:java/?" + paramsStr));
			
			List<Project> projects = new ArrayList<Project>();
			int cont = 0;
			
			for (Element li: doc.select(".projects > li")) {
				if(cont >= limit && limit >=0 ) break;
				Element a = li.select("[itemprop=url]").first();
			    
				if (a != null) {
					String projectName, description, projectURL;
					
					projectName = a.attr("href").split("/")[2];
					description = li.select("[itemprop=description]").first().text();
					projectURL = String.format("http://sourceforge.net/projects/%s/files/", projectName);
					
					Project forgeProject = new Project(projectName, description, projectURL);
					projects.add(forgeProject);
					cont++;
				}
			}
			
			return projects;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}

	@Override
	public List<Project> getProjects(String term, String username, int page) throws SearchException {
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	// yet to be implemented
	@Override
	public List<Project> getAllForgeProjects(int start, int limit)
			throws SearchException {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public List<Issue> getAllProjectIssues(Project gr) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}