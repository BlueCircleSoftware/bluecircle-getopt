package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.CommandLineOptionException;
import com.bluecirclesoft.open.getopt.TypeConverter;

/**
 * TODO document me
 */
public class IntegerConverter implements TypeConverter<Integer> {

	@Override
	public Integer convert(String input) throws CommandLineOptionException {
		if (ConverterUtil.isEmpty(input)) {
			return null;
		} else {
			try {
				return Integer.parseInt(input);
			} catch (NumberFormatException e) {
				throw new CommandLineOptionException("The value '" + input + "' must be a number " +
						"between " + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE);
			}
		}
	}
}
