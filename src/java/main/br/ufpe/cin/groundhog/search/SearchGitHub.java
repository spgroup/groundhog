package br.ufpe.cin.groundhog.search;

import static br.ufpe.cin.groundhog.http.URLsDecoder.encodeURL;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufpe.cin.groundhog.Commit;
import br.ufpe.cin.groundhog.GroundhogException;
import br.ufpe.cin.groundhog.Issue;
import br.ufpe.cin.groundhog.Language;
import br.ufpe.cin.groundhog.Milestone;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.Release;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.User;
import br.ufpe.cin.groundhog.http.HttpModule;
import br.ufpe.cin.groundhog.http.Requests;
import br.ufpe.cin.groundhog.search.UrlBuilder.GithubAPI;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.ning.http.client.Response;

/**
 * Performs the project search on GitHub, via its official JSON API
 * 
 * @author fjsj, gustavopinto, Rodrigo Alves, Jesus Jackson
 * @since 0.0.1
 */
public class SearchGitHub implements ForgeSearch {

	private static Logger logger = LoggerFactory.getLogger(SearchGitHub.class);

	/*
	 *  Default number of items per page as defined in http://developer.github.com/v3/#pagination
	 */
	@SuppressWarnings("unused")
	private static final int DEFAULT_PAGINATION_LIMIT = 30;

	/*
	 * Pattern for a Github Link header
	 */
	private static final Pattern LINK_PATTERN = Pattern.compile("\\<(?<link>\\S+)\\>\\; rel=\\\"(?<rel>\\S+)\\\"");

	public static int INFINITY = -1;

	private final Gson gson;
	private final Requests requests;
	private final UrlBuilder builder;

	@Inject
	public SearchGitHub(Requests requests) {
		this.requests = requests;
		this.gson = new Gson();
		this.builder = Guice.createInjector(new HttpModule()).getInstance(UrlBuilder.class);
	}

	public List<Project> getProjects(String term, int page, int limit) throws SearchException {
		logger.info("Searching project metadata");
		try {
			if (term == null) {
				return getAllForgeProjects(page, limit);
			}

			String searchUrl = builder.uses(GithubAPI.LEGACY_V2)
									  .withParam(encodeURL(term))
									  .withParam("start_page", page)
									  .withParam("language", "java")
									  .build();

			String json = requests.get(searchUrl);
			JsonObject jsonObject = gson.fromJson(json, JsonElement.class).getAsJsonObject();			
			JsonArray jsonArray = jsonObject.get("repositories").getAsJsonArray();

			List<Project> projects = new ArrayList<>();
			for (int i = 0; i < jsonArray.size() && (i < limit || limit < 0); i++) {
				String element = jsonArray.get(i).toString();

				Project p = gson.fromJson(element, Project.class);
				p.setSCM(SCM.GIT);

				String owner = jsonArray.get(i).getAsJsonObject().get("owner").getAsString();
				p.setScmURL(String.format("https://github.com/%s/%s.git", owner, p.getName()));

				String userUrl = builder.uses(GithubAPI.USERS).withParam(owner).build();
				String userJson = requests.get(userUrl);
				User user = gson.fromJson(userJson, User.class);

				p.setOwner(user);
				projects.add(p);
			}

			return projects;

		} catch (GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}

	/**
	 * Obtains from the GitHub API the set of projects with more than one language
	 * 
	 * @param Start indicates the desired page
	 * @param limit the total of projects that will be returned
	 * @throws SearchException
	 */
	public List<Project> getProjectsWithMoreThanOneLanguage(int limit) throws SearchException {
		
		try {
			logger.info("Searching project with more than one language metadata");
			
			List<Project> rawData = getAllProjects(0, limit);

			List<Project> projects = new ArrayList<>();
			for (Project project : rawData) {
				List<Language> languages = getProjectLanguages(project);

				if (languages.size() > 1) {
					projects.add(project);
				}
			}

			return projects;

		} catch (GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}

	/**
	 * Obtains from the GitHub API the set of projects within a specific
	 * language
	 * 
	 * @param lang the specific language
	 */
	public List<Project> getAllProjectsByLanguage(String lang) throws SearchException {

		logger.info("Searching all project by language metadata");
		
		String searchUrl = builder.uses(GithubAPI.LEGACY_V2).withSimpleParam("language=", lang).build();
		String json = requests.get(searchUrl);

		JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
		JsonArray jsonArray= jsonObject.get("repositories").getAsJsonArray();

		List<Project> projects = new ArrayList<>();
		for (JsonElement element : jsonArray) {
			Project p = gson.fromJson(element, Project.class);
			String owner = element.getAsJsonObject().get("owner").getAsString();
			p.setUser(new User(owner));
			projects.add(p);
		}

		return projects;
	}

	/**
	 * Obtains from the GitHub API the set of projects
	 * 
	 * @param Start indicates the desired page
	 * @param limit is the total of projects that are going to me returned 
	 * @throws SearchException
	 */
	public List<Project> getAllProjects(int start, int limit) throws SearchException {

		try {

			int since = start;
			int totalRepositories = 0;

			List<Project> projects = new ArrayList<>();
			JsonParser parser = new JsonParser();
			while(totalRepositories < limit || limit < 0){

				String searchUrl = builder.uses(GithubAPI.REPOSITORIES)
										  .withParam("language", "java")
										  .withParam("since", since)
										  .build();
				
				String response = getWithProtection(searchUrl);
				JsonArray jsonArray = parser.parse(response).getAsJsonArray();

				int counter = 0;

				for (Iterator<JsonElement> iterator = jsonArray.iterator(); (iterator
						.hasNext() && (totalRepositories + counter < limit || limit < 0));) {

					JsonElement element = (JsonElement) iterator.next();

					String repoName = element.getAsJsonObject().get("name").getAsString();	
					String searchUrlLegacy = builder.uses(GithubAPI.LEGACY_V2).withParam(repoName).build();
					
					String jsonLegacy = getWithProtection(searchUrlLegacy);
					JsonElement jsonElement = parser.parse(jsonLegacy);
					JsonObject jsonObject = jsonElement.getAsJsonObject();
					JsonArray jsonArrayLegacy = jsonObject.get("repositories").getAsJsonArray();

					if(jsonArrayLegacy.size() > 0) {
						JsonObject rawJsonObject = jsonArrayLegacy.get(0).getAsJsonObject();

						String stringElement = rawJsonObject.toString();
						Project p = gson.fromJson(stringElement, Project.class);

						p.setSCM(SCM.GIT);
						String owner = rawJsonObject.getAsJsonObject().get("owner").getAsString();
						p.setScmURL(String.format("https://github.com/%s/%s.git", owner, p.getName()));

						User user = new User(owner);
						p.setOwner(user);
						projects.add(p);

						counter++;
						totalRepositories++;
					}
				}

				JsonElement lastPagesRepository = jsonArray.get(jsonArray.size() -1);
				since = lastPagesRepository.getAsJsonObject().get("id").getAsInt();
			}
			return projects;

		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}

	@Override
	public List<Project> getProjects(String term, String username, int page) throws SearchException {
		try {
			logger.info("Searching project metadata by term");
			
			List<Project> projects = new ArrayList<>();
			
			String searchUrl = builder.uses(GithubAPI.LEGACY_V2)
					  .withParam(encodeURL(term))
					  .withParam("start_page", page)
					  .withParam("language", "java")
					  .build();

			JsonParser parser = new JsonParser();
			String jsonLegacy = getWithProtection(searchUrl);
			JsonElement jsonElement = parser.parse(jsonLegacy);
			
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			JsonArray jsonArray = jsonObject.get("repositories").getAsJsonArray();
			
			for (JsonElement j: jsonArray) {
				Project p = gson.fromJson(j, Project.class);
				JsonObject jsonObj = j.getAsJsonObject();	

				p.setSCM(SCM.GIT);
				p.setScmURL(String.format("git@github.com:%s/%s.git", username, p.getName()));
				p.setSourceCodeURL(jsonObj.get("url").getAsString());
				
				User u = new User(jsonObj.get("owner").getAsString());
				p.setUser(u);
				
				projects.add(p);
			}

			return projects;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}

	/**
	 * Obtains from the GitHub API the set of languages that compose a
	 * {@link Project}
	 * 
	 * @param project
	 *            a {@link Project} object to have its languages fetched
	 */
	public List<Language> getProjectLanguages(Project project) {

	  String searchUrl = builder.uses(GithubAPI.ROOT)
			  .withParam("repos")
			  .withSimpleParam("/", project.getUser().getLogin())
			  .withSimpleParam("/", project.getName())
			  .withParam("/languages")
			  .build();

		String json = requests.get(searchUrl).replace("{", "").replace("}", "");

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

	/**
	 * Fetches all the Issues (open and closed) of the given {@link Project} from the GitHub API
	 * 
	 * @param project the @{link Project} of which the Issues are about
	 * @return a {@link List} of {@link Issues} objects
	 */
	public List<Issue> getAllProjectIssues(Project project) {
		//XXX: Instead fetching ALL issues at once we could return a lazy collection that would
		// do it on the fly
		try {
			return getProjectIssuesInner(project);
		} catch (JsonSyntaxException | IOException e) {
			throw new SearchException(e);
		}
	}

	private List<Issue> getProjectIssuesInner(Project project) throws JsonSyntaxException, IOException {
		logger.info("Searching project issues metadata");
		List<Issue> issues = getAllProjectIssuesForState(project, "open");
		issues.addAll(getAllProjectIssuesForState(project, "closed"));
		return issues;
	}

	private List<Issue> getAllProjectIssuesForState(Project project,
			String state) throws IOException {
		List<Issue> issues = new ArrayList<Issue>();
		String searchUrl = buildListIssuesUrl(project.getUser().getLogin(), project.getName(), 1, state);
		Response response = getResponseWithProtection(searchUrl);
		String jsonResponse = response.getResponseBody();
		extractIssues(project, jsonResponse, issues);
		while((searchUrl = getNextUrl(response)) != null){
			response = getResponseWithProtection(searchUrl);
			jsonResponse = response.getResponseBody();
			extractIssues(project, jsonResponse, issues);
		}
		return issues;
	}

	private void extractIssues(Project project, String jsonResponse,
			List<Issue> issues) {
		JsonArray jsonArray = gson.fromJson(jsonResponse, JsonElement.class).getAsJsonArray();
		for (JsonElement element : jsonArray) {
			Issue issue = gson.fromJson(element, Issue.class);
			issue.setProject(project);
			issues.add(issue);
		}
	}

	private String buildListIssuesUrl(String user, String project, int page, String state){
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", user)
				  .withSimpleParam("/", project)
				  .withSimpleParam("/", "issues")
				  .withParam("state", state)
				  .withParam("page", "" + page)
				  .build();
		return searchUrl;
	}

	private String getNextUrl(Response response) {
		String nextLink = null;
		String linkHeader = response.getHeader("Link");
		if(linkHeader != null){
			Map<String, String> relToLinks = getLinks(linkHeader);
			if(relToLinks.containsKey("next")){
				nextLink = relToLinks.get("next");
			}
		}
		return nextLink;
	}

	/*
	 * Extract the links out of a Link header
	 */
	private Map<String, String> getLinks(String linkHeader) {
		Map<String, String> relToLinks = new HashMap<>();
		Matcher linkMatcher = LINK_PATTERN.matcher(linkHeader);
		while(linkMatcher.find()){
			String rel = linkMatcher.group("rel");
			String link = linkMatcher.group("link");
			if(!relToLinks.containsKey(rel)) relToLinks.put(rel, link);
			else {
				logger.warn("Duplicate rel, previous link " + relToLinks.get(rel) +
							" , new link " + link);
			}
		}
		return relToLinks;
	}

	/**
	 * Fetches all the Milestones of the given {@link Project} from the GitHub API
	 * @param project the @{link Project} of which the Milestones are about
	 * @return a {@link List} of {@link Milestone} objects
	 */
	public List<Milestone> getAllProjectMilestones(Project project) {

		logger.info("Searching project milestones metadata");
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getUser().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("milestones")
				  .build();

		String jsonString = requests.get(searchUrl);

		JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

		List<Milestone> milestones = new ArrayList<>();
		for (JsonElement element : jsonArray) {
			Milestone milestone = gson.fromJson(element, Milestone.class);
			milestone.setProject(project);
			
			milestones.add(milestone);
		}

		return milestones;
	}
	
	/**
	 * Fetches all the Tags of the given {@link Project} from the GitHub API
	 * @param project the @{link Project} of which the Milestones are about
	 * @return a the size of the {@link List} of {@link Tags} objects
	 */
	public int getNumberProjectTags(Project project) {

		logger.info("Searching project tags metadata");
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getUser().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/git/refs/tags")
				  .build();

		String jsonString = getWithProtection(searchUrl);

		int retorno = 0;
		
		if(!jsonString.contains("Not Found") && !jsonString.contains("Git Repository is empty.")){

			try {
				JsonElement element =gson.fromJson(jsonString, JsonElement.class);
				
				if(element.isJsonArray()){
					
					JsonArray jsonArray = element.getAsJsonArray();
					
					retorno = jsonArray.size();
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Causado por " + jsonString);
			}	
		}
		
		return retorno;
	}

	/**
	 * Returns the commit with the given SHA <code>sha</code> for <code>project</code>
	 * @param project
	 * @param sha
	 * @return
	 */
	public Commit getProjectCommit(Project project, String sha) {
		checkNotNull(project);
		checkNotNull(sha);
		Commit commit = null;
		String searchUrl = buildCommitSearchUrl(project, sha);
		String responseBody = getWithProtection(searchUrl);
		commit = gson.fromJson(responseBody, Commit.class);
		return commit;
	}

	private String buildCommitSearchUrl(Project project, String sha) {
		String searchUrl;
		searchUrl = builder.uses(GithubAPI.ROOT)
				.withParam("repos")
				.withSimpleParam("/", project.getUser().getLogin())
				.withSimpleParam("/", project.getName())
				.withParam("/commits")
				.withSimpleParam("/", sha)
				.build();
		return searchUrl;
	}

	/**
	 * Returns the commit pointed by <code>refSpec</code> for Project <code>project</code>.
	 * @param project
	 * @return
	 */
	public Commit getProjectCommitByRef(Project project, String refSpec){
		checkNotNull(project);
		checkNotNull(refSpec);
		Commit head = null;
		String refSHA = getRefSHA(project, refSpec);
		if(refSHA != null) {
			head = getProjectCommit(project, refSHA);
		}
		return head;
	}

	private String getRefSHA(Project project, String refName) {
		String sha = null;
		String searchUrl = buildRefSearchUrl(project, refName);
		String responseBody = getWithProtection(searchUrl);
		JsonElement ref = gson.fromJson(responseBody, JsonElement.class);
		JsonElement object = ref.getAsJsonObject().get("object");
		sha = object.getAsJsonObject().get("sha").getAsString();
		return sha;
	}

	private String buildRefSearchUrl(Project project, String refName) {
		return builder.uses(GithubAPI.ROOT).
								withParam("repos").
								withSimpleParam("/", project.getUser().getLogin()).
								withSimpleParam("/", project.getName()).
								withSimpleParam("/", "git").
								withSimpleParam("/", "refs").
								withSimpleParam("/", refName).build();
	}

	/**
	 * Fetches all the Commits of the given {@link Project} from the GitHub API for the specified
	 * period. If either since or until are <code>null</code> the period is open-ended, if both are
	 * <code>null</code> then this method behaves exactly like {@link #getAllProjectCommits(Project)}
	 * @param project project to fetch commits metadata for
	 * @param since initial date of the period in the ISO 8601 format YYYY-MM-DDTHH:MM:SSZ
	 * @param until end date of the period in the ISO 8601 format YYYY-MM-DDTHH:MM:SSZ
	 * @return the list of commits from project <code>project</code> within the specified period,
	 * or an empty list if no such commits exist
	 */
	public List<Commit> getProjectCommitsByPeriod(Project project, String since, String until){
		checkNotNull(project, "project must not be null");
		logger.info("Searching project commits metadata by period");
		try {
			return getProjectCommits(project, since, until);
		} catch (IOException e) {
			throw new SearchException(e);
		}
	}

	/**
	 * Utility method for <code>getAllProjectCommitsByPeriod(project, since, null)</code>
	 * @param project project to fetch commits metadata for
	 * @param since initial date of the period in the ISO 8601 format YYYY-MM-DDTHH:MM:SSZ
	 * @return the list of commits from <code>project</code> within the specified period,
	 * or an empty list if no such commits exist
	 */
	public List<Commit> getProjectCommitsSince(Project project, String since){
		return getProjectCommitsByPeriod(project, since, null);
	}

	/**
	 * Utility method for <code>getAllProjectCommitsByPeriod(project, null, until)</code>
	 * @param project project to fetch commits metadata for
	 * @param until end date of the period in the ISO 8601 format YYYY-MM-DDTHH:MM:SSZ
	 * @return the list of commits from project <code>project</code> within the specified period,
	 * or an empty list if no such commits exist
	 */
	public List<Commit> getProjectCommitsUntil(Project project, String until){
		return getProjectCommitsByPeriod(project, null, until);
	}

	/**
	 * Fetches all the Commits of the given {@link Project} from the GitHub API
	 * @param project the @{link Project} to which the commits belong
	 * @return a {@link List} of {@link Commit} objects
	 */
	public List<Commit> getAllProjectCommits(Project project) {
		checkNotNull(project, "project must not be null");
		logger.info("Searching all project commits metadata");
		try {
			return getProjectCommits(project, null, null);
		} catch (IOException e) {
			throw new SearchException(e);
		}
	}

	private List<Commit> getProjectCommits(Project project, String since, String until) throws IOException {
		List<Commit> commits = new ArrayList<>();
		int page = 1;
		String searchUrl = buildListCommitsUrl(project, since, until);
		logger.info("getting commits for page " + page);
		Response response = getResponseWithProtection(searchUrl);
		extractCommits(project, response, commits);
		
		while((searchUrl = getNextUrl(response)) != null){
			logger.info("getting commits for page " + ++page);
			response = getResponseWithProtection(searchUrl);
			extractCommits(project, response, commits);
		}

		return commits;
	}

	private String buildListCommitsUrl(Project project, String since,
			String until) {
		UrlBuilder listCommitsBuilder = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getUser().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withSimpleParam("/", "commits");
		
		if(since != null){
			listCommitsBuilder = listCommitsBuilder.withParam("since", since);
		}

		if(until != null) {
			listCommitsBuilder = listCommitsBuilder.withParam("until", until);
		}

		String searchUrl = listCommitsBuilder.build();
		return searchUrl;
	}

	private void extractCommits(Project project, Response response,
			List<Commit> commits) throws IOException {
		String jsonResponse = response.getResponseBody();
		JsonElement jsonElement = gson.fromJson(jsonResponse, JsonElement.class);
		JsonArray jsonArray = jsonElement.getAsJsonArray();

		for (JsonElement element : jsonArray) {
			Commit commit = gson.fromJson(element, Commit.class);
			commit = getProjectCommit(project, commit.getSha());
			commit.setProject(project);
			
			User user = gson.fromJson(element.getAsJsonObject().get("committer"), User.class);
			commit.setCommiter(user);
			
			commit.setMessage(element.getAsJsonObject().get("commit").getAsJsonObject().get("message").getAsString());

			String date = element.getAsJsonObject().get("commit").getAsJsonObject().get("author").getAsJsonObject().get("date").getAsString();
			commit.setCommitDate(date);

			commits.add(commit);
		}
	}

	/**
	 * Fetches all the contributors of the given {@link Project} from the GitHub API
	 * @param project the @{link Project} to get the contributors from
	 * @return a {@link List} of {@link User} objects
	 */
	public List<User> getAllProjectContributors(Project project) {
		logger.info("Searching project contributors metadata");
		
		List<User> users = new ArrayList<>();

		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getUser().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/contributors")
				  .build();
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
        JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
        
		for (JsonElement element: jsonArray) {
        	User user = gson.fromJson(element, User.class);
            users.add(user);
        }

		return users;
	}

	public List<Project> getAllForgeProjects(int start, int limit) throws SearchException{
		try{
			logger.info("Searching all projects metadata");
			
			int since = start;
			int totalRepositories = 0;
			List<Project> projects = new ArrayList<>();
			
			while (totalRepositories < limit || limit < 0) {

				String searchUrl = builder.uses(GithubAPI.REPOSITORIES)
										  .withParam("since", since)
										  .withParam("language", "java")
										  .build();
				
				String jsonString = requests.get(searchUrl);
				JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();
				for (int i = 0; i < jsonArray.size() && 
						(totalRepositories + i < limit || limit < 0); i++) {

					String repoName = jsonArray.get(i).getAsJsonObject().get("name").getAsString();					
					String searchUrlLegacy = builder.uses(GithubAPI.LEGACY_V2)
													  .withParam(repoName)
													  .withParam("language", "java")
													  .build();
					
					String jsonLegacy = requests.get(searchUrlLegacy);

					JsonObject jsonObject = gson.fromJson(jsonLegacy, JsonElement.class).getAsJsonObject();			
					JsonArray jsonArrayLegacy = jsonObject.get("repositories").getAsJsonArray();

					if (jsonArrayLegacy.size() > 0) {
						JsonObject rawJsonObject = jsonArrayLegacy.get(0).getAsJsonObject();
						String stringElement = rawJsonObject.toString(); // verify here
						
						Project p = gson.fromJson(stringElement, Project.class);

						p.setSCM(SCM.GIT);
						String owner = rawJsonObject.getAsJsonObject().get("owner").getAsString();
						p.setScmURL(String.format("https://github.com/%s/%s.git", owner, p.getName()));

						projects.add(p);
						totalRepositories++;
					}
				}
				
				JsonElement lastPagesRepository = jsonArray.get(jsonArray.size() -1);
				since = lastPagesRepository.getAsJsonObject().get("id").getAsInt();
			}
			return projects;
		} catch (GroundhogException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}
	
	/**
	 * Gets the top most Used languages among the projects considering the most used language
	 * in each project. 
	 * @param projects List of projects into consideration
	 * @param limit Limits the size of the returning list
	 * @return sorted list with the top most used languages
	 */
	public List<Language> getTopMostUsedLanguages(List<Project> projects, int limit){
		List<Language> topLanguages = new ArrayList<Language>();
		HashMap<String, Integer> LanguageMap = new HashMap<String, Integer>(); 
		for (Project project: projects) {			
			String language = project.getLanguage();
			Integer count = 1;
			
			if (LanguageMap.containsKey(language)){ 
				count += LanguageMap.get(language);
			}						   
			
			LanguageMap.put(language, count);

		}
		for (Entry<String, Integer> language : LanguageMap.entrySet()) {
			topLanguages.add(new Language(language.getKey(), language.getValue()));
		}

		Collections.sort(topLanguages);
		if(limit < 0) limit = 0;
		
		topLanguages = topLanguages.subList(0, Math.min(limit, topLanguages.size()));
		return topLanguages;		
	}


	/**
	 * Gets the top most used languages among the projects according to the number 
	 * of LOC (lines of code) that they appear. 
	 * @param projects {@link List} of projects into consideration
	 * @param limit for limiting (upper bound) the size of the returning list
	 * @return sorted list with the top most used languages
	 */
	public List<Language> getTopMostUsedLanguagesLoc(List<Project> projects, int limit){
		List<Language> topLanguages = new ArrayList<Language>();
		HashMap<String, Integer> LanguageMap = new HashMap<String, Integer>(); 
		
		for (Project project: projects){
			if (project.getLanguages() == null) {
				throw new GroundhogException("languages information required");
			}
			
			for (Language language : project.getLanguages()) {
				Integer newLoc = language.getByteCount();
				
				if (LanguageMap.containsKey(language.getName())) {
					newLoc += LanguageMap.get(language.getName());
				}
				
				LanguageMap.put(language.getName(), newLoc);
			}
		}
		
		for (Entry<String, Integer> language : LanguageMap.entrySet()) {
			topLanguages.add(new Language(language.getKey(), language.getValue()));
		}

		Collections.sort(topLanguages);
		if (limit < 0 ) limit = 0;
		
		topLanguages = topLanguages.subList(0, Math.min(limit, topLanguages.size()));
		return topLanguages;	
	}
	
	/**
	 * Fetches all the Releases of the given {@link Project} from the GitHub API
	 * 
	 * @param project the @{link Project} of which the Releases are about
	 * @return a {@link List} of {@link Release} objects
	 */
	public List<Release> getAllProjectReleases(Project project) {

		logger.info("Searching project releases metadata");
		
		String searchUrl = builder.uses(GithubAPI.ROOT)
				  .withParam("repos")
				  .withSimpleParam("/", project.getUser().getLogin())
				  .withSimpleParam("/", project.getName())
				  .withParam("/releases")
				  .build();
		
		String jsonString = requests.getWithPreviewHeader(searchUrl);
		JsonArray jsonArray = gson.fromJson(jsonString, JsonElement.class).getAsJsonArray();

		List<Release> releases = new ArrayList<Release>();
		for (JsonElement element : jsonArray) {
			Release release = gson.fromJson(element, Release.class);
			release.setProject(project);
			
			releases.add(release);
		}

		return releases;
	
	}

	private String getWithProtection(String url){
		try {
			return getResponseWithProtection(url).getResponseBody();
		} catch (IOException e) {
			// This should never happen, but let's be safe
			throw new SearchException(e);
		}
	}

	private Response getResponseWithProtection(String url){
		Response response = requests.getResponse(url);
		String data;
		try {
			int statusCode = response.getStatusCode();
			
			data = response.getResponseBody();
			// 403 == forbidden
			if(statusCode == 403 &&  data != null && data.contains("API rate limit exceeded")) {
				try {
					logger.info("API rate limit exceeded, waiting for " + (60 * 60) + " seconds");
					Thread.sleep(1000 * 60 * 60);
					data = requests.get(url);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		} catch (IOException e) {
			throw new SearchException(e);
		}
		return response;
	}
}
