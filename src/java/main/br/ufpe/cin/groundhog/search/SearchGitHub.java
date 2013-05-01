package br.ufpe.cin.groundhog.search;

import japa.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.ufpe.cin.groundhog.GroundhogException;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.http.Requests;

import com.google.inject.Inject;

public class SearchGitHub implements ForgeSearch {
	private static String root = "https://api.github.com";
	private Requests requests;
	
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
				
				String name, username, sourceCodeURL, description, lastPushedAt, createdAt;
				int watchersCount, followersCount, forksCount;
				boolean hasDownloads, hasIssues, hasWiki;
				
				name = result.getString("name");
				username = result.getString("username");
				sourceCodeURL = result.getString("url");
				
				lastPushedAt = result.getString("pushed_at");
				createdAt = result.getString("created_at");
								
				description = null;
				
				if (result.has("description")) {
					description = result.getString("description");
				}
				
				watchersCount = Integer.parseInt(result.getString("watchers"));
				followersCount = Integer.parseInt(result.getString("followers"));
				forksCount = Integer.parseInt(result.getString("forks"));
				
				hasDownloads = Boolean.parseBoolean(result.getString("has_downloads"));
				hasIssues = Boolean.parseBoolean(result.getString("has_issues"));
				hasWiki = Boolean.parseBoolean(result.getString("has_wiki"));
				
				forgeProject.setName(name);
				forgeProject.setCreator(username);
				forgeProject.setSCM(SCM.GIT);
				forgeProject.setScmURL(String.format("git://github.com/%s/%s.git", username, name));
				forgeProject.setDescription(description);
				
				forgeProject.setSourceCodeURL(sourceCodeURL);
				forgeProject.setFollowersCount(followersCount);
				
				try {
					forgeProject.setLastPushedAt(lastPushedAt);
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				forgeProject.setHasDownloads(hasDownloads);
				forgeProject.setHasIssues(hasIssues);
				forgeProject.setHasWiki(hasWiki);
				
				System.out.println(
						forgeProject.getSourceCodeURL() + " - " + 
						forgeProject.getFollowersCount() + " - " +  
						forgeProject.hasDownloads() + " - " + 
						forgeProject.hasWiki() + " - " +
						forgeProject.getLastPushedAt() + " - " +
						createdAt);
				
				projects.add(forgeProject);
			}
			return projects;
		} catch (JSONException | IOException | GroundhogException e) {
			throw new SearchException(e);
		}
	}
}