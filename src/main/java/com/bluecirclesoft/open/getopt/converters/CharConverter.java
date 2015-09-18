/*
 * Copyright 2015 Blue Circle Software, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bluecirclesoft.open.getopt.converters;

import com.bluecirclesoft.open.getopt.CommandLineProcessingException;
import com.bluecirclesoft.open.getopt.TypeConverter;

/**
 * Converter to parse parameter strings into Characters.
 */
public class CharConverter implements TypeConverter<Character> {

	/**
	 * Convert a string into a Character or null.
	 *
	 * @param input the input string (<em>this will NOT be trimmed</em>)
	 * @return {@code null} if {@code input} is null or "", or the character in the string.
	 * @throws CommandLineProcessingException if the string has more than one character
	 */
	@Override
	public Character convert(String input) {
		if (input == null || input.isEmpty()) {
			return null;
		} else if (input.length() > 1) {
			throw new CommandLineProcessingException("Parameter can only be one character");
		} else {
			return input.charAt(0);
		}
	}
}
