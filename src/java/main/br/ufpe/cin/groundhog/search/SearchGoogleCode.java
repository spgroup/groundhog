package br.ufpe.cin.groundhog.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.inject.Inject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.http.ParamBuilder;
import br.ufpe.cin.groundhog.http.Requests;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;

/**
 * Responsible for performing the project searches on Google Code
 * @author fjsj, gustavopinto, rodrigoalvesvieira
 *
 */
public class SearchGoogleCode implements ForgeSearch {
	private static String root = "http://code.google.com";
	private final Requests requests;
	
	@Inject
	public SearchGoogleCode(Requests requests) {	
		this.requests = requests;
	}
	
	/**
	 * 
	 * @param html the HTML content of the page to be parsed
	 * @return the checkout command within the given HTML page
	 * @throws IOException
	 */
	private String parseCheckoutCommand(String html) throws IOException {
		Document doc = Jsoup.parse(html);
		Elements es = doc.select("#checkoutcmd");
		
		if (es.isEmpty()) {
			return "";
		} else {
			return es.first().text();
		}
	}
	
	private void setCheckoutCommandToProject(String command, Project project) {
		if (command.startsWith("svn")) {
			String url = command.split(" ")[2];
			project.setSCM(SCM.SVN);
			project.setScmURL(url);
		} else if (command.startsWith("git")) {
			String url = command.split(" ")[2];
			project.setSCM(SCM.GIT);
			project.setScmURL(url);
		} else if (command.startsWith("hg")) {
			String url = command.split(" ")[2];
			project.setSCM(SCM.HG);
			project.setScmURL(url);
		} else if (command.equals("")) {
			project.setSCM(SCM.NONE);
		} else {
			project.setSCM(SCM.UNKNOWN);
		}
	}
	
	/**
	 * Performs the search in the Google Code forge
	 */
	public List<Project> getProjects(String term, int page) throws SearchException {
		try {
			List<Project> projects = new ArrayList<Project>();
			String paramsStr =
				new ParamBuilder().
				add("q", term + " label:Java").
				add("start", String.valueOf((page - 1) * 10)).
				build();
			
			Document doc = Jsoup.parse(requests.get(root + "/hosting/search?" + paramsStr));
			for (Element tr : doc.select("#serp table tbody tr")) {
				Element el = tr.child(0).child(0);
				
				String projectName, description, imgSrc, iconURL, sourceCodeUrl;
				
				projectName = el.attr("href").split("/")[2];
				description = tr.child(1).ownText();
				imgSrc = el.child(0).attr("src");
				iconURL = imgSrc;
				
				if (imgSrc.startsWith("/")) {
					iconURL = root + iconURL;
				}
				
				sourceCodeUrl = "https://code.google.com/p/" + projectName + "/source/browse/";
				
				Project forgeProject = new Project(projectName, description, iconURL, sourceCodeUrl);
				projects.add(forgeProject);
			}
			
			// get checkout commands for each project in parallel (asynchronously)
			List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
			for (final Project forgeProject : projects) {
				String projectName = forgeProject.getName();
				String checkoutPageURL = String.format("http://code.google.com/p/%s/source/checkout", projectName);
				Future<Integer> f = requests.getAsync(checkoutPageURL, new AsyncCompletionHandler<Integer>() {
					@Override
					public Integer onCompleted(Response response) throws Exception {
						String command = parseCheckoutCommand(response.getResponseBody());
						setCheckoutCommandToProject(command, forgeProject);
						return response.getStatusCode();
					}
				});
				
				futures.add(f);
			}
			
			// wait for all futures to have all 
			for (Future<Integer> f : futures) {
				f.get();
			}
			return projects;
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}
}