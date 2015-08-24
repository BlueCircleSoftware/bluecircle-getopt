/*

Copyright 2015 Blue Circle Software, LLC.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 */

package com.bluecirclesoft.open.getopt;

import java.util.ArrayList;
import java.util.List;

abstract class ParameterDef<T> {

	private final List<Character> shortOptList = new ArrayList<>();

	private final List<String> longOptList = new ArrayList<>();

	private final boolean takesParam;

	private final boolean required;

	private final String mnemonic;

	private final String documentation;

	private final Class<T> type;

	private final TypeConverter<T> typeConverter;

	private boolean set = false;

	private final GetOpt parent;

	/**
	 * Flag form
	 *
	 * @param parent
	 * @param type
	 * @param mnemonic
	 * @param documentation
	 */
	public ParameterDef(GetOpt parent, Class<T> type, String mnemonic, String documentation) {
		this.parent = parent;
		this.type = type;
		this.takesParam = false;
		this.required = false;
		this.mnemonic = mnemonic;
		this.documentation = documentation;
		this.typeConverter = null;
	}

	/**
	 * Parameter form
	 *
	 * @param parent
	 * @param type
	 * @param mnemonic
	 * @param documentation
	 * @param required
	 * @param typeConverter
	 */
	public ParameterDef(GetOpt parent, Class<T> type, String mnemonic, String documentation,
	                    boolean required, TypeConverter<T> typeConverter) {
		this.parent = parent;
		this.type = type;
		this.takesParam = true;
		this.required = required;
		this.mnemonic = mnemonic;
		this.documentation = documentation;
		this.typeConverter = typeConverter;
	}

	public ParameterDef addShortOpt(Character opt) throws OptionProcessingException {
		parent.addShortOpt(this, opt);
		shortOptList.add(opt);
		return this;
	}

	public ParameterDef addLongOpt(String opt) throws OptionProcessingException {
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

	public boolean isTakesParam() {
		return takesParam;
	}

	public boolean isRequired() {
		return required;
	}

	public String getMnemonic() {
		return mnemonic;
	}

	public String getDocumentation() {
		return documentation;
	}

	private String makeOptionDescriptor() {
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

	public Class<T> getType() {
		return type;
	}

	protected abstract void setValue(Object value);

	void setParameterValue(String parameterValue) throws CommandLineOptionException {
		assert takesParam;
		if (typeConverter == null) {
			throw new OptionProcessingException("No type converter defined for parameter " + this);
		}
		set = true;
		setValue(typeConverter.convert(parameterValue));
	}

	void setFlagPresent() {
		set = true;
		setValue(true);
	}

	public boolean isSet() {
		return set;
	}
}
