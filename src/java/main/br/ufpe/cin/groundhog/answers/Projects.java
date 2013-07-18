package br.ufpe.cin.groundhog.answers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import br.ufpe.cin.groundhog.GroundhogException;
import br.ufpe.cin.groundhog.Language;
import br.ufpe.cin.groundhog.Project;

/**
 * Utility class to help answer the milestone questions
 * 
 * TODO: We should have a better place to put this class
 * 
 * @author gustavopinto
 */
public class Projects {

	/**
	 * Method to inform the median number of forks per project in a collection of projects
	 * @param projects
	 * @return
	 */
	public double getMedianForksRate(List<Project> projects) {
		int i = 0, listSize = projects.size(), half = listSize/2;
		double median = 0.0;
		int[] forkStore = new int[listSize];

		if (listSize == 0) {
		    throw new IllegalArgumentException("List of projects can't be empty");
		}

		for (; i < listSize; i++) {
			forkStore[i] = projects.get(i).getForksCount();
		}

		Arrays.sort(forkStore);

		if (listSize % 2 == 0) {
			median = (forkStore[half - 1] + forkStore[half])/2;
		} else {
			median = forkStore[half];
		}

		return median;
	}

	/**
	 * Method to inform the average number of forks per project in a collection of projects
	 * @param projects
	 * @return
	 */
	public static int getAverageForksRate(List<Project> projects) {
		int i = 0, j = 0, total = 0, listSize = projects.size();
		int[] forkStore = new int[listSize];

		if (listSize == 0) {
		    throw new IllegalArgumentException("List of projects can't be empty");
		}

		for (; i < listSize; i++) {
			forkStore[i] = projects.get(i).getForksCount();
		}

		for (; j < forkStore.length; j++) {
			total += forkStore[j];
		}

		return total/listSize;
	}

	/**
	 * Method to discover the percentage of projects that have forks
	 * @param The list of projects to be analyzed
	 * @return a double result - such that 0 <= result <= 1 - indicating how many of the informed projects have forks (were forked at least once)
	 */
	public static double getProjectsWithForksRate(List<Project> projects) {
		double result = 0.0;
		int projectsWithForks = 0, i = 0, listSize = projects.size();

		if (listSize == 0) {
		    throw new IllegalArgumentException("List of projects can't be empty");
		}

		for (; i < listSize; i++) {
			if (projects.get(i).getForksCount() > 0) {
				projectsWithForks++;
			}
		}

		result = (projectsWithForks / listSize);
		return result;
	}

	/**
	 * Informs what is the overall percentage of the given projects that are forks
	 * With this method we can answer the question "What is the overall percentage of Github projects that ARE forks?"
	 * @return a double result - such that 0 <= result <= 1 - indicating how many of the informed projects are forks
	 */
	public static double getProjectsThatAreForks(List<Project> projects) {
		double result = 0.0;
		int projectsAreForks = 0, i = 0, listSize = projects.size();

		if (listSize == 0) {
		    throw new IllegalArgumentException("List of projects can't be empty");
		}

		for (; i < listSize; i++) {
			if (projects.get(i).isFork()) {
				projectsAreForks++;
			}
		}

		result = (projectsAreForks / listSize);
		return result;
	}
	
	
	/**
	 * Gets the top most Used languages among the projects considering the most used language
	 * in each project. 
	 * @param projects List of projects into consideration
	 * @param limit Limits the size of the returning list
	 * @return sorted list with the top most used languages
	 */
	public static List<Language> getTopMostUsedLanguages(List<Project> projects, int limit){
		List<Language> topLanguages = new ArrayList<Language>();
		HashMap<String, Integer> LanguageMap = new HashMap<String, Integer>(); 
		for( Project project: projects){			
			String language = project.getLanguage();
			Integer count = 1;
			if ( LanguageMap.containsKey(language) ){ 
				count += LanguageMap.get(language);
			}						   
			LanguageMap.put(language, count);
			
		}
		for( Entry<String, Integer> language : LanguageMap.entrySet() ){
			topLanguages.add(new Language(language.getKey(), language.getValue()));
		}
		
		Collections.sort(topLanguages);
		if( limit < 0 ) limit = 0;
		topLanguages = topLanguages.subList(0, Math.min( limit, topLanguages.size() ));
		return topLanguages;		
	}
	

	/**
	 * Gets the top most Used languages among the projects according to the number 
	 * of LOC (lines of code) that they apper. 
	 * @param projects List of projects into consideration
	 * @param limit Limits the size of the returning list
	 * @return sorted list with the top most used languages
	 */
	public static List<Language> getTopMostUsedLanguagesLoc(List<Project> projects, int limit){
		List<Language> topLanguages = new ArrayList<Language>();
		HashMap<String, Integer> LanguageMap = new HashMap<String, Integer>(); 
		for( Project project: projects){
			if( project.getLanguages() == null) {
				throw new GroundhogException("languages information required");
			}
			for( Language language : project.getLanguages()){
				Integer newLoc = language.getLoc();
				if ( LanguageMap.containsKey(language.getName()) ){ 
					newLoc += LanguageMap.get(language.getName());
				}						   
				LanguageMap.put(language.getName(), newLoc);
			}
		}
		for( Entry<String, Integer> language : LanguageMap.entrySet() ){
			topLanguages.add(new Language(language.getKey(), language.getValue()));
		}
		
		Collections.sort(topLanguages);
		if( limit < 0 ) limit = 0;
		topLanguages = topLanguages.subList(0, Math.min( limit, topLanguages.size() ));
		return topLanguages;		
	}
	
}
