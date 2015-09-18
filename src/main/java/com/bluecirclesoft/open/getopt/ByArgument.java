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

package com.bluecirclesoft.open.getopt;

import com.bluecirclesoft.open.getopt.converters.UseTheDefaultConverter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to place on a field or setter. This allows GetOpt to dispatch the parameters to the
 * appropriate locations.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ByArgument {

	/**
	 * The short (one-dash, one character) option(s) for this parameter. At least one short or long
	 * option must be specified.
	 *
	 * @return the short option character(s), or {} if no short option is desired.
	 */
	String[] shortOpt() default {};

	/**
	 * The long (two-dash, long string) option(s) for this parameter. At least one short or long
	 * option must be specified.
	 *
	 * @return the long option string(s), or {} if no long option is desired.
	 */
	String[] longOpt() default {};

	/**
	 * A mnemonic to use as a place holder for the specified value for the documentation. This is
	 * required.
	 *
	 * @return the mnemonic
	 */
	String mnemonic();

	/**
	 * The documentation for this option (put into the usage). This is required
	 *
	 * @return the documentation string
	 */
	String documentation();

	/**
	 * Is this parameter required? If the annotated field/method is primitive, then it is always
	 * required.
	 *
	 * @return yes or no
	 */
	boolean required() default false;

	/**
	 * A converter that will parse the string value into the correct type.
	 *
	 * @return the converter
	 */
	Class<? extends TypeConverter> converter() default UseTheDefaultConverter.class;
}
