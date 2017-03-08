package com.bluecirclesoft.open.getopt;

import org.junit.Assert;
import org.junit.Test;

/**
 * TODO document me
 */
public class UsageTest {

	@Test
	public void testReceptacleA() {
		ReceptacleA opts = new ReceptacleA();
		GetOpt getOpt = GetOpt.createFromReceptacle(opts, "testReceptacleA", null);
		try {
			getOpt.processParams("-x", "3");
		} catch (CommandLineProcessingException e) {
			String usage = e.getMessage();
			System.out.println(usage);
			Assert.assertEquals("No such option -x\n" + "usage:\n" + "testReceptacleA -b [ --a <opt> ] [ --c <o3> ]\n" + "  -a <opt>\n" +
					"  --option-one <opt>\n" + "    value for option one\n" + "\n" + "  -b\n" + "    option 2?\n" + "\n" + "  -c <o3>\n" +
					"  --opt3 <o3>\n" + "  --option-3 <o3>\n" + "    Option 3\n", usage);
		}

	}

	@Test
	public void testGnuReceptacle() {
		GnuReceptacle opts = new GnuReceptacle();
		GetOpt getOpt = GetOpt.createFromReceptacle(opts, "testGnuReceptacle", null);
		try {
			getOpt.processParams("-x", "3");
		} catch (CommandLineProcessingException e) {
			Assert.assertEquals("No such option -x\n" + "usage:\n" +
					"testGnuReceptacle -m [ --a <value> ] [ --abc <value> ] [ --b <value> ] [ --c <value> ]\n" + "  --abc <value>\n" +
					"    the abc value\n" + "\n" + "  -a <value>\n" + "  --a-value <value>\n" + "    the a value\n" + "\n" +
					"  -b <value>\n" + "  --b-value <value>\n" + "    the b value\n" + "\n" + "  -c <value>\n" + "  --c-value <value>\n" +
					"    the c value\n" + "\n" + "  -m\n" + "  --m-flag\n" + "    the m flag\n", e.getMessage());
		}

	}

}
