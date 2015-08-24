package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.converters.ConverterUtil;
import junit.framework.Assert;
import org.junit.Test;

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
