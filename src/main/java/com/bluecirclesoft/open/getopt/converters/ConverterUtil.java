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

import com.bluecirclesoft.open.getopt.TypeConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * String utility class (to avoid bringing in Apache Commons)
 */
public final class ConverterUtil {

	private static final HashMap<Class, TypeConverter> DEFAULT_CONVERTERS = new HashMap<>();

	static {
		DEFAULT_CONVERTERS.put(Boolean.class, new BooleanConverter());
		DEFAULT_CONVERTERS.put(Byte.class, new ByteConverter());
		DEFAULT_CONVERTERS.put(Character.class, new CharConverter());
		DEFAULT_CONVERTERS.put(Double.class, new DoubleConverter());
		DEFAULT_CONVERTERS.put(Float.class, new FloatConverter());
		DEFAULT_CONVERTERS.put(Integer.class, new IntegerConverter());
		DEFAULT_CONVERTERS.put(Long.class, new LongConverter());
		DEFAULT_CONVERTERS.put(Short.class, new ShortConverter());

		DEFAULT_CONVERTERS.put(Boolean.TYPE, new BooleanConverter());
		DEFAULT_CONVERTERS.put(Byte.TYPE, new ByteConverter());
		DEFAULT_CONVERTERS.put(Character.TYPE, new CharConverter());
		DEFAULT_CONVERTERS.put(Double.TYPE, new DoubleConverter());
		DEFAULT_CONVERTERS.put(Float.TYPE, new FloatConverter());
		DEFAULT_CONVERTERS.put(Integer.TYPE, new IntegerConverter());
		DEFAULT_CONVERTERS.put(Long.TYPE, new LongConverter());
		DEFAULT_CONVERTERS.put(Short.TYPE, new ShortConverter());

		DEFAULT_CONVERTERS.put(String.class, new StringConverter());
		DEFAULT_CONVERTERS.put(BigDecimal.class, new BigDecimalConverter());
		DEFAULT_CONVERTERS.put(BigInteger.class, new BigIntegerConverter());
	}

	private ConverterUtil() {
	}

	/**
	 * Returns true if the string is null or only contains spaces
	 *
	 * @param testString the string to test
	 * @return true or false
	 */
	public static boolean isEmpty(CharSequence testString) {
		if (testString == null) {
			return true;
		}
		for (int i = 0; i < testString.length(); i++) {
			if (!Character.isWhitespace(testString.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Get the default converter for a given class,
	 *
	 * @param paramClass the class
	 * @param <T>        the specific type of the class
	 * @return the converter, or null if the class has no default converter
	 */
	public static <T> TypeConverter<T> getDefaultConverter(Class<T> paramClass) {

		return DEFAULT_CONVERTERS.get(paramClass);

	}

}
