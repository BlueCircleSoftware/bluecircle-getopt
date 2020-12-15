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

import java.util.List;

import com.bluecirclesoft.open.getopt.CommandLineProcessingException;

/**
 * Defines a parameter processing flavor. I.e., should it process like GNU getopt, tar, etc.
 */
public interface CommandLineProcessingFlavor {

	/**
	 * Process the command line
	 *
	 * @param params The command line parameters
	 * @return The remaining parameters after processing is over
	 * @throws CommandLineProcessingException if any of the command line processing semantics are
	 *                                        violated. The message will contain a usage message in
	 *                                        traditional unix style.
	 */
	List<String> processParams(String... params);

	/**
	 * Process the command line
	 *
	 * @param params The command line parameters
	 * @return The remaining parameters after processing is over
	 * @throws CommandLineProcessingException if any of the command line processing semantics are
	 *                                        violated
	 */
	List<String> processParams(List<String> params);
}
