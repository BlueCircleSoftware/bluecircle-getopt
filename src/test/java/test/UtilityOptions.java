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

import com.bluecirclesoft.open.getopt.ByArgument;
import com.bluecirclesoft.open.getopt.ByFlag;

public class UtilityOptions {

	@ByArgument(mnemonic = "file", documentation = "the input file (- for standard input)", shortOpt = "i", longOpt = "input-file")
	private String input = "-";

	@ByArgument(mnemonic = "file", documentation = "the output file (- for standard input)", shortOpt = "o", longOpt = "output-file")
	private String output = "-";

	@ByFlag(documentation = "produce verbose output", shortOpt = "v", longOpt = "verbose")
	private boolean verbose = false;

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
}
