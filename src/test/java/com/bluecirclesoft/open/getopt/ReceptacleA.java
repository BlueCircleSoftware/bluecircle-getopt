package com.bluecirclesoft.open.getopt;

/**
 * TODO document me
 */
public class ReceptacleA {

	@Parameter(shortOpt = "a", longOpt = "option-one", mnemonic = "opt", documentation = "value " +
			"for option one")
	private String opt1;

	@Flag(shortOpt = "b", documentation = "option 2?")
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

	@Parameter(shortOpt = "c", longOpt = {"opt3", "option-3"}, documentation = "Option 3",
			mnemonic = "o3")
	public void setOpt3(short opt3) {
		this.opt3 = opt3;
	}
}
