package br.ufpe.cin.groundhog.main;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

	@Test
	public void testJsonInputFile() {
		CmdOptions op = new CmdOptions();
		op.setInputFile(new File("groundhog.json"));
		Assert.assertNotNull(op.getInputFile());
	}
}
