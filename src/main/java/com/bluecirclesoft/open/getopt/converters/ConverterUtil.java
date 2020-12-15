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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import com.bluecirclesoft.open.getopt.TypeConverter;

/**
 * String utility class (to avoid bringing in Apache Commons)
 */
public final class ConverterUtil {

	private static final HashMap<Class<?>, TypeConverter<?>> DEFAULT_CONVERTERS = new HashMap<>();

	private static <T> void addConverter(Class<T> typeClass, TypeConverter<T> converter) {
		DEFAULT_CONVERTERS.put(typeClass, converter);
	}

	static {
		addConverter(Boolean.class, new BooleanConverter());
		addConverter(Byte.class, new ByteConverter());
		addConverter(Character.class, new CharConverter());
		addConverter(Double.class, new DoubleConverter());
		addConverter(Float.class, new FloatConverter());
		addConverter(Integer.class, new IntegerConverter());
		addConverter(Long.class, new LongConverter());
		addConverter(Short.class, new ShortConverter());

		addConverter(Boolean.TYPE, new BooleanConverter());
		addConverter(Byte.TYPE, new ByteConverter());
		addConverter(Character.TYPE, new CharConverter());
		addConverter(Double.TYPE, new DoubleConverter());
		addConverter(Float.TYPE, new FloatConverter());
		addConverter(Integer.TYPE, new IntegerConverter());
		addConverter(Long.TYPE, new LongConverter());
		addConverter(Short.TYPE, new ShortConverter());

		addConverter(String.class, new StringConverter());
		addConverter(BigDecimal.class, new BigDecimalConverter());
		addConverter(BigInteger.class, new BigIntegerConverter());
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

		return (TypeConverter<T>) DEFAULT_CONVERTERS.get(paramClass);

	}

}
