package br.ufpe.cin.groundhog.search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.ufpe.cin.groundhog.GroundhogException;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.http.Requests;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;

/**
 * Performs the project search on GitHub, via its official JSON API
 * 
 * @author fjsj, gustavopinto, rodrigoalvesvieira
 */
public class SearchGitHub implements ForgeSearch {
	private final static String root = "https://api.github.com";
	private final Requests requests;
	private final Gson gson;

	@Inject
	public SearchGitHub(Requests requests) {
		this.requests = requests;
		this.gson = new Gson();
	}

	private List<Project> gatherProjects(String url) {
		try {
			List<Project> projects = new ArrayList<Project>();

			String json = requests.get(url);
			
			JsonObject jsonObject = gson.fromJson(json, JsonElement.class).getAsJsonObject();
			JsonArray jsonArray = jsonObject.get("repositories").getAsJsonArray();

			for (int i = 0; i < jsonArray.size(); i++) {
				String element = jsonArray.get(i).toString();
				Project p = gson.fromJson(element, Project.class);
				p.setSCM(SCM.GIT);
				p.setScmURL(String.format("git://github.com/%s/%s.git", p.getOwner(), p.getName()));
				projects.add(p);
			}
			return projects;
		} catch (IOException | GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}

	public List<Project> getProjects(String term, int page)
			throws SearchException {
		term = requests.encodeURL(term);
		String searchUrl = String.format("%s/legacy/repos/search/%s?start_page=%s&language=java", root, term, page);
		return gatherProjects(searchUrl);
	}

	@Override
	public List<Project> getProjects(String term, String username, int page)
			throws SearchException {
		
		try {
			term = requests.encodeURL(term);
			String url = String.format("%s/repos/%s/%s", root, username, term);
			
			String 	json = requests.get(url);
			
			Project p = new GsonBuilder().excludeFieldsWithModifiers(Modifier.VOLATILE).create().fromJson(json, Project.class);
			p.setSCM(SCM.GIT);
			p.setScmURL(String.format("git://github.com/%s/%s.git", username, p.getName()));
			
			return Lists.newArrayList(p);
		
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		String json = new Scanner(new File("/home/ghlp/Desktop/example.json")).nextLine();
		Project p = new GsonBuilder().excludeFieldsWithModifiers(Modifier.VOLATILE).create().fromJson(json, Project.class);
		System.out.println(p.getName());
	}
}