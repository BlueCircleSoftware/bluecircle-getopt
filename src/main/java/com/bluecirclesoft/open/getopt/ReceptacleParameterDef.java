package com.bluecirclesoft.open.getopt;

import java.util.function.Consumer;

/**
 * TODO document me
 */
class ReceptacleParameterDef<T> extends ParameterDef<T> {

	private Consumer<T> setter;

	private ReceptacleParameterDef(GetOpt parent, Class<T> type, String mnemonic,
	                               String documentation, Consumer<T> setter) {
		super(parent, type, mnemonic, documentation);
		this.setter = setter;
	}

	private ReceptacleParameterDef(GetOpt parent, Class<T> type, String mnemonic,
	                               String documentation, boolean required,
	                               TypeConverter<T> typeConverter, Consumer<T> setter) {
		super(parent, type, mnemonic, documentation, required, typeConverter);
		this.setter = setter;
	}

	public static ReceptacleParameterDef<Boolean> makeFlag(GetOpt parent, String documentation,
	                                              Consumer<Boolean> setter) {
		return new ReceptacleParameterDef<>(parent, Boolean.class, null, documentation, setter);
	}

	public static <M> ReceptacleParameterDef<M> makeParameter(GetOpt parent, Class<M> type,
	                                                          String mnemonic, String documentation,
	                                                          boolean required,
	                                                          TypeConverter<M> typeConverter,
	                                                          Consumer<M> setter) {
		return new ReceptacleParameterDef<>(parent, type, mnemonic, documentation, required,
				typeConverter, setter);
	}

	@Override
	public void setValue(Object value) {
		setter.accept((T) value);
	}

}
