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

import com.bluecirclesoft.open.getopt.CommandLineProcessingException;
import com.bluecirclesoft.open.getopt.GetOpt;

import java.util.List;

/**
 * Example code for the README.md file
 */
public class GetOptExample2 {

	public static int main(String... args) {
		final UtilityOptions options = new UtilityOptions();
		GetOpt getOpt = GetOpt.createFromReceptacle(options, "myUtility [OPTIONS] file...");
		List<String> remainingCommandLine;
		try {
			remainingCommandLine = getOpt.processParams(args);
		} catch (CommandLineProcessingException e) {
			System.err.println(e.getMessage());
			return 1;
		}

		if (options.isVerbose()) {
			System.out.println("Verbsoe on");
		} else {
			System.out.println("Verbose off");
		}

		System.out.print("Parameters: " + remainingCommandLine);

		return 0;
	}
}
