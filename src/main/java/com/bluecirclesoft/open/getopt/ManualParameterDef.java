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

import com.bluecirclesoft.open.getopt.converters.ConverterUtil;

/**
 * TODO document me
 */
class ManualParameterDef<T> extends ParameterDef<T> {

	private T rawValue;

	private ManualParameterDef(GetOpt parent, Class<T> type, String mnemonic, String documentation) {
		super(parent, type, mnemonic, documentation);
	}

	private ManualParameterDef(GetOpt parent, Class<T> type, String mnemonic, String documentation,
	                           boolean required, TypeConverter<T> typeConverter) {
		super(parent, type, mnemonic, documentation, required, typeConverter);
	}

	public static <M> ManualParameterDef<M> makeFlag(GetOpt parent, Class<M> type, String mnemonic,
	                                           String documentation) {
		return new ManualParameterDef<>(parent, type, mnemonic, documentation);
	}

	public static <M> ManualParameterDef<M> makeParameter(GetOpt parent, Class<M> type, String mnemonic,
	                                                String documentation, boolean required) {
		TypeConverter<M> defaultConverter = ConverterUtil.getDefaultConverter(type);
		if (defaultConverter == null) {
			throw new OptionProcessingException("Class " + type.getName() + " has no default " +
					"converter");
		}
		return new ManualParameterDef<>(parent, type, mnemonic, documentation, required,
				defaultConverter);
	}

	public static <M> ManualParameterDef<M> makeParameter(GetOpt parent, Class<M> type, String mnemonic,
	                                                String documentation, boolean required,
	                                                TypeConverter<M> typeConverter) {
		return new ManualParameterDef<>(parent, type, mnemonic, documentation, required, typeConverter);
	}

	public <W> W getValue(Class<W> castClass) {
		return castClass.cast(rawValue);
	}

	@Override
	public void setValue(Object value) {
		rawValue = getType().cast(value);
	}

}
