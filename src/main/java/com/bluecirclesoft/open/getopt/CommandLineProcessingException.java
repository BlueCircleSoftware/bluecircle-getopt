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

/**
 * This exception is thrown when there's an error in the user-supplied command line arguments (user
 * did not meet the expectation of the software).
 *
 * @see GetOptSetupException
 */

public class CommandLineProcessingException extends RuntimeException {

	public CommandLineProcessingException(String s, GetOpt options) {
		super(createExceptionMessage(s, options));
	}

	private static String createExceptionMessage(String s, GetOpt options) {
		StringBuilder sb = new StringBuilder();
		sb.append(s);
		sb.append("\n");
		options.usage(sb);
		return sb.toString();
	}
}
