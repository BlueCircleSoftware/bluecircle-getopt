package com.bluecirclesoft.open.getopt.converters;

import org.junit.Test;

import com.bluecirclesoft.open.getopt.GetOpt;
import com.bluecirclesoft.open.getopt.OptionSpecification;
import junit.framework.Assert;

/**
 * TODO document me
 */
public class CharConverterTest {

	@Test
	public void convert() throws Exception {


		CharConverter converter = new CharConverter();

		Assert.assertNull(converter.convert(null, null, null));
		Assert.assertNull(converter.convert("", null, null));

		Assert.assertEquals(new Character('a'), converter.convert("a", null, null));
		Assert.assertEquals(new Character('A'), converter.convert("A", null, null));
		Assert.assertEquals(new Character(' '), converter.convert(" ", null, null));

		GetOpt options = GetOpt.create("test", null);
		options.addParam("val", "the val", true, Character.class, null).addShortOpt('i');
		OptionSpecification myOpt = options.getOptions().iterator().next();
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("  ", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert(" a", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("a ", options, myOpt));
		ConverterTestHelper.checkProcessingExection(() -> converter.convert("abc", options, myOpt));
	}

}