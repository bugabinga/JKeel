package com.msc.jkeel;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class JKeelTest
{

	@Test
	public void test() throws FileNotFoundException, IOException
	{
		final JKeel jkeel = new JKeel();
		jkeel.setLanguage(new File(getClass().getResource("/com/msc/jkeel/lang_de.loc").getFile()), "english");
		assertEquals("Wie geht's dir, ([Name]).", jkeel.getText("test"));
		assertEquals("Wie geht's dir, Marcel.", jkeel.getText("test", "Marcel"));
		assertEquals("Wie geht's dir, Marcel.", jkeel.getText("test", "Marcel", "Hey"));
		assertEquals("Wie geht's dir, Hey.", jkeel.getText("test", "Hey", "Marcel"));
		assertEquals("Wie geht's dir, Hey.", jkeel.getText("test", new ReplacePair("Name", "Hey"), new ReplacePair("Name", "Fail")));
	}
}
