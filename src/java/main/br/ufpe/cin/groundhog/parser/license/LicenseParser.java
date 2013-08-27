package br.ufpe.cin.groundhog.parser.license;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufpe.cin.groundhog.License;
import br.ufpe.cin.groundhog.parser.Parser;
import br.ufpe.cin.groundhog.util.FileUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * This class tries to find which is the current license in use. It raises an
 * exception if no source code is found on the root dir.
 * 
 * IMPORTANT: This class still a work in progress. May lead to misleading results.
 * 
 * @author ghlp
 * @since 0.1.0
 */
public class LicenseParser implements Parser<Set<License>> {

	private static Logger logger = LoggerFactory.getLogger(LicenseParser.class);

	private final File[] files;
	private final File root;
	private final Set<License> licenses;
	private final FileUtil filesUtils = FileUtil.getInstance();

	public LicenseParser(File project) {
		this.files = project.listFiles();
		this.root = project;
		this.licenses = new HashSet<>();
	}

	/**
	 * Parses the top level folder looking for licenses files
	 */
	@Override
	public Set<License> parser() {
		logger.info("Running license parser..");

		for (File file : files) {
			if (filesUtils.isTextFile(file)) {
				String content = filesUtils.readAllLines(file);

				if (containsLicenseWord(content)) {
					extractLicense(content, file.getName());
				}
			}
		}

		if (this.licenses.size() > 0) {
			return this.licenses;
		}

		logger.info(String.format("No license found for project %s.",
				root.getName()));
		return Sets.newHashSet(new License("unlincesed"));
	}

	private void extractLicense(String content, String fileName) {

		boolean isKnownLicense = false;
		for (String license : Licenses.names()) {
			Pattern pattern = Pattern.compile("\\b(" + license +")\\b");
			Matcher matcher = pattern.matcher(content);

			if(matcher.find()) {
				int start = matcher.start();
				int end = matcher.end();
				
				while (start < end) {
					this.licenses.add(new License(matcher.group()));
					isKnownLicense = true;
					start++;
				}
				
				logger.info(String.format("License found in %s file! It uses %s license.", fileName, license));
			}
		}
			
		if(!isKnownLicense) {
			this.licenses.add(new License("not-understandable-license"));				
		}
	}

	private boolean containsLicenseWord(String content) {

		for (String keyword : Lists.newArrayList("license", "copyright")) {
			if (content.toLowerCase().contains(keyword)) {
				return true;
			}
		}
		return false;
	}
}