package br.ufpe.cin.groundhog.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.ufpe.cin.groundhog.Commit;
import br.ufpe.cin.groundhog.GroundhogException;
import br.ufpe.cin.groundhog.Issue;
import br.ufpe.cin.groundhog.Language;
import br.ufpe.cin.groundhog.Milestone;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.User;
import br.ufpe.cin.groundhog.http.HttpException;
import br.ufpe.cin.groundhog.http.Requests;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Performs the project search on GitHub, via its official JSON API
 * 
 * @author fjsj, gustavopinto, Rodrigo Alves
 */
public class SearchGitHub implements ForgeSearch {
    public static int INFINITY = -1;

    private static final String ROOT = "https://api.github.com";
	private static final String USERS_API = "https://api.github.com/users/";
	
	private final Requests requests;
	private final Gson gson;

	private final String oauthToken;

	@Inject
	public SearchGitHub(Requests requests, @Named("githubOauthToken") String oauthToken) {
		this.requests = requests;
		this.gson = new Gson();
		this.oauthToken = oauthToken;
	}
	
	public List<Project> getProjects(String term, int page, int limit) throws SearchException {
		try {
			if (term == null) {
				return getAllForgeProjects(page, limit);
			}
			
			String searchUrl = String.format("%s/legacy/repos/search/%s?start_page=%s&language=java%s", ROOT, requests.encodeURL(term), page, this.oauthToken);
			
			String json = requests.get(searchUrl);
			JsonObject jsonObject = gson.fromJson(json, JsonElement.class).getAsJsonObject();			
			JsonArray jsonArray = jsonObject.get("repositories").getAsJsonArray();
			
			List<Project> projects = new ArrayList<Project>();
			for (int i = 0; i < jsonArray.size() && (i < limit || limit < 0); i++) {
				String element = jsonArray.get(i).toString();

				Project p = gson.fromJson(element, Project.class);
				p.setSCM(SCM.GIT);
				
				String owner = jsonArray.get(i).getAsJsonObject().get("owner").getAsString();
				p.setScmURL(String.format("git@github.com:%s/%s.git", owner, p.getName()));
				
				String userJson = requests.get(USERS_API + owner + "?" + oauthToken);
				User user = gson.fromJson(userJson, User.class);
				
				p.setOwner(user);
				projects.add(p);
			}
			
			return projects;
		
		} catch (IOException | GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}

	/**
	 * Obtains from the GitHub API the set of projects with more than one language
	 * @param Start indicates the desired page
	 * @param limit the total of projects that will be returned
	 * @throws SearchException
	 */
	public List<Project> getProjectsWithMoreThanOneLanguage(int page, int limit) throws SearchException {
		try {
			
			List<Project> projects = new ArrayList<Project>();
			List<Project> rawData = getAllProjects(page, limit);
			
			for (Project project : rawData) {
				List<Language> languages = fetchProjectLanguages(project);
				
				if(languages.size() > 1){
					projects.add(project);
				}
			}
			
			return projects;
		
		} catch (GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}

	public List<Project> getAllProjectsByLanguage(String lang) throws SearchException{
		throw new UnsupportedOperationException("not implemented yet.");
	}
	
	/**
	 * Obtains from the GitHub API the set of projects
	 * 
	 * @param Start indicates the desired page
	 * @param limit is the total of projects that are going to me returned 
	 * @throws SearchException
	 */
	public List<Project> getAllProjects(int start, int limit) throws SearchException{
		
		String searchUrl  = null;
		List<Project> projects = new ArrayList<Project>();
		JsonParser parser = new JsonParser();
		
		try{
			
			int since = start;
			int totalRepositories = 0;
			
			while(totalRepositories < limit || limit < 0){
				
				searchUrl = String.format("%s/repositories?since=%s%s", ROOT, since, this.oauthToken);
				
				String response = requests.get(searchUrl);
				JsonElement jsonElement = parser.parse(response);
				
				checkAPIErrorMessage(jsonElement);
					
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				
				int counter = 0;
				
				for (Iterator<JsonElement> iterator = jsonArray.iterator(); (iterator
						.hasNext() && (totalRepositories + counter < limit || limit < 0));) {
					
					JsonElement element = (JsonElement) iterator.next();
					
					String repoName = element.getAsJsonObject().get("name").getAsString();	
					String searchUrlLegacy = String.format("%s/legacy/repos/search/%s?%s", ROOT , repoName, this.oauthToken);
					
					String jsonLegacy = requests.get(searchUrlLegacy);
					jsonElement = parser.parse(jsonLegacy);

					checkAPIErrorMessage(jsonElement);
					
					JsonObject jsonObject = parser.parse(jsonLegacy).getAsJsonObject();			
					JsonArray jsonArrayLegacy = jsonObject.get("repositories").getAsJsonArray();
					
					JsonObject rawJsonObject = jsonArrayLegacy.get(0).getAsJsonObject();
					
					String stringElement = rawJsonObject.toString();
					Project p = gson.fromJson(stringElement, Project.class);
					
					p.setSCM(SCM.GIT);
					String owner = rawJsonObject.getAsJsonObject().get("owner").getAsString();
					p.setScmURL(String.format("git://github.com/%s/%s.git", owner, p.getName()));
					
					User user = new User(owner);
					p.setOwner(user);
					projects.add(p);
					
					counter++;
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

	@Override
	public List<Project> getProjects(String term, String username, int page)
			throws SearchException {
		
		try {
			String searchUrl = String.format("%s/repos/%s/%s%s", ROOT, username, requests.encodeURL(term), this.oauthToken);
			
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
	
	/**
	 * Obtains from the GitHub API the set of languages that compose a {@link Project}
	 * @param project a {@link Project} object to have its languages fetched
	 * @throws IOException
	 */
	public List<Language> fetchProjectLanguages(Project project) {
		
		String searchUrl = String.format("%s/repos/%s/%s/languages", ROOT, project.getUser().getLogin(), project.getName());
		String json = null;
		try {
			json = requests.get(searchUrl).replace("{", "").replace("}", "");
		} catch (IOException e) {
			e.printStackTrace();
			throw new HttpException("Unable to download json file. Is it the correct path?" + searchUrl, e);
		}
		
		List<Language> languages = new ArrayList<>();
		if(!json.equalsIgnoreCase("{}")){
			for (String str: json.split(",")) {
				String[] hash = str.split(":");
				String key = hash[0].trim().replaceAll("\"", "");
				Integer value = Integer.parseInt(hash[1].trim());
				Language lang = new Language(key, value);
				languages.add(lang);
			}
		}
		
		return languages;
	}

	public List<Issue> getAllProjectIssues(Project project) throws IOException {
		List<Issue> collection = new ArrayList<Issue>();

		String searchUrl = String.format("%s/repos/%s/%s/issues",
				ROOT, project.getUser().getLogin(), project.getName());
		String jsonString = requests.get(searchUrl);
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		
		int i = 0;
		Issue issue;
		
		for (; i < jsonArray.size(); i++) {
			issue = gson.fromJson(jsonArray.get(i), Issue.class);
			collection.add(issue);
		}
		
		return collection;
	}
	
	/**
	 * Fetches all the Milestones of the given {@link Project} from the GitHub API
	 * @param project the @{link Project} of which the Milestones are about
	 * @return a {@link List} of {@link Milestone} objects
	 * @throws IOException
	 */
	public List<Milestone> getAllProjectMilestones(Project project) throws IOException {
		
		String searchUrl = String.format("%s/repos/%s/%s/milestones", ROOT, project.getUser().getLogin(), project.getName());
		String jsonString = requests.get(searchUrl);
		
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		
		List<Milestone> collection = new ArrayList<Milestone>();
		for (int i = 0; i < jsonArray.size(); i++) {
			Milestone milestone = gson.fromJson(jsonArray.get(i), Milestone.class);
			collection.add(milestone);
		}
		
		return collection;
	}
	
	/**
	 * Fetches all the Commits of the given {@link Project} from the GitHub API
	 * @param project the @{link Project} to which the commits belong
	 * @return a {@link List} of {@link Commit} objects
	 * @throws IOException
	 */
	public List<Commit> getAllProjectCommits(Project project) throws IOException {
		List<Commit> collection = new ArrayList<Commit>();
		
		String searchUrl = String.format("%s/repos/%s/%s/commits",
				ROOT, project.getUser().getLogin(), project.getName());
		
		JsonElement jsonElement = gson.fromJson(requests.get(searchUrl), JsonElement.class);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		
		int i = 0;
		Commit commit;
		
		for (; i < jsonArray.size(); i++) {
			commit = gson.fromJson(jsonArray.get(i), Commit.class);
			collection.add(commit);
		}
		
		return collection;
	}
	
	/**
	 * Fetches all the contributors of the given {@link Project} from the GitHub API
	 * @param project the @{link Project} to get the contributors from
	 * @return a {@link List} of {@link User} objects
	 * @throws IOException
	 */
	public List<User> getAllProjectContributors(Project project) throws IOException {
		List<User> collection = new ArrayList<User>();
		
		String searchUrl = String.format("%s/repos/%s/%s/contributors", ROOT, project.getUser().getLogin(), project.getName());
		String jsonString = requests.get(searchUrl);
		
		JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		
		for (int i = 0; i < jsonArray.size(); i++) {
			User contributor = gson.fromJson(jsonArray.get(i), User.class);
			collection.add(contributor);
		}
		
		return collection;
	}
	
	public List<Project> getAllForgeProjects(int start, int limit) throws SearchException{
		String searchUrl  = null;
		List<Project> projects = new ArrayList<Project>();
		try{
			int since = start;
			int totalRepositories = 0;
			while(totalRepositories < limit || limit < 0){
				searchUrl = ROOT + String.format("/repositories?since=%s&language=java&%s", since, this.oauthToken);
				
				String jsonString = requests.get(searchUrl);
				JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
				
				if( jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("message") ){
					throw new GroundhogException( jsonElement.getAsJsonObject().get("message").toString() );
				}
					
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				for (int i = 0; i < jsonArray.size() && 
						(totalRepositories + i < limit || limit < 0); i++) {
					
					String repoName = jsonArray.get(i).getAsJsonObject().get("name").getAsString();					
					String searchUrlLegacy = String.format("%s/legacy/repos/search/%s?language=java&%s", ROOT, repoName, this.oauthToken);
					
					String jsonLegacy = requests.get(searchUrlLegacy);
					
					checkAPIErrorMessage(jsonElement);
					
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
	
	public void checkAPIErrorMessage(JsonElement jsonElement) throws GroundhogException{
		
		if( jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("message") ){
			
			throw new GroundhogException( jsonElement.getAsJsonObject().
					get("message").toString() );
		}
	}
}
