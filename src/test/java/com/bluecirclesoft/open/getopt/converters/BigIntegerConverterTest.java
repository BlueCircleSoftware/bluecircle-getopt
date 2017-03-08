package com.bluecirclesoft.open.getopt.converters;

import java.math.BigInteger;

import org.junit.Test;

import com.bluecirclesoft.open.getopt.GetOpt;
import com.bluecirclesoft.open.getopt.OptionSpecification;
import junit.framework.Assert;

/**
 * TODO document me
 */
public class BigIntegerConverterTest {


	@Test
	public void convert() throws Exception {

		BigIntegerConverter converter = new BigIntegerConverter();

		Assert.assertNull(converter.convert(null, null, null));
		Assert.assertNull(converter.convert("", null, null));
		Assert.assertNull(converter.convert("   ", null, null));

		Assert.assertEquals(new BigInteger("10"), converter.convert("10", null, null));
		Assert.assertEquals(new BigInteger("-232"), converter.convert("-232", null, null));
		Assert.assertEquals(new BigInteger("232"), converter.convert("+232", null, null));
		Assert.assertEquals(new BigInteger("123456789123456789"), converter.convert("123456789123456789", null, null));

		Assert.assertEquals(new BigInteger("10"), converter.convert("   10", null, null));

		Assert.assertEquals(new BigInteger("10"), converter.convert("10   ", null, null));

		GetOpt options = GetOpt.create("test", null);
		options.addParam("val", "the val", true, BigInteger.class, null).addShortOpt('i');
		OptionSpecification myOpt = options.getOptions().iterator().next();
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("10.", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("10.0", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("    10.0", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("10.0   ", options, myOpt));
	}
}