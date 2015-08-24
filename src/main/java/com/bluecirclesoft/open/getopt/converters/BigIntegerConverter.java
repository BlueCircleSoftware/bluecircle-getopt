package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.CommandLineOptionException;
import com.bluecirclesoft.open.getopt.TypeConverter;

import java.math.BigInteger;

/**
 * TODO document me
 */
public class BigIntegerConverter implements TypeConverter<BigInteger> {

	@Override
	public BigInteger convert(String input) throws CommandLineOptionException {
		if (ConverterUtil.isEmpty(input)) {
			return null;
		} else {
			try {
				return new BigInteger(input);
			} catch (NumberFormatException e) {
				throw new CommandLineOptionException(
						"The value '" + input + "' must be an integer");
			}
		}
	}
}
