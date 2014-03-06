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

import br.ufpe.cin.groundhog.Issue;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.http.ParamBuilder;
import br.ufpe.cin.groundhog.http.Requests;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;

/**
 * Responsible for performing the project searches on Google Code
 * @author fjsj, gustavopinto, Rodrigo Alves
 * @deprecated
 */
public class SearchGoogleCode implements ForgeSearch {
	private static String root = "http://code.google.com";
	private final Requests requests;

	@Inject
	public SearchGoogleCode(Requests requests) {
		this.requests = requests;
	}

	/**
	 * Fetches and returns the checkout command String for the project
	 * @param html the HTML content of the page to be parsed
	 * @return the checkout command within the given HTML page
	 */
	private String parseCheckoutCommand(String html) {
		Document doc = Jsoup.parse(html);
		Elements es = doc.select("#checkoutcmd");

		return es.isEmpty() ? "" : es.first().text();
	}

	/**
	 * Sets the checkout command of the project according to its SCM tool
	 * @param command the checkout command String
	 * @param project the project to which the checkout must be applied
	 */
	private void setCheckoutCommandToProject(String command, Project project) {
		String url = command.split(" ")[2];
		project.setCheckoutURL(url);

		if (command.startsWith("svn")) {
			project.setSCM(SCM.SVN);
		} else if (command.startsWith("git")) {
			project.setSCM(SCM.GIT);
		} else if (command.startsWith("hg")) {
			project.setSCM(SCM.HG);
		} else if (command.equals("")) {
			project.setSCM(SCM.NONE);
		} else {
			project.setSCM(SCM.UNKNOWN);
		}
	}

	public List<Project> getProjects(String term, int page, int limit) throws SearchException {
		if( term == null){
			return getAllForgeProjects(page, limit);
		}
		try {
			List<Project> projects = new ArrayList<Project>();
			String params = new ParamBuilder()
									.add("q", term + " label:Java")
									.add("start", String.valueOf((page - 1) * 10))
									.build();

			Document doc = Jsoup.parse(requests.get(root + "/hosting/search?" + params));
			int cont = 0;
			for (Element tr : doc.select("#serp table tbody tr")) {
				if ( cont >= limit && limit >=0 ) break;

				Element el = tr.child(0).child(0);

				// The span element within the main search result that contains the number
				// of people watching the project on Google Code
				Element span = tr.child(1).child(2).child(0);

				String projectName = el.attr("href").split("/")[2];
				String description = tr.child(1).ownText();

				String sourceCodeUrl = "https://code.google.com/p/" + projectName + "/source/browse/";

				Project forgeProject = new Project(projectName, description, sourceCodeUrl);

				int stars = Integer.parseInt(span.text());
				forgeProject.setWatchersCount(stars);
				projects.add(forgeProject);
				cont++;
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

	@Override
	public List<Project> getProjects(String term, String username, int page)
			throws SearchException {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public List<Project> getAllForgeProjects(int start, int limit)
			throws SearchException {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public List<Issue> getAllProjectIssues(Project gr) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}