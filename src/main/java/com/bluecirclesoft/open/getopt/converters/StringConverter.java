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

import com.bluecirclesoft.open.getopt.GetOpt;
import com.bluecirclesoft.open.getopt.OptionSpecification;
import com.bluecirclesoft.open.getopt.TypeConverter;

/**
 * Converter to "parse" parameter strings into Strings (pass-through).
 */
public class StringConverter implements TypeConverter<String> {

	/**
	 * Pass-through "converter" of a string.
	 *
	 * @param input the input string
	 * @return same string
	 */
	@Override
	public String convert(String input, GetOpt options, OptionSpecification option) {
		return input;
	}
}
