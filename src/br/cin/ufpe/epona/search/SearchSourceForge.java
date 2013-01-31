package br.cin.ufpe.epona.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import br.cin.ufpe.epona.entity.ForgeProject;
import br.cin.ufpe.epona.entity.SCM;
import br.cin.ufpe.epona.http.ParamBuilder;
import br.cin.ufpe.epona.http.Requests;

public class SearchSourceForge implements ForgeSearch {
	
	private static SearchSourceForge instance;
	
	public static SearchSourceForge getInstance() {
		if (instance == null) {
			instance = new SearchSourceForge();
		}
		return instance;
	}
	
	private SearchSourceForge() {
		
	}
	
	public List<ForgeProject> getProjects(String term, int page) throws SearchException {
		try {
			List<ForgeProject> projects = new ArrayList<ForgeProject>();
			String paramsStr =
				new ParamBuilder().
				add("q", term).
				add("sort", "popular").
				add("page", String.valueOf(page)).
				build();
			Document doc = Jsoup.parse(Requests.getInstance().get("http://sourceforge.net/directory/language:java/?" + paramsStr));
			for (Element li : doc.select(".projects > li")) {
				Element a = li.select("[itemprop=url]").first();
				if (a != null) {
					String projectName = a.attr("href").split("/")[2];
					String description = li.select("[itemprop=description]").first().text();
					String iconURL = li.select("[itemprop=image]").first().attr("src");
					if (iconURL.startsWith("//")) {
						iconURL = "http:" + iconURL;
					}
					String projectURL = String.format("http://sourceforge.net/projects/%s/files/", projectName);
					ForgeProject forgeProject = new ForgeProject(projectName, description, iconURL, SCM.SOURCE_FORGE, projectURL);
					projects.add(forgeProject);
				}
			}
			return projects;
		} catch (IOException e) {
			throw new SearchException(e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(SearchSourceForge.getInstance().getProjects("", 1));
	}
	
}
