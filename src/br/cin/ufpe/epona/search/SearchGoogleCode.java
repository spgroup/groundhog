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

public class SearchGoogleCode implements ForgeSearch {
	
	private static String root = "http://code.google.com";
	private static SearchGoogleCode instance;
	
	public static SearchGoogleCode getInstance() {
		if (instance == null) {
			instance = new SearchGoogleCode();
		}
		return instance;
	}
	
	private SearchGoogleCode() {
		
	}
	
	public List<ForgeProject> getProjects(String term, int page) throws IOException {
		List<ForgeProject> projects = new ArrayList<ForgeProject>();
		String paramsStr =
			new ParamBuilder().
			addParam("q", term + " label:Java").
			addParam("start", String.valueOf((page - 1) * 10)).
			build();
		Document doc = Jsoup.parse(Requests.getInstance().get(root + "/hosting/search?" + paramsStr));
		for (Element tr : doc.select("#serp table tbody tr")) {
			Element a = tr.child(0).child(0);
			String projectName = a.attr("href").split("/")[2];
			String description = tr.child(1).ownText();
			String imgSrc = a.child(0).attr("src");
			String iconURL = imgSrc;
			if (imgSrc.startsWith("/")) {
				iconURL = root + iconURL;
			}
			ForgeProject forgeProject = new ForgeProject(projectName, description, iconURL);
			projects.add(forgeProject);
		}
		return projects;
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(SearchGoogleCode.getInstance().getProjects("", 1));
	}
	
}
