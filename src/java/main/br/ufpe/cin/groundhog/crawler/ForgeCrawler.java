package br.ufpe.cin.groundhog.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.ufpe.cin.groundhog.Project;

/**
 * An abstract class that defines the forge crawl functionality.
 * A forge crawler downloads projects from a certain forge
 * to a given folder. 
 * @author fjsj, gustavopinto
 *
 */
public abstract class ForgeCrawler {
	
	private ExecutorService ex;
	
	/**
	 * Constructs a new ForgeCrawler with a given destinationFolder.
	 */
	protected ForgeCrawler() {
		this.ex = Executors.newCachedThreadPool();
	}
	
	/**
	 * Downloads a single project and returns its repository folder. A project
	 * repository folder is useful to be manipulated by {@link CodeHistory}
	 * class and to be parsed by {@link Parser} class.
	 * 
	 * @param a project
	 * 
	 * @return a repository folder
	 * @throws DownloadException
	 *             when something nasty happens
	 */
	public abstract File downloadProject(Project project) throws DownloadException;
	
	/**
	 * Downloads the given list of projects, returning the files objects. Each
	 * file represents the repository folder of a project, according to
	 * ForgeProject list ordering. A project repository folder is useful to be
	 * manipulated by {@link CodeHistory} class and to be parsed by Parser class.
	 * 
	 * @param list of Projects
	 * @return list of repository folders as File objects
	 */
	public List<File> downloadProjects(List<Project> projects) {
		List<Future<File>> futures = asyncDownloadProjects(projects);
		
		try {
			List<File> files = new ArrayList<>();
			for (Future<File> future : futures) {
					File f = future.get();
					files.add(f);
			}
			return files;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			throw new DownloadException(e.getLocalizedMessage());
		}
	}

	/**
	 * Downloads the given list of projects, returning futures with files objects.
	 * Each file represents the repository folder of a project,
	 * according to ForgeProject list ordering. To get a future result, 
	 * just call its .get() method. A project repository folder is useful to be
	 * manipulated by {@link CodeHistory} class and to be parsed by {@link Parser} class.
	 * 
	 * @param list of Projects, usually given by a ForgeSearch subclass
	 * @return list of futures with repository folders as File objects
	 */
	public List<Future<File>> asyncDownloadProjects(List<Project> projects) {
		List<Future<File>> fs = new ArrayList<Future<File>>();
		
		for (final Project p : projects) {
			Future<File> f = ex.submit(new Callable<File>() {
				public File call() throws Exception {
					return downloadProject(p);
				}
			});
			fs.add(f);
		}
		
		ex.shutdown();
		
		return fs;
	}
}