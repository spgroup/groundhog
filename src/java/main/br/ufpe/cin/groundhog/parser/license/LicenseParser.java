package br.ufpe.cin.groundhog.parser.license;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufpe.cin.groundhog.License;
import br.ufpe.cin.groundhog.parser.NotAProjectException;
import br.ufpe.cin.groundhog.util.FileUtil;

import com.google.common.collect.Lists;

/**
 * This class tries to find which is the license in use. It raises an exception
 * if no source code if found on the root dir.
 * 
 * This class still a work in progress. May lead to misleading results.
 * 
 * @author ghlp
 * @since 0.1.0
 */
public class LicenseParser {

	private static Logger logger = LoggerFactory.getLogger(LicenseParser.class);

	private final File[] files;
	private final File root;

	public LicenseParser(File project) {
		checkIfIsProject(project);
		this.files = project.listFiles();
		this.root = project;
	}

	private void checkIfIsProject(File project) {
		if (project.listFiles().length == 0) {
			logger.warn(String.format("The project %s does not have source code!"), project.getName());
			throw new NotAProjectException();
		}
	}

	/**
	 * Parses the top level folder looking for licenses files
	 */
	public License parser() {
		logger.info("Running license parser..");

		FileUtil filesUtils = FileUtil.getInstance();

		for (File file : files) {
			if (filesUtils.isTextFile(file)) {
				String content = filesUtils.readAllLines(file);

				if (containsLicenseWord(content)) {
					return extractLicense(content);
				}
			}
		}

		logger.info(String.format("No license found for project %s",
				root.getName()));
		return new License("unlincesed");
	}

	private License extractLicense(String content) {

		for (String license : Licenses.names()) {
			if (content.contains(license)) {
				logger.info(String.format("License found! %s uses %s license.", root.getName(), license));
				return new License(license);
			}
		}
		return new License("not-understandable-license");
	}

	private boolean containsLicenseWord(String content) {

		for (String licenseKeyword : Lists.newArrayList("license", "copyright", "permission")) {
			if (content.toLowerCase().contains(licenseKeyword)) {
				return true;
			}
		}
		return false;
	}
}