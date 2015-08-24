package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.CommandLineOptionException;
import com.bluecirclesoft.open.getopt.TypeConverter;

/**
 * TODO document me
 */
public class ByteConverter implements TypeConverter<Byte> {

	@Override
	public Byte convert(String input) throws CommandLineOptionException {
		if (ConverterUtil.isEmpty(input)) {
			return null;
		} else {
			try {
				return Byte.parseByte(input);
			} catch (NumberFormatException e) {
				throw new CommandLineOptionException("The value '" + input + "' must be a number " +
						"between " + Byte.MIN_VALUE + " and " + Byte.MAX_VALUE);
			}
		}
	}
}
