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

/**
 * This class tries to find which is the license in use. It raises an exception
 * if no source code if found on the root dir.
 * 
 * This class still a work in progress. May lead to misleading results.
 * 
 * @author ghlp
 * @since 0.1.0
 */
public class LicenseParser implements Parser<License> {

	private static Logger logger = LoggerFactory.getLogger(LicenseParser.class);

	private final File[] files;
	private final File root;
	private final Set<String> licenses;

	public LicenseParser(File project) {
		this.files = project.listFiles();
		this.root = project;
		this.licenses = new HashSet<>();
	}

	/**
	 * Parses the top level folder looking for licenses files
	 */
	@Override
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

		logger.info(String.format("No license found for project %s", root.getName()));
		return new License("unlincesed");
	}

	private License extractLicense(String content) {

		for (String license : Licenses.names()) {
			Pattern pattern = Pattern.compile("\\b(" + license +")\\b");
			Matcher matcher = pattern.matcher(content);
			if(matcher.find()) {
				int start = matcher.start();
				int end = matcher.end();
				
				while (start < end) {
					this.licenses.add(matcher.group());
					start++;
				}
				
				logger.info(String.format("License found! %s uses %s license.", root.getName(), licenses));
				return new License(license);
			}
		}
		return new License("not-understandable-license");
	}

	private boolean containsLicenseWord(String content) {
		for (String licenseKeyword : Lists.newArrayList("license", "copyright")) {
			if (content.toLowerCase().contains(licenseKeyword)) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		String content = "The MIT License (MIT)  Copyright (c) [year] [fullname]  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the Software), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.  THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.";
		new LicenseParser(new File(".")).extractLicense(content);
	}
}