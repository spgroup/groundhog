package br.ufpe.cin.groundhog;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ufpe.cin.groundhog.codehistory.GitCodeHistoryTest;
import br.ufpe.cin.groundhog.codehistory.SFCodeHistoryTest;
import br.ufpe.cin.groundhog.crawler.CrawlGitHubTest;
import br.ufpe.cin.groundhog.crawler.CrawlGoogleCodeTest;
import br.ufpe.cin.groundhog.crawler.CrawlSourceForgeTest;
import br.ufpe.cin.groundhog.main.OptionsTest;
import br.ufpe.cin.groundhog.search.SearchGitHubTest;
import br.ufpe.cin.groundhog.search.SearchGoogleCodeTest;
import br.ufpe.cin.groundhog.search.SearchSourceForgeTest;
import br.ufpe.cin.groundhog.util.FileUtilTest;

@RunWith(Suite.class)
@SuiteClasses({ GitCodeHistoryTest.class, SFCodeHistoryTest.class,
		CrawlGitHubTest.class, CrawlGoogleCodeTest.class,
		CrawlSourceForgeTest.class, OptionsTest.class,
		SearchGoogleCodeTest.class, SearchSourceForgeTest.class,
		SearchGitHubTest.class, FileUtilTest.class, EntityTest.class })
public class AllTests {

}