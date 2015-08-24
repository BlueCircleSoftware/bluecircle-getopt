package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.TypeConverter;

/**
 * TODO document me
 */
public class BooleanConverter implements TypeConverter<Boolean> {

	@Override
	public Boolean convert(String input) {
		if (ConverterUtil.isEmpty(input)) {
			return null;
		} else {
			return Boolean.parseBoolean(input);
		}
	}
}
