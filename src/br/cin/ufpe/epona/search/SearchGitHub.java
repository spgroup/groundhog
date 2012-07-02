package br.cin.ufpe.epona.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import br.cin.ufpe.epona.entity.ForgeProject;
import br.cin.ufpe.epona.http.ParamBuilder;
import br.cin.ufpe.epona.http.Requests;

public class SearchGitHub implements ForgeSearch {
	
	private static SearchGitHub instance;
	
	public static SearchGitHub getInstance() {
		if (instance == null) {
			instance = new SearchGitHub();
		}
		return instance;
	}
	
	private SearchGitHub() {	
	
	}
	
	public List<ForgeProject> getProjects(String term, int page) throws IOException {
		List<ForgeProject> projects = new ArrayList<ForgeProject>();
		String paramsStr =
			new ParamBuilder().
			addParam("q", term).
			addParam("repo", "").
			addParam("langOverride", "").
			addParam("start_value", String.valueOf(page)).
			addParam("type", "Repositories").
			addParam("language", "Java").
			build();
		Document doc = Jsoup.parse(Requests.getInstance().get("https://github.com/search?" + paramsStr));
		for (Element result : doc.select("div.results div.result")) {
			Element a = result.select("a").first();
			String[] splitted = a.attr("href").split("/");
			String username = splitted[1];
			String projectName = splitted[2];
			Element descriptionDiv = result.select("div.description").first();
			String description = descriptionDiv.text();
			ForgeProject forgeProject = new ForgeProject(projectName, description);
			forgeProject.setCreator(username);
			projects.add(forgeProject);
		}
		return projects;
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(SearchGitHub.getInstance().getProjects("github api", 1));
	}
}
