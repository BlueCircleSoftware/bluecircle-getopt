package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.CommandLineOptionException;
import com.bluecirclesoft.open.getopt.TypeConverter;

/**
 * TODO document me
 */
public class DoubleConverter implements TypeConverter<Double> {

	@Override
	public Double convert(String input) throws CommandLineOptionException {
		if (ConverterUtil.isEmpty(input)) {
			return null;
		} else {
			try {
				return Double.parseDouble(input);
			} catch (NumberFormatException e) {
				throw new CommandLineOptionException("The value '" + input + "' must be a " +
						"floating-point number");
			}
		}
	}
}
