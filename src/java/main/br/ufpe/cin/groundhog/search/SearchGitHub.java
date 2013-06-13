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
	private static String root = "https://api.github.com";
	private static final String REPO_API = "https://api.github.com";
	private static final String USERS_API = "https://api.github.com/users/";
	
	private final Requests requests;
	private final Gson gson;

	// Used to increase the number of GitHub API requests per hour
	private String gitHubOauthAcessToken;
	
	@Inject
	public SearchGitHub(Requests requests) {
		this.requests = requests;
		this.gson = new Gson();
		this.gitHubOauthAcessToken = null;
	}
	
	public SearchGitHub(Requests requests, Gson gson, String gitHubOauthAcessToken) {	
		this.requests = requests;
		this.gson = gson;
		//Personal API Access Tokens
		this.gitHubOauthAcessToken = gitHubOauthAcessToken;
		// TODO get this through command line parameter
	}
	
	public List<Project> getProjects(String term, int page, int limit) throws SearchException {
		try {
			if (term == null) {
				return getAllForgeProjects(page, limit); 
			}
			
			List<Project> projects = new ArrayList<Project>();
			String searchUrl = root + String.format("/legacy/repos/search/%s?start_page=%s&language=java",
					requests.encodeURL(term), page);
			
			if( this.gitHubOauthAcessToken != null ){
				searchUrl += String.format("&access_token=%s", this.gitHubOauthAcessToken);
			}
			
			String json = requests.get(searchUrl);
			JsonObject jsonObject = gson.fromJson(json, JsonElement.class).getAsJsonObject();			
			JsonArray jsonArray = jsonObject.get("repositories").getAsJsonArray();
			
			for (int i = 0; i < jsonArray.size() && (i < limit || limit < 0); i++) {
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
			String searchUrl = String.format("%s/repos/%s/%s", REPO_API, username, requests.encodeURL(term));
			
			if( this.gitHubOauthAcessToken != null){
				searchUrl += String.format("&access_token=%s", this.gitHubOauthAcessToken);
			}
			
			String json = requests.get(searchUrl);
			
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
	
	public List<Project> getAllForgeProjects(int start, int limit) throws SearchException{
		String searchUrl  = null;
		List<Project> projects = new ArrayList<Project>();
		try{
			int since = start;
			int totalRepositories = 0;
			while(totalRepositories < limit || limit < 0){
				searchUrl = root + String.format("/repositories?since=%s&language=java", since);
				
				if( this.gitHubOauthAcessToken != null){
					searchUrl += String.format("&access_token=%s", this.gitHubOauthAcessToken);
				}
								
				String jsonString = requests.get(searchUrl);
				JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
				if( jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("message") ){
					throw new GroundhogException( jsonElement.getAsJsonObject().get("message").toString() );
				}
					
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				for (int i = 0; i < jsonArray.size() && 
						(totalRepositories + i < limit || limit < 0); i++) {
					
					String repoName = jsonArray.get(i).getAsJsonObject().get("name").getAsString();					
					
					String searchUrlLegacy  = null;					
					searchUrlLegacy = root
							+ String.format(
									"/legacy/repos/search/%s?language=java", repoName);
					
					if( this.gitHubOauthAcessToken != null){
						searchUrlLegacy += String.format("&access_token=%s", this.gitHubOauthAcessToken);
					}
					
					System.out.println(repoName + " " + searchUrlLegacy);
					String jsonLegacy = requests.get(searchUrlLegacy);
					

					if( jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("message") ){
						throw new GroundhogException( jsonElement.getAsJsonObject().get("message").toString() );
					}
					
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