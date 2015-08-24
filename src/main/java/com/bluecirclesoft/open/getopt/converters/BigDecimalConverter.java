package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.CommandLineOptionException;
import com.bluecirclesoft.open.getopt.TypeConverter;

import java.math.BigDecimal;

/**
 * TODO document me
 */
public class BigDecimalConverter implements TypeConverter<BigDecimal> {

	@Override
	public BigDecimal convert(String input) throws CommandLineOptionException {
		if (ConverterUtil.isEmpty(input)) {
			return null;
		} else {
			try {
				return new BigDecimal(input);
			} catch (NumberFormatException e) {
				throw new CommandLineOptionException(
						"The value '" + input + "' must be an integer or decimal number");
			}
		}
	}
}
