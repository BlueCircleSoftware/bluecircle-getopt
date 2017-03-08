package com.bluecirclesoft.open.getopt.converters;

import java.math.BigInteger;

import org.junit.Test;

import com.bluecirclesoft.open.getopt.GetOpt;
import com.bluecirclesoft.open.getopt.OptionSpecification;
import junit.framework.Assert;

/**
 * TODO document me
 */
public class ByteConverterTest {

	@Test
	public void convert() throws Exception {

		ByteConverter converter = new ByteConverter();

		Assert.assertNull(converter.convert(null, null, null));
		Assert.assertNull(converter.convert("", null, null));
		Assert.assertNull(converter.convert("   ", null, null));

		Assert.assertEquals(new Byte("10"), converter.convert("10", null, null));
		Assert.assertEquals(new Byte("-12"), converter.convert("-12", null, null));
		Assert.assertEquals(new Byte("12"), converter.convert("+12", null, null));

		Assert.assertEquals(new Byte("10"), converter.convert("   10", null, null));

		Assert.assertEquals(new Byte("10"), converter.convert("10   ", null, null));

		GetOpt options = GetOpt.create("test", null);
		options.addParam("val", "the val", true, BigInteger.class, null).addShortOpt('i');
		OptionSpecification myOpt = options.getOptions().iterator().next();
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("1000", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("-1000", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("10.", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("10.0", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("    10.0", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("10.0   ", options, myOpt));
	}


}