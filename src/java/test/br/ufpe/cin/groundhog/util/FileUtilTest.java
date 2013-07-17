package br.ufpe.cin.groundhog.util;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import br.ufpe.cin.groundhog.util.FileUtil;

public class FileUtilTest {

	@Test
	public void testIsFile() throws IOException {
		File files[] = new File(".").listFiles();

		for (File file : files) {
			boolean is = FileUtil.getInstance().isTextFile(file);
			System.out.println(is + ", " + file.getAbsolutePath());
		}
	}
}
