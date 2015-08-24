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

package com.bluecirclesoft.open.getopt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to define a flag to place on a boolean field or setter. This allows GetOpt to
 * dispatch the parameters to the appropriate locations.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Flag {

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
	 * The documentation for this option (put into the usage)
	 *
	 * @return the documentation string
	 */
	String documentation();
}
