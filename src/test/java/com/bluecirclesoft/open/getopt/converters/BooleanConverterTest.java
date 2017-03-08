package com.bluecirclesoft.open.getopt.converters;

import org.junit.Test;

import junit.framework.Assert;

/**
 * TODO document me
 */
public class BooleanConverterTest {

	@Test
	public void convert() throws Exception {
		BooleanConverter converter = new BooleanConverter();

		Assert.assertNull(converter.convert(null, null, null));
		Assert.assertNull(converter.convert("", null, null));
		Assert.assertNull(converter.convert("   ", null, null));

		Assert.assertEquals(Boolean.TRUE, converter.convert("true", null, null));
		Assert.assertEquals(Boolean.TRUE, converter.convert("TRUE", null, null));
		Assert.assertEquals(Boolean.TRUE, converter.convert("   true", null, null));
		Assert.assertEquals(Boolean.TRUE, converter.convert("true    ", null, null));

		Assert.assertEquals(Boolean.FALSE, converter.convert("false", null, null));
		Assert.assertEquals(Boolean.FALSE, converter.convert("t", null, null));
		Assert.assertEquals(Boolean.FALSE, converter.convert("f", null, null));
	}

}