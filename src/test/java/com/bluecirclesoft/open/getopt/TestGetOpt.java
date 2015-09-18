/*
 * Copyright 2015 Blue Circle Software, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bluecirclesoft.open.getopt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestGetOpt {

	protected GetOpt defaultOptions;

	private Map<String, Boolean> encountered;

	@Before
	public void setUp() {
		defaultOptions = GetOpt.create("Test");
		defaultOptions.addFlag("blah", on -> encountered.put("a", on))
				.addShortOpt('a')
				.addLongOpt("alpha");
		defaultOptions.addFlag("blah", on -> encountered.put("b", on))
				.addShortOpt('b')
				.addLongOpt("beta");
		defaultOptions.addFlag("blah", on -> encountered.put("c", on))
				.addShortOpt('c')
				.addLongOpt("gamma");
		defaultOptions.addFlag("blah", on -> encountered.put("d", on))
				.addShortOpt('d')
				.addLongOpt("delta");
		encountered = new HashMap<>();
	}

	@Test
	public void testAddShortOpt() {
		GetOpt opt = GetOpt.create("Test");
		for (char c = 'a'; c <= 'z'; c++) {
			opt.addFlag("blah", on -> encountered.put("a", on)).addShortOpt(c);
		}
	}

	@Test
	public void testShortOptOn() {
		String s = "x";
		String[] in = {"-a", s};
		List<String> out;
		out = defaultOptions.processParams(in);
		Assert.assertTrue(out.size() == 1);
		Assert.assertTrue(out.get(0) == s);
		Assert.assertTrue(defaultOptions.isFlagSet('a'));
		Assert.assertTrue(!defaultOptions.isFlagSet('b'));
	}

	@Test
	public void testLongOptOn() throws CommandLineProcessingException {
		String s = "x";
		String[] in = {"--alp", s};
		List<String> out;
		out = defaultOptions.processParams(in);
		Assert.assertTrue(out.size() == 1);
		Assert.assertTrue(out.get(0) == s);
		Assert.assertTrue(defaultOptions.isFlagSet('a'));
		Assert.assertTrue(!defaultOptions.isFlagSet('b'));
	}

	@Test
	public void testOptOff() throws CommandLineProcessingException {
		String s = "x";
		String[] in = {s};
		List<String> out;
		out = defaultOptions.processParams(in);
		Assert.assertTrue(out.size() == 1);
		Assert.assertTrue(out.get(0) == s);
		Assert.assertTrue(!defaultOptions.isFlagSet('a'));
		Assert.assertTrue(!defaultOptions.isFlagSet('b'));
	}

	@Test
	public void testDashDash() throws CommandLineProcessingException {
		String s = "-a";
		String[] in = {"-b", "--", s};
		List<String> out;
		out = defaultOptions.processParams(in);
		Assert.assertTrue(out.size() == 1);
		Assert.assertTrue(out.get(0) == s);
		Assert.assertTrue(!defaultOptions.isFlagSet('a'));
		Assert.assertTrue(defaultOptions.isFlagSet('b'));
	}

	@Test
	public void testAddFail1() {
		try {
			defaultOptions.addFlag("blah", on -> encountered.put("a", on))
					.addShortOpt('a')
					.addLongOpt("alpha");
			Assert.fail("should have gotten OptionProcessingException");
		} catch (GetOptSetupException ignored) {
		}
	}

	@Test
	public void testAddFail2() {
		try {
			// alpha already there
			defaultOptions.addFlag("blah", on -> encountered.put("e", on))
					.addShortOpt('e')
					.addLongOpt("alpha");
			Assert.fail("should have gotten OptionProcessingException");
		} catch (GetOptSetupException ignored) {
		}
	}

	@Test
	public void testAddSuccess3f() {
		try {
			setUp();
			defaultOptions.addFlag("blah", on -> encountered.put("e", on))
					.addShortOpt('e')
					.addLongOpt("alp");
			// should be no match
			String[] x = {"--alph0"};
			defaultOptions.processParams(x);
			Assert.fail("--alph0 should have failed");
		} catch (CommandLineProcessingException ignored) {
		}
	}

	@Test
	public void testAddSuccess3e() {
		try {
			setUp();
			defaultOptions.addFlag("blah", on -> encountered.put("e", on))
					.addShortOpt('e')
					.addLongOpt("alp");
			// should match --alpha
			String[] x = {"--alpha"};
			defaultOptions.processParams(x);
			Assert.assertFalse(defaultOptions.isFlagSet('e'));
			Assert.assertTrue(defaultOptions.isFlagSet('a'));
		} catch (CommandLineProcessingException e) {
			Assert.fail("--alpha should have succeeded");
		}
	}

	@Test
	public void testAddSuccess3d() {
		try {
			setUp();
			defaultOptions.addFlag("blah", on -> encountered.put("e", on))
					.addShortOpt('e')
					.addLongOpt("alp");
			// should match --alpha
			String[] x = {"--alph"};
			defaultOptions.processParams(x);
			Assert.assertFalse(defaultOptions.isFlagSet('e'));
			Assert.assertTrue(defaultOptions.isFlagSet('a'));
		} catch (CommandLineProcessingException e) {
			Assert.fail("--alph should have succeeded");
		}
	}

	@Test
	public void testAddSuccess3c() {
		try {
			setUp();
			defaultOptions.addFlag("blah", on -> encountered.put("e", on))
					.addShortOpt('e')
					.addLongOpt("alp");
			// should match --alp
			String[] x = {"--alp"};
			defaultOptions.processParams(x);
			Assert.assertTrue(defaultOptions.isFlagSet('e'));
			Assert.assertFalse(defaultOptions.isFlagSet('a'));
		} catch (CommandLineProcessingException e) {
			Assert.fail("--alp should have succeeded");
		}
	}

	@Test
	public void testAddSuccess3b() {
		try {
			setUp();
			defaultOptions.addFlag("blah", on -> encountered.put("e", on))
					.addShortOpt('e')
					.addLongOpt("alp");
			// should be ambiguous
			String[] x = {"--al"};
			defaultOptions.processParams(x);
			Assert.fail("--al should have failed");
		} catch (CommandLineProcessingException ignored) {
		}
	}

	@Test
	public void testAddSuccess3a() {
		try {
			setUp();
			defaultOptions.addFlag("blah", on -> encountered.put("e", on))
					.addShortOpt('e')
					.addLongOpt("alp");
			// should be ambiguous
			String[] x = {"--a"};
			defaultOptions.processParams(x);
			Assert.fail("--a should have failed");
		} catch (CommandLineProcessingException ignored) {
		}
	}

	@Test
	public void testAddSuccess4() {
		defaultOptions.addFlag("blah", on -> encountered.put("e", on))
				.addShortOpt('e')
				.addLongOpt("alpharetta");
	}

	@Test
	public void testAddFail5() {
		try {
			defaultOptions.addFlag("blah", on -> encountered.put("e", on))
					.addShortOpt('e')
					.addLongOpt("alpho");
			String[] x = {"--alp"};
			defaultOptions.processParams(x);
			Assert.fail("should have gotten CommandLineOptionException");
		} catch (CommandLineProcessingException ignored) {
		}
	}

	@Test
	public void testRequiredParameterMissing() {
		try {
			defaultOptions.addParam("filename", "hoobly", true, arg -> {
			}).addShortOpt('x');
			String[] x = {};
			defaultOptions.processParams(x);
			Assert.fail("should have gotten CommandLineOptionException");
		} catch (CommandLineProcessingException ignored) {
		}
	}

	@Test
	public void testNoSuchShort() {
		try {
			String[] x = {"-q"};
			defaultOptions.processParams(x);
			Assert.fail("should have gotten CommandLineOptionException");
		} catch (CommandLineProcessingException ignored) {
		}
	}

	@Test
	public void testNoSuchLong() {
		try {
			String[] x = {"--quassy"};
			defaultOptions.processParams(x);
			Assert.fail("should have gotten CommandLineOptionException");
		} catch (CommandLineProcessingException ignored) {
		}
	}

	@Test
	public void testAmbiguousLong() {
		try {
			defaultOptions.addFlag("crap", arg -> {
			}).addLongOpt("alpho");
			String[] x = {"--alp"};
			defaultOptions.processParams(x);
			Assert.fail("should have gotten CommandLineOptionException");
		} catch (CommandLineProcessingException ignored) {
		}
	}

	@Test
	public void testReturnAddOpt() {
		OptionSpecification opt = defaultOptions.addFlag("whoa", arg -> {
		});
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
		} catch (CommandLineProcessingException e) {
			e.printStackTrace();
			Assert.fail("Should not have failed");
		}
	}

	@Test
	public void testExampleFluent1() {
		final UtilityOptions receptacle = new UtilityOptions();
		GetOpt options = GetOpt.create("myutility");
		options.addParam("file", "the input file (- for standard input)", false,
				receptacle::setInput).addShortOpt('i').addLongOpt("input-file");
		options.addParam("file", "the output file (- for standard output)", false,
				receptacle::setOutput).addShortOpt('o').addLongOpt("output-file");
		options.addFlag("produce verbose output", receptacle::setVerbose)
				.addShortOpt('v')
				.addLongOpt("verbose");
		try {
			options.processParams("-v");
		} catch (CommandLineProcessingException e) {
			Assert.fail("Should have succeeded");
		}

		Assert.assertTrue(receptacle.isVerbose());
		Assert.assertEquals("-", receptacle.getInput());
		Assert.assertEquals("-", receptacle.getOutput());
	}

	@Test
	public void testExampleReceptacle1() {
		final UtilityOptions2 receptacle = new UtilityOptions2();
		GetOpt options = GetOpt.createFromReceptacle(receptacle, "myUtility");
		try {
			options.processParams("-v");
		} catch (CommandLineProcessingException e) {
			Assert.fail("Should have succeeded");
		}

		Assert.assertTrue(receptacle.isVerbose());
		Assert.assertEquals("-", receptacle.getInput());
		Assert.assertEquals("-", receptacle.getOutput());
	}

	@Test
	public void testExampleFluent2() {
		final UtilityOptions receptacle = new UtilityOptions();
		GetOpt options = GetOpt.create("myutility");
		options.addParam("file", "the input file (- for standard input)", false,
				receptacle::setInput).addShortOpt('i').addLongOpt("input-file");
		options.addParam("file", "the output file (- for standard output)", false,
				receptacle::setOutput).addShortOpt('o').addLongOpt("output-file");
		options.addFlag("produce verbose output", receptacle::setVerbose)
				.addShortOpt('v')
				.addLongOpt("verbose");
		try {
			options.processParams();
		} catch (CommandLineProcessingException e) {
			Assert.fail("Should have succeeded");
		}

		Assert.assertFalse(receptacle.isVerbose());
		Assert.assertEquals("-", receptacle.getInput());
		Assert.assertEquals("-", receptacle.getOutput());
	}

	@Test
	public void testExampleFluent3() {
		final UtilityOptions options = new UtilityOptions();
		GetOpt getOpt = GetOpt.create("myutility");
		getOpt.addParam("file", "the input file (- for standard input)", false, options::setInput)
				.addShortOpt('i')
				.addLongOpt("input-file");
		getOpt.addParam("file", "the output file (- for standard output)", false,
				options::setOutput).addShortOpt('o').addLongOpt("output-file");
		getOpt.addFlag("produce verbose output", options::setVerbose)
				.addShortOpt('v')
				.addLongOpt("verbose");
		try {
			getOpt.processParams("-v", "-i");
			Assert.fail("Should have failed");
		} catch (CommandLineProcessingException e) {
			System.err.println(e.getMessage());
		}
	}
}
