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

package com.bluecirclesoft.open.getopt.flavors;

import java.util.function.Function;

import com.bluecirclesoft.open.getopt.GetOpt;

/**
 * Lists the flavors of command-line processing
 */
public enum CommandLineProcessingFlavors {
	/**
	 * GNU getopt-style, stop processing options as soon as the first non-option argument is
	 * encountered.
	 */
	GNU_GETOPT_POSIXLY_CORRECT(getOpt -> new GnuGetoptFlavor(getOpt, false)),
	/**
	 * GNU getopt-style, process all options on the command line, only stopping for '--'
	 */
	GNU_GETOPT(getOpt -> new GnuGetoptFlavor(getOpt, true)),
	;

	private final Function<GetOpt, CommandLineProcessingFlavor> builder;

	CommandLineProcessingFlavors(Function<GetOpt, CommandLineProcessingFlavor> builder) {
		this.builder = builder;
	}

	public Function<GetOpt, CommandLineProcessingFlavor> getBuilder() {
		return builder;
	}
}
