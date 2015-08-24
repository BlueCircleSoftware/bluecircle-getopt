package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.CommandLineOptionException;
import com.bluecirclesoft.open.getopt.TypeConverter;

/**
 * TODO document me
 */
public class LongConverter implements TypeConverter<Long> {

	@Override
	public Long convert(String input) throws CommandLineOptionException {
		if (ConverterUtil.isEmpty(input)) {
			return null;
		} else {
			try {
				return Long.parseLong(input);
			} catch (NumberFormatException e) {
				throw new CommandLineOptionException("The value '" + input + "' must be a number " +
						"between " + Long.MIN_VALUE + " and " + Long.MAX_VALUE);
			}
		}
	}
}
