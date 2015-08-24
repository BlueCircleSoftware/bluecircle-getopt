package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.CommandLineOptionException;
import com.bluecirclesoft.open.getopt.TypeConverter;

/**
 * TODO document me
 */
public class CharConverter implements TypeConverter<Character> {

	@Override
	public Character convert(String input) throws CommandLineOptionException {
		if (ConverterUtil.isEmpty(input)) {
			return null;
		} else if (input.length() > 1) {
			throw new CommandLineOptionException("Parameter can only be one character");
		} else {
			return input.charAt(0);
		}
	}
}
