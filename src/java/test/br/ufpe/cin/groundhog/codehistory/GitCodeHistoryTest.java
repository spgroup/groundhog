package br.ufpe.cin.groundhog.codehistory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

public class GitCodeHistoryTest {

	private SFCodeHistory  codeHistory;
	private File file;
	private Date calendar;
	
	@Before
	public void setup(){
		codeHistory = SFCodeHistory.getInstance();
		file = new File("C:\\Users\\fjsj\\Downloads\\EponaProjects\\playframework");
		calendar = new GregorianCalendar(2012, 5, 23).getTime();
	}
	
	@Test(expected=AssertionError.class)
	public void main() throws Exception {
		assertTrue(file.exists());
		File result = codeHistory.checkoutToDate("javacv", file, calendar);
		assertNotNull(result);
	}
}
