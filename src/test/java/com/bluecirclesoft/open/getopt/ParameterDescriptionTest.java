package com.bluecirclesoft.open.getopt;

import java.util.ArrayList;

import org.junit.Test;

import junit.framework.Assert;

/**
 * Tests for {@link ParameterDescription}
 */
public class ParameterDescriptionTest {

	@Test
	public void getBrokenDocumentation() throws Exception {
		ParameterDescription parameterDescription = new ParameterDescription();
		parameterDescription.setDocumentation("WAR file to install (must specify this");
		ArrayList<String> result = (ArrayList<String>) parameterDescription.getBrokenDocumentation();
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("WAR file to install (must specify this", result.get(0));

		parameterDescription = new ParameterDescription();
		parameterDescription.setDocumentation("WAR file to install (must specify this or --dependency-file)");
		result = (ArrayList<String>) parameterDescription.getBrokenDocumentation();
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("WAR file to install (must specify this or --dependency-file)", result.get(0));

		parameterDescription = new ParameterDescription();
		parameterDescription.setDocumentation("WAR file to install (must specify this or --dependency-files)");
		result = (ArrayList<String>) parameterDescription.getBrokenDocumentation();
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("WAR file to install (must specify this or", result.get(0));
		Assert.assertEquals("--dependency-files)", result.get(1));
	}

}