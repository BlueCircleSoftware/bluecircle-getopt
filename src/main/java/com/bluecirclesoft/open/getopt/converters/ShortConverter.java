package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.CommandLineOptionException;
import com.bluecirclesoft.open.getopt.TypeConverter;

/**
 * TODO document me
 */
public class ShortConverter implements TypeConverter<Short> {

	@Override
	public Short convert(String input) throws CommandLineOptionException {
		if (ConverterUtil.isEmpty(input)) {
			return null;
		} else {
			try {
				return Short.parseShort(input);
			} catch (NumberFormatException e) {
				throw new CommandLineOptionException("The value '" + input + "' must be a number " +
						"between " + Short.MIN_VALUE + " and " + Short.MAX_VALUE);
			}
		}
	}
}
