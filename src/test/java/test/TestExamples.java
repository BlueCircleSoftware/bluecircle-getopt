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

package test;

import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test the examples outside the getopt package to make sure I don't accidentally hide the interface again.
 */
public class TestExamples {

	private static final Logger logger = Logger.getLogger(TestExamples.class.getName());


	@Test
	public void testExample1() {
		GetOptExample1.setErrored(false);
		GetOptExample1.main("-i", "-", "-v");
		Assert.assertFalse(GetOptExample1.isErrored());

		GetOptExample1.setErrored(false);
		GetOptExample1.main();
		Assert.assertFalse(GetOptExample1.isErrored());

		GetOptExample1.setErrored(false);
		GetOptExample1.main("-i");
		Assert.assertTrue(GetOptExample1.isErrored());

	}

}
