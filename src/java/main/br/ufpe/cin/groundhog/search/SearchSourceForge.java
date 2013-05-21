package br.ufpe.cin.groundhog.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.http.ParamBuilder;
import br.ufpe.cin.groundhog.http.Requests;

import com.google.inject.Inject;

/**
 * Performs the search for projects on SourceForge
 * @author fjsj, gustavopinto, rodrigoalvesvieira
 *
 */
public class SearchSourceForge implements ForgeSearch {
	private final Requests requests;
	
	@Inject
	public SearchSourceForge(Requests requests) {	
		this.requests = requests;
	}
	
	public List<Project> getProjects(String term, int page) throws SearchException {
		try {
			List<Project> projects = new ArrayList<Project>();
			String paramsStr =
				new ParamBuilder().
				add("q", term).
				add("sort", "popular").
				add("page", String.valueOf(page)).
				build();
			
			Document doc = Jsoup.parse(requests.get("http://sourceforge.net/directory/language:java/?" + paramsStr));
			
			for (Element li: doc.select(".projects > li")) {
				Element a = li.select("[itemprop=url]").first();
			    
				if (a != null) {
					String projectName, description, iconURL, projectURL;
					
					projectName = a.attr("href").split("/")[2];
					description = li.select("[itemprop=description]").first().text();
					iconURL = li.select("[itemprop=image]").first().attr("src");
					
					if (iconURL.startsWith("//")) {
						iconURL = "http:" + iconURL;
					}
					
					projectURL = String.format("http://sourceforge.net/projects/%s/files/", projectName);
					
					Project forgeProject = new Project(projectName, description, iconURL, SCM.SOURCE_FORGE, projectURL);
					projects.add(forgeProject);
				}
			}
			return projects;
		} catch (IOException e) {
			throw new SearchException(e);
		}
	}

	@Override
	public List<Project> getAllForgeProjects(int start, int limit)
			throws SearchException {
		// TODO Auto-generated method stub
		return null;
	}
}