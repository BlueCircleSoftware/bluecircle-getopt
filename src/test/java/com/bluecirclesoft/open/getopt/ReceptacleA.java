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
 * TODO document me
 */
public class ReceptacleA {

	@ByArgument(shortOpt = "a", longOpt = "option-one", mnemonic = "opt", documentation = "value for option one")
	private String opt1;

	@ByFlag(shortOpt = "b", documentation = "option 2?")
	private boolean opt2;

	private short opt3;

	public String getOpt1() {
		return opt1;
	}

	public void setOpt1(String opt1) {
		this.opt1 = opt1;
	}

	public boolean isOpt2() {
		return opt2;
	}

	public void setOpt2(boolean opt2) {
		this.opt2 = opt2;
	}

	public short getOpt3() {
		return opt3;
	}

	@ByArgument(shortOpt = "c", longOpt = {"opt3", "option-3"}, documentation = "Option 3", mnemonic = "o3")
	public void setOpt3(short opt3) {
		this.opt3 = opt3;
	}
}
