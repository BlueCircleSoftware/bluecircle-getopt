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

package com.bluecirclesoft.open.getopt.converters;

import org.junit.Test;

import junit.framework.Assert;

/**
 * TODO document me
 */
public class TestConverterUtil {

	@Test
	public void testIsEmpty() {
		Assert.assertTrue(ConverterUtil.isEmpty(null));
		Assert.assertTrue(ConverterUtil.isEmpty(""));
		Assert.assertTrue(ConverterUtil.isEmpty(" "));
		Assert.assertTrue(ConverterUtil.isEmpty("   "));
		Assert.assertTrue(ConverterUtil.isEmpty("\t"));
		Assert.assertTrue(ConverterUtil.isEmpty(" \t "));
		Assert.assertFalse(ConverterUtil.isEmpty("a"));
		Assert.assertFalse(ConverterUtil.isEmpty(" a"));
		Assert.assertFalse(ConverterUtil.isEmpty("a "));
		Assert.assertFalse(ConverterUtil.isEmpty(" a "));
	}

}
