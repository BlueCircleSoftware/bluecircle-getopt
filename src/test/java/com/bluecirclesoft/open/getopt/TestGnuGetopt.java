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

import com.bluecirclesoft.open.getopt.flavors.CommandLineProcessingFlavors;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test class for the GNU getopt style processing
 */
public class TestGnuGetopt {

	@Test
	public void testShortTwoStrings() {
		GnuReceptacle gnuReceptacle = new GnuReceptacle();

		GetOpt getOpt = GetOpt.createFromReceptacle(gnuReceptacle, TestGnuGetopt.class);
		getOpt.processParams("-a", "a");
		Assert.assertEquals("a", gnuReceptacle.getA());
		Assert.assertNull(gnuReceptacle.getB());
		Assert.assertNull(gnuReceptacle.getC());
		Assert.assertNull(gnuReceptacle.getAbc());
		Assert.assertFalse(gnuReceptacle.isM());
	}

	@Test
	public void testShortTwoStrings2() {
		GnuReceptacle gnuReceptacle = new GnuReceptacle();

		GetOpt getOpt = GetOpt.createFromReceptacle(gnuReceptacle, TestGnuGetopt.class);
		getOpt.processParams("-a", "abc");
		Assert.assertEquals("abc", gnuReceptacle.getA());
		Assert.assertNull(gnuReceptacle.getB());
		Assert.assertNull(gnuReceptacle.getC());
		Assert.assertNull(gnuReceptacle.getAbc());
		Assert.assertFalse(gnuReceptacle.isM());
	}

	@Test
	public void testShortOneString() {
		GnuReceptacle gnuReceptacle = new GnuReceptacle();

		GetOpt getOpt = GetOpt.createFromReceptacle(gnuReceptacle, TestGnuGetopt.class);
		getOpt.processParams("-aa");
		Assert.assertEquals("a", gnuReceptacle.getA());
		Assert.assertNull(gnuReceptacle.getB());
		Assert.assertNull(gnuReceptacle.getC());
		Assert.assertNull(gnuReceptacle.getAbc());
		Assert.assertFalse(gnuReceptacle.isM());
	}

	@Test
	public void testShortOneString2() {
		GnuReceptacle gnuReceptacle = new GnuReceptacle();

		GetOpt getOpt = GetOpt.createFromReceptacle(gnuReceptacle, TestGnuGetopt.class);
		getOpt.processParams("-aabc");
		Assert.assertEquals("abc", gnuReceptacle.getA());
		Assert.assertNull(gnuReceptacle.getB());
		Assert.assertNull(gnuReceptacle.getC());
		Assert.assertNull(gnuReceptacle.getAbc());
		Assert.assertFalse(gnuReceptacle.isM());
	}

	@Test
	public void testShortOneString3() {
		GnuReceptacle gnuReceptacle = new GnuReceptacle();

		GetOpt getOpt = GetOpt.createFromReceptacle(gnuReceptacle, TestGnuGetopt.class);
		getOpt.processParams("-aabcm");
		Assert.assertEquals("abcm", gnuReceptacle.getA());
		Assert.assertNull(gnuReceptacle.getB());
		Assert.assertNull(gnuReceptacle.getC());
		Assert.assertNull(gnuReceptacle.getAbc());
		Assert.assertFalse(gnuReceptacle.isM());
	}

	@Test
	public void testShortOneString4() {
		GnuReceptacle gnuReceptacle = new GnuReceptacle();

		GetOpt getOpt = GetOpt.createFromReceptacle(gnuReceptacle, TestGnuGetopt.class);
		getOpt.processParams("-maabc");
		Assert.assertEquals("abc", gnuReceptacle.getA());
		Assert.assertNull(gnuReceptacle.getB());
		Assert.assertNull(gnuReceptacle.getC());
		Assert.assertNull(gnuReceptacle.getAbc());
		Assert.assertTrue(gnuReceptacle.isM());
	}

	@Test
	public void testLongFullNoEquals() {
		GnuReceptacle gnuReceptacle = new GnuReceptacle();

		GetOpt getOpt = GetOpt.createFromReceptacle(gnuReceptacle, TestGnuGetopt.class);
		getOpt.processParams("--a-value", "a");
		Assert.assertEquals("a", gnuReceptacle.getA());
		Assert.assertNull(gnuReceptacle.getB());
		Assert.assertNull(gnuReceptacle.getC());
		Assert.assertNull(gnuReceptacle.getAbc());
		Assert.assertFalse(gnuReceptacle.isM());
	}

	@Test
	public void testLongFullWithEquals() {
		GnuReceptacle gnuReceptacle = new GnuReceptacle();

		GetOpt getOpt = GetOpt.createFromReceptacle(gnuReceptacle, TestGnuGetopt.class);
		getOpt.processParams("--a-value=a");
		Assert.assertEquals("a", gnuReceptacle.getA());
		Assert.assertNull(gnuReceptacle.getB());
		Assert.assertNull(gnuReceptacle.getC());
		Assert.assertNull(gnuReceptacle.getAbc());
		Assert.assertFalse(gnuReceptacle.isM());
	}

	@Test
	public void testOptAfterNonOpt() {
		GnuReceptacle gnuReceptacle = new GnuReceptacle();

		GetOpt getOpt = GetOpt.createFromReceptacle(gnuReceptacle, TestGnuGetopt.class);
		List<String> result = getOpt.processParams("q", "--a-value", "a");
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("q", result.get(0));
		Assert.assertEquals("a", gnuReceptacle.getA());
		Assert.assertNull(gnuReceptacle.getB());
		Assert.assertNull(gnuReceptacle.getC());
		Assert.assertNull(gnuReceptacle.getAbc());
		Assert.assertFalse(gnuReceptacle.isM());
	}

	@Test
	public void testOptAfterNonOptPosixlyCorrect() {
		GnuReceptacle gnuReceptacle = new GnuReceptacle();

		GetOpt getOpt = GetOpt.createFromReceptacle(gnuReceptacle, TestGnuGetopt.class,
				CommandLineProcessingFlavors.GNU_GETOPT_POSIXLY_CORRECT);
		List<String> result = getOpt.processParams("q", "--a-value", "a");
		Assert.assertEquals(3, result.size());
		Assert.assertEquals("q", result.get(0));
		Assert.assertEquals("--a-value", result.get(1));
		Assert.assertEquals("a", result.get(2));
		Assert.assertNull(gnuReceptacle.getA());
		Assert.assertNull(gnuReceptacle.getB());
		Assert.assertNull(gnuReceptacle.getC());
		Assert.assertNull(gnuReceptacle.getAbc());
		Assert.assertFalse(gnuReceptacle.isM());
	}


}
