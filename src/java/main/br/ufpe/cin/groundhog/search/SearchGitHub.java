package br.ufpe.cin.groundhog.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import br.ufpe.cin.groundhog.GroundhogException;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.http.Requests;

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
	private static String root = "https://api.github.com";
	private final Requests requests;
	private final Gson gson;

	@Inject
	public SearchGitHub(Requests requests) {
		this.requests = requests;
		this.gson = new Gson();
	}	
	
	public List<Project> getProjects(String term, int page)
			throws SearchException {
		try {
			if( term == null ){
				return getAllForgeProjects(0, -1);
			}
			List<Project> projects = new ArrayList<Project>();
			String searchUrl  = null;					
			searchUrl = root
					+ String.format(
							"/legacy/repos/search/%s?start_page=%s&language=java",
							requests.encodeURL(term), page);
			
			String json = requests.get(searchUrl);
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
	
	public List<Project> getAllForgeProjects( int start, int limit ) throws SearchException{
		String searchUrl  = null;
		List<Project> projects = new ArrayList<Project>();
		try{
			int since = start;
			int totalRepositories = 0;
			while(totalRepositories < limit || limit < 0){
				searchUrl = root + String.format("/repositories?since=%s&language=java", since);
				String json = requests.get(searchUrl);
				JsonArray jsonArray = gson.fromJson(json, JsonElement.class).getAsJsonArray();
				for (int i = 0; i < jsonArray.size() && 
						(totalRepositories + i < limit || limit < 0); i++) {
					
					//TODO - The v3 search and legacy search returns different information 
					// regarding the project.
					String repoName = jsonArray.get(i).getAsJsonObject().get("name").getAsString();					
					
					String searchUrlLegacy  = null;					
					searchUrlLegacy = root
							+ String.format(
									"/legacy/repos/search/%s?language=java", repoName);
					System.out.println(repoName + " " + searchUrlLegacy);
					String jsonLegacy = requests.get(searchUrlLegacy);
					JsonObject jsonObject = gson.fromJson(jsonLegacy, JsonElement.class).getAsJsonObject();			
					JsonArray jsonArrayLegacy = jsonObject.get("repositories").getAsJsonArray();
					
					if( jsonArrayLegacy.size() == 0 ) continue; // not a java project
					
					String element = jsonArrayLegacy.get(0).toString(); // results are sorted by best match
					Project p = gson.fromJson(element, Project.class);
					p.setSCM(SCM.GIT);
					p.setScmURL(String.format("git://github.com/%s/%s.git", p.getOwner(), p.getName()));
					
					projects.add(p);
					totalRepositories++;
				}				
				JsonElement lastPagesRepository = jsonArray.get(jsonArray.size() -1);
				since = lastPagesRepository.getAsJsonObject().get("id").getAsInt();
			}
		} catch (IOException | GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
		return projects;
	}
	
}