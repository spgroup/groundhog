package br.ufpe.cin.groundhog;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ufpe.cin.groundhog.codehistory.GitCodeHistoryTest;
import br.ufpe.cin.groundhog.codehistory.SFCodeHistoryTest;
import br.ufpe.cin.groundhog.crawler.CrawlGitHubTest;
import br.ufpe.cin.groundhog.search.SearchGitHubTest;
import br.ufpe.cin.groundhog.search.SearchGoogleCodeTest;
import br.ufpe.cin.groundhog.search.SearchSourceForgeTest;

@RunWith(Suite.class)
@SuiteClasses({ GitCodeHistoryTest.class, SFCodeHistoryTest.class,
		CrawlGitHubTest.class, SearchGitHubTest.class,
		SearchGoogleCodeTest.class, SearchSourceForgeTest.class })
public class AllTests {

}