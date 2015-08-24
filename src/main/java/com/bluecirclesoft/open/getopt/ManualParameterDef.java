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
