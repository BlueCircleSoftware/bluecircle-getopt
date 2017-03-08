package com.bluecirclesoft.open.getopt.converters;

import java.math.BigDecimal;

import org.junit.Test;

import com.bluecirclesoft.open.getopt.GetOpt;
import com.bluecirclesoft.open.getopt.OptionSpecification;
import junit.framework.Assert;

/**
 * TODO document me
 */
public class BigDecimalConverterTest {

	@Test
	public void convert() throws Exception {

		BigDecimalConverter converter = new BigDecimalConverter();

		Assert.assertNull(converter.convert(null, null, null));
		Assert.assertNull(converter.convert("", null, null));
		Assert.assertNull(converter.convert("   ", null, null));

		Assert.assertEquals(new BigDecimal("10"), converter.convert("10", null, null));
		Assert.assertEquals(new BigDecimal("10"), converter.convert("10.", null, null));
		Assert.assertEquals(new BigDecimal("10.0"), converter.convert("10.0", null, null));
		Assert.assertEquals(new BigDecimal("10.00"), converter.convert("10.00", null, null));

		Assert.assertEquals(new BigDecimal("10"), converter.convert("   10", null, null));
		Assert.assertEquals(new BigDecimal("10"), converter.convert("   10.", null, null));
		Assert.assertEquals(new BigDecimal("10.0"), converter.convert("   10.0", null, null));
		Assert.assertEquals(new BigDecimal("10.00"), converter.convert("   10.00", null, null));

		Assert.assertEquals(new BigDecimal("10"), converter.convert("10   ", null, null));
		Assert.assertEquals(new BigDecimal("10"), converter.convert("10.   ", null, null));
		Assert.assertEquals(new BigDecimal("10.0"), converter.convert("10.0   ", null, null));
		Assert.assertEquals(new BigDecimal("10.00"), converter.convert("10.00   ", null, null));

		GetOpt options = GetOpt.create("test", null);
		options.addParam("val", "the val", true, BigDecimal.class, null).addShortOpt('i');
		OptionSpecification myOpt = options.getOptions().iterator().next();
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("10q", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("10q0", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("    10q0", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("10q0   ", options, myOpt));


	}

}