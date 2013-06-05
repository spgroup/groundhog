package br.ufpe.cin.groundhog.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.groundhog.GroundhogException;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.User;
import br.ufpe.cin.groundhog.http.Requests;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
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
	private static final String REPO_API = "https://api.github.com";
	private static final String USERS_API = "https://api.github.com/users/";
	
	private final Requests requests;
	private final Gson gson;

	@Inject
	public SearchGitHub(Requests requests) {
		this.requests = requests;
		this.gson = new Gson();
	}

	@Override
	public List<Project> getProjects(String term, int page)
			throws SearchException {
		try {
			String url = String.format("%s/legacy/repos/search/%s?start_page=%s&language=java", REPO_API, requests.encodeURL(term), page);
			String json = requests.get(url);
			
			JsonObject jsonObject = gson.fromJson(json, JsonElement.class).getAsJsonObject();
			JsonArray jsonArray = jsonObject.get("repositories").getAsJsonArray();

			List<Project> projects = new ArrayList<Project>();
			for (int i = 0; i < jsonArray.size(); i++) {
				
				String element = jsonArray.get(i).toString();

				Project p = gson.fromJson(element, Project.class);
				p.setSCM(SCM.GIT);
				
				String owner = jsonArray.get(i).getAsJsonObject().get("owner").getAsString();
				p.setScmURL(String.format("git@github.com:%s/%s.git", owner, p.getName()));
				
				json = requests.get(USERS_API + owner);
				User user = gson.fromJson(json, User.class);
				
				p.setOwner(user);
				projects.add(p);
			}
			return projects;
		
		} catch (IOException | GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}

	@Override
	public List<Project> getProjects(String term, String username, int page)
			throws SearchException {
		
		try {
			String url = String.format("%s/repos/%s/%s", REPO_API, username, requests.encodeURL(term));
			String json = requests.get(url);
			
			Project p = gson.fromJson(json, Project.class);
			p.setSCM(SCM.GIT);
			p.setScmURL(String.format("git@github.com:%s/%s.git", username, p.getName()));
			
			json = requests.get(USERS_API + username);
			User user = gson.fromJson(json, User.class);
			p.setOwner(user);
			
			return Lists.newArrayList(p);
		
		} catch (Throwable e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}
}