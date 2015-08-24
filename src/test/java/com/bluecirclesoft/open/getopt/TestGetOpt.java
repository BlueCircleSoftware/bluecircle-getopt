/*

Copyright (C) 2003, 2004, 2005 Marc A. Ramirez.  All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

1) Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.

2) Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.

3) The name of Marc A. Ramirez may not be used neither to endorse nor
promote products derived from this software without specific prior
written permission.

THIS SOFTWARE IS PROVIDED BY MARC A. RAMIREZ "AS IS" AND ANY EXPRESS
OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL MARC A. RAMIREZ BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

package com.bluecirclesoft.open.getopt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TestGetOpt {

	protected GetOpt defaultOptions;

	@Before
	public void setUp() {
		defaultOptions = GetOpt.create("Test");
		defaultOptions.addFlag("blah").addShortOpt('a').addLongOpt("alpha");
		defaultOptions.addFlag("blah").addShortOpt('b').addLongOpt("beta");
		defaultOptions.addFlag("blah").addShortOpt('c').addLongOpt("gamma");
		defaultOptions.addFlag("blah").addShortOpt('d').addLongOpt("delta");
	}

	@Test
	public void testAddShortOpt() throws CommandLineOptionException {
		GetOpt opt = GetOpt.create("Test");
		for (char c = 'a'; c <= 'z'; c++) {
			opt.addFlag("blah").addShortOpt(c);
		}
	}

	@Test
	public void testShortOptOn() throws CommandLineOptionException {
		String s = "x";
		String[] in = {"-a", s};
		ArrayList out;
		out = defaultOptions.processParams(in);
		Assert.assertTrue(out.size() == 1);
		Assert.assertTrue(out.get(0) == s);
		Assert.assertTrue(defaultOptions.isFlagSet('a'));
		Assert.assertTrue(!defaultOptions.isFlagSet('b'));
	}

	@Test
	public void testLongOptOn() throws CommandLineOptionException {
		String s = "x";
		String[] in = {"--alp", s};
		ArrayList out;
		out = defaultOptions.processParams(in);
		Assert.assertTrue(out.size() == 1);
		Assert.assertTrue(out.get(0) == s);
		Assert.assertTrue(defaultOptions.isFlagSet('a'));
		Assert.assertTrue(!defaultOptions.isFlagSet('b'));
	}

	@Test
	public void testOptOff() throws CommandLineOptionException {
		String s = "x";
		String[] in = {s};
		ArrayList out;
		out = defaultOptions.processParams(in);
		Assert.assertTrue(out.size() == 1);
		Assert.assertTrue(out.get(0) == s);
		Assert.assertTrue(!defaultOptions.isFlagSet('a'));
		Assert.assertTrue(!defaultOptions.isFlagSet('b'));
	}

	@Test
	public void testDashDash() throws CommandLineOptionException {
		String s = "-a";
		String[] in = {"-b", "--", s};
		ArrayList out;
		out = defaultOptions.processParams(in);
		Assert.assertTrue(out.size() == 1);
		Assert.assertTrue(out.get(0) == s);
		Assert.assertTrue(!defaultOptions.isFlagSet('a'));
		Assert.assertTrue(defaultOptions.isFlagSet('b'));
	}

	@Test
	public void testAddFail1() {
		try {
			defaultOptions.addFlag("blah").addShortOpt('a').addLongOpt("alpha");
			Assert.fail("should have gotten OptionProcessingException");
		} catch (OptionProcessingException ignored) {
		}
	}

	@Test
	public void testAddFail2() {
		try {
			// alpha already there
			defaultOptions.addFlag("blah").addShortOpt('e').addLongOpt("alpha");
			Assert.fail("should have gotten OptionProcessingException");
		} catch (OptionProcessingException ignored) {
		}
	}

	@Test
	public void testAddSuccess3f() {
		try {
			setUp();
			defaultOptions.addFlag("blah").addShortOpt('e').addLongOpt("alp");
			// should be no match
			String[] x = {"--alph0"};
			defaultOptions.processParams(x);
			Assert.fail("--alph0 should have failed");
		} catch (CommandLineOptionException ignored) {
		}
	}

	@Test
	public void testAddSuccess3e() {
		try {
			setUp();
			defaultOptions.addFlag("blah").addShortOpt('e').addLongOpt("alp");
			// should match --alpha
			String[] x = {"--alpha"};
			defaultOptions.processParams(x);
			Assert.assertFalse(defaultOptions.isFlagSet('e'));
			Assert.assertTrue(defaultOptions.isFlagSet('a'));
		} catch (CommandLineOptionException e) {
			Assert.fail("--alpha should have succeeded");
		}
	}

	@Test
	public void testAddSuccess3d() {
		try {
			setUp();
			defaultOptions.addFlag("blah").addShortOpt('e').addLongOpt("alp");
			// should match --alpha
			String[] x = {"--alph"};
			defaultOptions.processParams(x);
			Assert.assertFalse(defaultOptions.isFlagSet('e'));
			Assert.assertTrue(defaultOptions.isFlagSet('a'));
		} catch (CommandLineOptionException e) {
			Assert.fail("--alph should have succeeded");
		}
	}

	@Test
	public void testAddSuccess3c() {
		try {
			setUp();
			defaultOptions.addFlag("blah").addShortOpt('e').addLongOpt("alp");
			// should match --alp
			String[] x = {"--alp"};
			defaultOptions.processParams(x);
			Assert.assertTrue(defaultOptions.isFlagSet('e'));
			Assert.assertFalse(defaultOptions.isFlagSet('a'));
		} catch (CommandLineOptionException e) {
			Assert.fail("--alp should have succeeded");
		}
	}

	@Test
	public void testAddSuccess3b() {
		try {
			setUp();
			defaultOptions.addFlag("blah").addShortOpt('e').addLongOpt("alp");
			// should be ambiguous
			String[] x = {"--al"};
			defaultOptions.processParams(x);
			Assert.fail("--al should have failed");
		} catch (CommandLineOptionException ignored) {
		}
	}

	@Test
	public void testAddSuccess3a() {
		try {
			setUp();
			defaultOptions.addFlag("blah").addShortOpt('e').addLongOpt("alp");
			// should be ambiguous
			String[] x = {"--a"};
			defaultOptions.processParams(x);
			Assert.fail("--a should have failed");
		} catch (CommandLineOptionException ignored) {
		}
	}

	@Test
	public void testAddSuccess4() {
		defaultOptions.addFlag("blah").addShortOpt('e').addLongOpt("alpharetta");
	}

	@Test
	public void testAddFail5() {
		try {
			defaultOptions.addFlag("blah").addShortOpt('e').addLongOpt("alpho");
			String[] x = {"--alp"};
			defaultOptions.processParams(x);
			Assert.fail("should have gotten CommandLineOptionException");
		} catch (CommandLineOptionException ignored) {
		}
	}

	@Test
	public void testRequiredParameterMissing() {
		try {
			defaultOptions.addParam("filename", "hoobly", true).addShortOpt('x');
			String[] x = {};
			defaultOptions.processParams(x);
			Assert.fail("should have gotten CommandLineOptionException");
		} catch (CommandLineOptionException ignored) {
		}
	}

	@Test
	public void testNoSuchShort() {
		try {
			String[] x = {"-q"};
			defaultOptions.processParams(x);
			Assert.fail("should have gotten CommandLineOptionException");
		} catch (CommandLineOptionException ignored) {
		}
	}

	@Test
	public void testNoSuchLong() {
		try {
			String[] x = {"--quassy"};
			defaultOptions.processParams(x);
			Assert.fail("should have gotten CommandLineOptionException");
		} catch (CommandLineOptionException ignored) {
		}
	}

	@Test
	public void testAmbiguousLong() {
		try {
			defaultOptions.addFlag("crap").addLongOpt("alpho");
			String[] x = {"--alp"};
			defaultOptions.processParams(x);
			Assert.fail("should have gotten CommandLineOptionException");
		} catch (CommandLineOptionException ignored) {
		}
	}

	@Test
	public void testReturnAddOpt() {
		ParameterDef<Boolean> opt = defaultOptions.addFlag("whoa");
		Assert.assertEquals(opt, opt.addShortOpt('w'));
		Assert.assertEquals(opt, opt.addLongOpt("whoa"));
	}

	@Test
	public void testReceptacle1() {
		try {
			ReceptacleA receptacleA = new ReceptacleA();
			GetOpt opt = GetOpt.createFromReceptacle(receptacleA, "main");
			opt.processParams("-c", "3", "-a", "qqq", "-b");
			Assert.assertEquals("qqq", receptacleA.getOpt1());
			Assert.assertTrue(receptacleA.isOpt2());
			Assert.assertEquals((short) 3, receptacleA.getOpt3());
		} catch (CommandLineOptionException e) {
			e.printStackTrace();
			Assert.fail("Should not have failed");
		}
	}
}
