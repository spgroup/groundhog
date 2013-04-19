package br.ufpe.cin.groundhog.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.ufpe.cin.groundhog.GroundhogException;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.http.HttpModule;
import br.ufpe.cin.groundhog.http.Requests;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class SearchGitHub implements ForgeSearch {
	private static String root = "https://api.github.com";
	private static SearchGitHub instance;
	
	
	private Requests requests;
	
	public static SearchGitHub getInstance() {
		if (instance == null) {
			Injector injector = Guice.createInjector(new HttpModule());
			Requests requests = injector.getInstance(Requests.class);
			instance = new SearchGitHub(requests);
		}
		return instance;
	}
	
	@Inject
	public SearchGitHub(Requests requests) {	
		this.requests = requests;
	
	}
	
	private JSONObject getJsonFromAPI(String urlStr) throws IOException, JSONException {
		return new JSONObject(requests.get(urlStr));
	}
	
	public List<Project> getProjects(String term, int page) throws SearchException {
		try {
			List<Project> projects = new ArrayList<Project>();
			String searchUrl = root + String.format("/legacy/repos/search/%s?start_page=%s&language=java",
					requests.encodeURL(term), page);
			JSONArray results = getJsonFromAPI(searchUrl).getJSONArray("repositories");
			
			for (int i = 0; i < results.length(); i++) {
				JSONObject result = results.getJSONObject(i);
				Project forgeProject = new Project();
				
				String name = result.getString("name");
				String username = result.getString("username");
				String description = null;
				if (result.has("description")) {
					description = result.getString("description");
				}
				forgeProject.setName(name);
				forgeProject.setCreator(username);
				forgeProject.setSCM(SCM.GIT);
				forgeProject.setScmURL(String.format("git://github.com/%s/%s.git", username, name));
				forgeProject.setDescription(description);
				
				projects.add(forgeProject);
			}
			return projects;
		} catch (JSONException | IOException | GroundhogException e) {
			throw new SearchException(e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		long time = System.nanoTime();
		System.out.println(SearchGitHub.getInstance().getProjects("github api", 1));
		System.out.printf("Elapsed: %.2f", (System.nanoTime() - time) / 1000000000.0);
	}
}