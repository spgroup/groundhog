package br.ufpe.cin.groundhog.codehistory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.groundhog.codehistory.GitCodeHistory;

public class SFCodeHistoryTest {
	
	private GitCodeHistory gitCodeHistory;
	private File file;
	private Date calendar;
	
	@Before
	public void setup(){
		gitCodeHistory = GitCodeHistory.getInstance();
		file = new File("C:\\Users\\fjsj\\Downloads\\EponaProjects\\playframework");
		calendar = new GregorianCalendar(2012, 5, 23).getTime();
	}
	
	@Test(expected=AssertionError.class)
	public void main() throws Exception {
		assertTrue(file.exists());
		File result = gitCodeHistory.checkoutToDate("javacv", file, calendar);
		assertNotNull(result);
	}
}
