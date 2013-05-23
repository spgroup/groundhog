package br.ufpe.cin.groundhog.main;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

	@Test
	public void testJsonInputFile() {
		Options op = new Options();
		op.setInputFile(new File("groundhog.json"));
		Assert.assertNotNull(op.getInputFile());
	}
}
