package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.TypeConverter;

/**
 * TODO document me
 */
public class StringConverter implements TypeConverter<String> {

	@Override
	public String convert(String input) {
		if (input == null) {
			return null;
		} else {
			String result = input.trim();
			if (input.isEmpty()) {
				return null;
			} else {
				return result;
			}
		}
	}
}
