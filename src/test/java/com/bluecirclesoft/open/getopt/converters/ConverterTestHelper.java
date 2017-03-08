package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.CommandLineProcessingException;
import junit.framework.Assert;

/**
 * TODO document me
 */
public class ConverterTestHelper {

	/**
	 * Assert that a CommandLineProcessingException is thrown
	 *
	 * @param r the runnable to test
	 */
	public static void checkProcessingExection(Runnable r) {
		try {
			r.run();
			Assert.fail("Should have thrown CommandLineProcessingException");
		} catch (CommandLineProcessingException e) {
			// pass
		}
	}
}
