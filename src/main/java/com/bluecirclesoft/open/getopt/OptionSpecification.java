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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OptionSpecification {

	private final List<Character> shortOptList = new ArrayList<>();

	private final List<String> longOptList = new ArrayList<>();

	private final ArgumentSpecification argumentSpecification;

	private final boolean required;

	private final String mnemonic;

	private final String documentation;

	private boolean specified = false;

	private final GetOpt parent;

	private final List<Consumer<Boolean>> onEncounterNoArgument = new ArrayList<>();

	private final List<Consumer<String>> onEncounterWithArgument = new ArrayList<>();

	/**
	 * Constructor
	 *
	 * @param parent                the option processor that created this instance
	 * @param required              is this option required to be specified?
	 * @param argumentSpecification is there an argument for this option
	 * @param mnemonic              the mnemonic for the option for usage
	 * @param documentation         the documentation for the option for usage
	 * @param onEncounterNoArg      the function to call when this option is encountered on the
	 *                              command line (with no argument)
	 * @param onEncounterWithArg    the function to call when this option is encountered on the
	 *                              command line (with an argument)
	 */
	OptionSpecification(GetOpt parent, boolean required,
	                    ArgumentSpecification argumentSpecification, String mnemonic,
	                    String documentation, Consumer<Boolean> onEncounterNoArg,
	                    Consumer<String> onEncounterWithArg) {
		switch (argumentSpecification) {
			case NONE:
				if (onEncounterNoArg == null) {
					throw new GetOptSetupException("onEncounterNoArg must be set if " +
							"argumentSpecification is " + argumentSpecification);
				}
				if (onEncounterWithArg != null) {
					throw new GetOptSetupException("onEncounterWithArg cannot be set if " +
							"argumentSpecification is " + argumentSpecification);
				}
				break;
			case REQUIRED:
				if (onEncounterNoArg != null) {
					throw new GetOptSetupException("onEncounterNoArg cannot be set if " +
							"argumentSpecification is " + argumentSpecification);
				}
				if (onEncounterWithArg == null) {
					throw new GetOptSetupException("onEncounterWithArg must be set if " +
							"argumentSpecification is " + argumentSpecification);
				}
				break;
			case OPTIONAL:
				if (onEncounterNoArg == null) {
					throw new GetOptSetupException("onEncounterNoArg must be set if " +
							"argumentSpecification is " + argumentSpecification);
				}
				if (onEncounterWithArg == null) {
					throw new GetOptSetupException("onEncounterWithArg must be set if " +
							"argumentSpecification is " + argumentSpecification);
				}
				break;
			default:
				throw new GetOptSetupException(
						"Internal error: unhandled argumentSpecification:" + argumentSpecification);
		}
		this.parent = parent;
		this.argumentSpecification = argumentSpecification;
		this.required = required;
		this.mnemonic = mnemonic;
		this.documentation = documentation;
		onEncounterNoArgument.add(onEncounterNoArg);
		onEncounterWithArgument.add(onEncounterWithArg);
	}

	public OptionSpecification addShortOpt(Character opt) {
		parent.addShortOpt(this, opt);
		shortOptList.add(opt);
		return this;
	}

	public OptionSpecification addLongOpt(String opt) {
		parent.addLongOpt(this, opt);
		longOptList.add(opt);
		return this;
	}

	public List<Character> getShortOptList() {
		return shortOptList;
	}

	public List<String> getLongOptList() {
		return longOptList;
	}

	public boolean isRequired() {
		return required;
	}

	public ArgumentSpecification getArgumentSpecification() {
		return argumentSpecification;
	}

	public String getMnemonic() {
		return mnemonic;
	}

	public String getDocumentation() {
		return documentation;
	}

	public String makeOptionDescriptor() {
		if (!shortOptList.isEmpty()) {
			return "-" + shortOptList.get(0);
		} else {
			return "--" + longOptList.get(0);
		}
	}

	ParameterDescription getDescription() {
		ParameterDescription result = new ParameterDescription();
		for (Character shortOpt : shortOptList) {
			result.addOptionDescription("-" + shortOpt, mnemonic);
		}
		for (String longOpt : longOptList) {
			result.addOptionDescription("--" + longOpt, mnemonic);
		}
		result.setDocumentation((required ? "(required) " : "") + documentation);
		return result;
	}

	public void encounter(String argument) {
		if (argumentSpecification == ArgumentSpecification.NONE) {
			throw new GetOptSetupException(
					"Option " + makeOptionDescriptor() + " does not take an argument");
		}
		specified = true;
		for (Consumer<String> acceptor : onEncounterWithArgument) {
			acceptor.accept(argument);
		}
	}

	public void encounter(boolean on) {
		if (argumentSpecification == ArgumentSpecification.REQUIRED) {
			throw new GetOptSetupException(
					"Option " + makeOptionDescriptor() + " requires an argument");
		}
		specified = true;
		for (Consumer<Boolean> acceptor : onEncounterNoArgument) {
			acceptor.accept(on);
		}
	}

	/**
	 * Was this parameter specified on the command line?
	 *
	 * @return yes or no
	 */
	public boolean isSpecified() {
		return specified;
	}

	public static OptionSpecification makeFlag(GetOpt parent, String documentation,
	                                           Consumer<Boolean> onEncounterNoArgument) {
		return new OptionSpecification(parent, false, ArgumentSpecification.NONE, null,
				documentation, onEncounterNoArgument, null);
	}

	public static OptionSpecification makeOption(GetOpt parent, String mnemonic,
	                                             String documentation, boolean required,
	                                             ArgumentSpecification argumentSpecification,
	                                             Consumer<Boolean> onEncounterNoArgument,
	                                             Consumer<String> onEncounterWithArgument) {
		return new OptionSpecification(parent, required, argumentSpecification, mnemonic,
				documentation, onEncounterNoArgument, onEncounterWithArgument);
	}

	public OptionReceiver<Boolean> makeFlagReceiver() {
		final OptionReceiver<Boolean> receiver = new OptionReceiver<>();
		onEncounterNoArgument.add(receiver::setResult);
		return receiver;
	}

	public OptionReceiver<String> makeArgumentReceiver() {
		final OptionReceiver<String> receiver = new OptionReceiver<>();
		onEncounterWithArgument.add(receiver::addResult);
		return receiver;
	}
}


