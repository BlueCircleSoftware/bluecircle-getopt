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
 * Test receptacle for the GNU getopt flavor tests
 */
public class GnuReceptacle {

	@ByArgument(documentation = "the a value", mnemonic = "value", shortOpt = "a", longOpt = "a-value")
	private String a;

	@ByArgument(documentation = "the b value", mnemonic = "value", shortOpt = "b", longOpt = "b-value")
	private String b;

	@ByArgument(documentation = "the c value", mnemonic = "value", shortOpt = "c", longOpt = "c-value")
	private String c;

	@ByFlag(documentation = "the m flag", shortOpt = "m", longOpt = "m-flag")
	private boolean m;

	@ByArgument(documentation = "the abc value", mnemonic = "value", longOpt = "abc")
	private String abc;

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getAbc() {
		return abc;
	}

	public void setAbc(String abc) {
		this.abc = abc;
	}

	public boolean isM() {
		return m;
	}

	public void setM(boolean m) {
		this.m = m;
	}
}
