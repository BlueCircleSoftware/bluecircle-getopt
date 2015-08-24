/*

Copyright 2015 Blue Circle Software, LLC.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 */

package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.CommandLineOptionException;
import com.bluecirclesoft.open.getopt.TypeConverter;

/**
 * Converter to parse parameter strings into Doubles. This converter uses  {@code
 * Double.parseDouble()}, so look there for all its peccadilloes.
 *
 * @see Double#parseDouble(String)
 */
public class DoubleConverter implements TypeConverter<Double> {

	/**
	 * Convert a string into a Double or null.
	 *
	 * @param input the input string
	 * @return {@code null} if {@code input} is null or blank, or a Double if the string is
	 * parseable
	 * @throws CommandLineOptionException if the parse fails
	 * @see Double#parseDouble(String)
	 */
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
