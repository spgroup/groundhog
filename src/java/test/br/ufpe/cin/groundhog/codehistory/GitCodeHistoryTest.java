package br.ufpe.cin.groundhog.codehistory;

import static org.junit.Assert.assertNotNull;

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
		file = new File("C:\\Users\\fjsj\\Downloads\\EponaProjects\\playframework");
		calendar = new GregorianCalendar(2012, 5, 23).getTime();
	}
	
	@Test
	public void main() throws Exception {
		if(file.exists()){
			File result = codeHistory.checkoutToDate("javacv", file, calendar);
			assertNotNull(result);
		}
	}
}
