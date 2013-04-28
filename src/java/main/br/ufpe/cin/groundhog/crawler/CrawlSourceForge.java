package br.ufpe.cin.groundhog.crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.extractor.Formats;
import br.ufpe.cin.groundhog.http.HttpModule;
import br.ufpe.cin.groundhog.http.Requests;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;

public class CrawlSourceForge extends ForgeCrawler {
	private static String rootUrl = "http://sourceforge.net";

	private ConcurrentHashMap<String, Date> mapModifiedDate;
	private SimpleDateFormat dateFormat;
	private Requests requests;

	@Inject
	public CrawlSourceForge(Requests requests, File destinationFolder) {
		super(destinationFolder);
		this.mapModifiedDate = new ConcurrentHashMap<String, Date>();
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		this.requests = requests;
	}

	private void parseURLsFromPage(String project, String html,
			Stack<String> dirURLs, List<String> fileURLs) throws IOException,
			InterruptedException {
		Document doc = Jsoup.parse(html);
		Elements rows = doc.select("table#files_list tbody tr");

		for (Element row : rows) {
			String href = row.select("th a").first().attr("href");
			String modified = row.select("td[headers=files_date_h] abbr")
					.first().attr("title");
			String[] splitted = href.split("/");
			String filename = splitted[splitted.length - 2];
			String extension = null;

			if (filename.contains(".")) {
				extension = filename.substring(filename.lastIndexOf("."));
			}
			if (href.endsWith("/download")) { // file
				if (isParseable(filename, extension)) {
					String filePath = requests.decodeURL(href.substring(
							href.indexOf("/files/") + "/files/".length(),
							href.indexOf("/download")));
					try {
						mapModifiedDate.put("/" + project + "/" + filePath,
								dateFormat.parse(modified));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					fileURLs.add(href);
				}
			} else { // folder
				String folderPath = requests.decodeURL(href.substring(
						href.indexOf("/files/") + "/files/".length(),
						href.lastIndexOf('/')));
				try {
					mapModifiedDate.put("/" + project + "/" + folderPath,
							dateFormat.parse(modified));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				dirURLs.push(rootUrl + href);
			}
		}
	}

	private boolean isParseable(String filename, String extension) {
		filename = filename.toLowerCase();
		if (filename.contains("src") || 
				filename.contains("source") && 
				Formats.getInstance().isCompatible(extension)) {
			return true;
		}
		return false;
	}

	private List<String> getDownloadURLs(final String project)
			throws IOException, InterruptedException, ExecutionException {
		final Stack<String> dirURLs = new Stack<String>();
		final List<String> fileURLs = new Vector<String>();
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		dirURLs.add(String.format("http://sourceforge.net/projects/%s/files/",
				project));

		while (!dirURLs.isEmpty()) {
			final String pageURL = dirURLs.pop();
			ListenableFuture<Integer> future = requests.getAsync(pageURL,
					new AsyncCompletionHandler<Integer>() {
						@Override
						public Integer onCompleted(Response r) throws Exception {
							String html = r.getResponseBody();
							parseURLsFromPage(project, html, dirURLs, fileURLs);
							return r.getStatusCode();
						}
					});
			futures.add(future);
			if (dirURLs.isEmpty()) {
				for (Future<Integer> f : futures) {
					f.get();
					if (!dirURLs.isEmpty()) {
						break; // don't need to sync wait other requests
					}
				}
			}
		}
		return fileURLs;
	}

	private void downloadAndSaveFile(String projectName, String url,
			InputStream is, File destination) throws IOException {
		url = requests.decodeURL(url);
		String fileSeparator = File.separator;
		String[] folders = url.substring(
				url.indexOf("/files/") + "/files/".length(),
				url.indexOf("/download")).split("/");
		String filename = folders[folders.length - 1];
		folders = Arrays.copyOf(folders, folders.length - 1);
		String path = destination.getAbsolutePath() + fileSeparator
				+ projectName;
		String relativePath = "/" + projectName;

		File pathF = new File(path);
		if (!pathF.exists()) {
			pathF.mkdir();
		}

		for (String folder : folders) {
			path = path + fileSeparator + folder;
			relativePath = relativePath + "/" + folder;
			File f = new File(path);
			if (!f.exists()) {
				f.mkdir();
			}
		}

		path = path + fileSeparator + filename;
		relativePath = relativePath + "/" + filename;

		File file = new File(path);
		FileOutputStream out = new FileOutputStream(file);
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = is.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.close();

		file.setLastModified(mapModifiedDate.get(relativePath).getTime());
	}

	@Override
	protected File downloadProject(Project project) throws IOException,
			InterruptedException, ExecutionException {
		String projectName = project.getName();
		List<String> urls = getDownloadURLs(projectName);

		for (String url : urls) {
			InputStream is = requests.download(url);
			downloadAndSaveFile(projectName, url, is, destinationFolder);
		}
		return new File(destinationFolder, projectName);
	}

	private void setProjectsDirectoriesDates() {
		Enumeration<String> e = mapModifiedDate.keys();
		for (String relativePath = e.nextElement(); e.hasMoreElements(); relativePath = e
				.nextElement()) {
			Date modifiedDate = mapModifiedDate.get(relativePath);
			File f = new File(destinationFolder, relativePath);
			if (f.exists()) {
				f.setLastModified(modifiedDate.getTime());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		long time = System.nanoTime();
		List<Project> projects = Arrays.asList(new Project("geom-java", ""),
				new Project("im4java", ""));
		File dest = new File("C:\\Users\\fjsj\\Downloads\\EponaProjects\\");
		Injector injector = Guice.createInjector(new HttpModule());
		Requests requests = injector.getInstance(Requests.class);

		CrawlSourceForge crawl = new CrawlSourceForge(requests, dest);
		List<Future<File>> fs = crawl.downloadProjects(projects);
		crawl.shutdown();
		for (Future<File> f : fs)
			f.get();
		crawl.setProjectsDirectoriesDates();
		System.out.printf("Elapsed: %.2f",
				(System.nanoTime() - time) / 1000000000.0);
	}
}