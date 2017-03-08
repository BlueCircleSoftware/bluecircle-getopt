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

package test;

import java.util.List;

import com.bluecirclesoft.open.getopt.CommandLineProcessingException;
import com.bluecirclesoft.open.getopt.GetOpt;
import com.bluecirclesoft.open.getopt.UtilityOptions;

/**
 * Example code for the README.md file
 */
public class GetOptExample1 {

	private static boolean errored;

	public static boolean isErrored() {
		return errored;
	}

	public static void setErrored(boolean errored) {
		GetOptExample1.errored = errored;
	}

	public static void main(String... args) {
		final UtilityOptions options = new UtilityOptions();
		GetOpt getOpt = GetOpt.create("myutility", "file...");
		getOpt.addParam("file", "the input file (- for standard input)", false, options::setInput)
				.addShortOpt('i')
				.addLongOpt("input-file");
		getOpt.addParam("file", "the output file (- for standard output)", false, options::setOutput)
				.addShortOpt('o')
				.addLongOpt("output-file");
		getOpt.addFlag("produce verbose output", options::setVerbose).addShortOpt('v').addLongOpt("verbose");
		List<String> remainingCommandLine;
		try {
			remainingCommandLine = getOpt.processParams(args);
		} catch (CommandLineProcessingException e) {
			System.err.println(e.getMessage());
			errored = true;
			return;
		}

		if (options.isVerbose()) {
			System.out.println("Verbsoe on");
		} else {
			System.out.println("Verbose off");
		}
		System.out.print("Parameters: " + remainingCommandLine);
	}
}