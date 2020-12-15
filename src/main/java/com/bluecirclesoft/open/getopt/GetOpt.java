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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;

import com.bluecirclesoft.open.getopt.converters.ConverterUtil;
import com.bluecirclesoft.open.getopt.converters.UseTheDefaultConverter;
import com.bluecirclesoft.open.getopt.flavors.CommandLineProcessingFlavor;
import com.bluecirclesoft.open.getopt.flavors.CommandLineProcessingFlavors;

/**
 * Simple class for doing command line argument processing. <ul> <li>Supports both flags (option is
 * present/absent) and parameters (options that take a value).</li> <li>Supports both short
 * (<code><b>-a</b></code>) and long ( <code><b>--freakin-long-option</b></code>) options.</li>
 * <li>Short options may be glommed (<code><b>-abqv</b></code>), and if any options take parameters,
 * those will be read in sequence from the command line following the glommage.</li> <li>Long
 * options may be abbreviated to the point that they are still unique (this is determined by the
 * class at runtime; you don't have to worry about abbreviations).</li> <li>The stop-processing
 * option (<code><b>--</b></code>) is recognized and handled.</li> <li>If an unknown option is
 * passed, or if a long option is too abbreviated (no longer selects a distinct option), or if a
 * required parameter is left out, then a CommandLineOptionException is thrown. The exception's
 * message string contains an enumeration of all the problems found with the command line, and a
 * usage blurb is automatically generated based on the documentation given when adding the options.
 * The message includes newlines, and can just be written out to <code><b>System.err</b></code>.</li>
 * </ul> To use: <ol> <li>Add the options to the class using <code><b>add...()</b></code></li>
 * <li>Take your command line and pass it to <code><b>processParams()</b></code></li> <li>Read the
 * parameters out of the class using <code><b>get...Value()</b></code></li> <li>...</li>
 * <li>Profit!</li> </ol>
 * <p></p>
 * An example of the automatic usage statement (as rendered by a standard printStackTrace()):
 * <pre>
 *
 *         com.bluecirclesoft.open.getopt.GetOpt.CommandLineOptionException:
 *         error: Unknown option: -?
 *         error: required option --output-dir was not given
 *         usage:
 *         com.bluecirclesoft.open.getopt.ClassGenerator.Output.JavaNoMiddle -d &lt;directory&gt; [
 * -D &lt;type&gt; ]
 *         where:
 *         -d, --output-dir &lt;directory&gt;:
 *         (required) output the new classes/hierarchy to &lt;directory&gt;
 *         -D, --date-type &lt;type&gt;:
 *         the type to use for dates (default: javax.sql.Date)
 *
 *         at com.bluecirclesoft.open.getopt.GetOpt.GetOpt.processParams(GetOpt.java:459)
 *         at com.bluecirclesoft.open.getopt.ClassGenerator.Output.JavaNoMiddle.setParams(JavaNoMiddle.java:80)
 *         at com.bluecirclesoft.open.getopt.ClassGenerator.Generate.main(Generate.java:72)
 *
 * </pre>
 * And the code that generated it:
 * <pre>
 * GetOpt opts = new GetOpt(getClass().getName());
 *
 * opts.addParam('d', &quot;output-dir&quot;, &quot;directory&quot;,
 * 		&quot;output the new classes/hierarchy to &lt;directory&gt;&quot;, true);
 * opts.addParam('D', &quot;date-type&quot;, &quot;type&quot;, &quot;the type to use for dates
 * (default: &quot;
 * 		+ m_DateType + &quot;)&quot;, false);
 * opts.processParams(Options);
 * </pre>
 */

public class GetOpt {

	public static final CommandLineProcessingFlavors DEFAULT_FLAVOR = CommandLineProcessingFlavors.GNU_GETOPT;

	private final String programName;

	private final String restOfParamsDescription;

	private final CommandLineProcessingFlavor flavor;

	private final Map<Character, OptionSpecification> byShort_ = new HashMap<>();

	private final SortedMap<String, OptionSpecification> byLong_ = new TreeMap<>();

	private final Set<OptionSpecification> options = new HashSet<>();

	private boolean hasShortOpt(Character ch) {
		return byShort_.containsKey(ch);
	}

	public OptionSpecification getShortOptProcessing(Character ch) {
		OptionSpecification def = byShort_.get(ch);
		if (def == null) {
			throw new CommandLineProcessingException("No such option -" + ch, this);
		}
		return def;
	}

	private List<String> getLongMatches(String str) {
		SortedMap<String, OptionSpecification> tail = byLong_.tailMap(str);
		Iterator<Entry<String, OptionSpecification>> possibleMatchIt = tail.entrySet().iterator();
		List<String> possibleMatches = new ArrayList<>();
		while (possibleMatchIt.hasNext()) {
			Entry<String, OptionSpecification> ent = possibleMatchIt.next();
			if (ent.getKey().startsWith(str)) {
				possibleMatches.add(ent.getKey());
			} else {
				break;
			}
		}
		return possibleMatches;
	}

	public OptionSpecification getLongOpt(String str, Collection<String> problems) {
		if (byLong_.containsKey(str)) {
			return byLong_.get(str);
		}
		List<String> matches = getLongMatches(str);
		if (matches.isEmpty()) {
			problems.add("Unknown option: --" + str);
			return null;
		} else if (matches.size() > 1) {
			for (int i = 0; i < matches.size(); i++) {
				matches.set(i, "--" + matches.get(i));
			}
			problems.add("Option --" + str + " is not unique; it matches:");
			for (String match : matches) {
				problems.add("    --" + match);
			}
			return null;
		} else {
			return byLong_.get(matches.get(0));
		}
	}

	/**
	 * Constructor.
	 *
	 * @param programName The name of the program (for the usage message)
	 */
	private GetOpt(String programName, CommandLineProcessingFlavors flavor, String restOfParamsDescription) {
		this.programName = programName;
		this.flavor = flavor.getBuilder().apply(this);
		this.restOfParamsDescription = restOfParamsDescription;
	}

	/**
	 * Factory method.
	 *
	 * @param programName The name of the program (for the usage message)
	 */
	public static GetOpt create(String programName, String restOfParamsDescription) {
		return create(programName, restOfParamsDescription, DEFAULT_FLAVOR);
	}

	/**
	 * Constructor.
	 *
	 * @param mainClass The class name of the program (for the usage message)
	 */
	public static GetOpt create(Class<?> mainClass, String restOfParamsDescription) {
		return create(mainClass.getName(), restOfParamsDescription);
	}


	/**
	 * Factory method.
	 *
	 * @param programName The name of the program (for the usage message)
	 */
	public static GetOpt create(String programName, String restOfParamsDescription, CommandLineProcessingFlavors flavor) {
		return new GetOpt(programName, flavor, restOfParamsDescription);
	}

	/**
	 * Constructor.
	 *
	 * @param mainClass The class name of the program (for the usage message)
	 */
	public static GetOpt create(Class<?> mainClass, String restOfParamsDescription, CommandLineProcessingFlavors flavor) {
		return create(mainClass.getName(), restOfParamsDescription, flavor);
	}

	public static GetOpt createFromReceptacle(Object receptacle, String programName, String restOfParamsDescription) {
		GetOpt getOpt = create(programName, restOfParamsDescription);
		getOpt.defineFromClass(receptacle);
		return getOpt;
	}

	public static GetOpt createFromReceptacle(Object receptacle, Class<?> mainClass, String restOfParamsDescription) {
		GetOpt getOpt = create(mainClass, restOfParamsDescription);
		getOpt.defineFromClass(receptacle);
		return getOpt;
	}

	public static GetOpt createFromReceptacle(Object receptacle, String programName, String restOfParamsDescription,
	                                          CommandLineProcessingFlavors flavor) {
		GetOpt getOpt = create(programName, restOfParamsDescription, flavor);
		getOpt.defineFromClass(receptacle);
		return getOpt;
	}

	public static GetOpt createFromReceptacle(Object receptacle, Class<?> mainClass, String restOfParamsDescription,
	                                          CommandLineProcessingFlavors flavor) {
		GetOpt getOpt = create(mainClass, restOfParamsDescription, flavor);
		getOpt.defineFromClass(receptacle);
		return getOpt;
	}

	private void defineFromClass(Object receptacle) {
		Class<?> definitionClass = receptacle.getClass();
		for (Field field : definitionClass.getDeclaredFields()) {
			ByArgument byArgument = field.getAnnotation(ByArgument.class);
			ByFlag byFlag = field.getAnnotation(ByFlag.class);
			Class<?> type = field.getType();
			if (byArgument != null && byFlag != null) {
				throw new GetOptSetupException(
						"Both @ByFlag and @ByArgument set on " + field + "; " + "should be one or the other, but not both");
			} else if (byArgument != null) {
				processParameterAnnotation(byArgument, type, (Object newValue) -> {
					field.setAccessible(true);
					try {
						field.set(receptacle, newValue);
					} catch (IllegalAccessException e) {
						throw new GetOptSetupException("Exception setting field " + field, e);
					}
				});
			} else if (byFlag != null) {
				if (type != Boolean.class && type != Boolean.TYPE) {
					throw new GetOptSetupException("Field " + field + " must be boolean to " + "be annotated with @ByFlag");
				}
				processFlagAnnotation(byFlag, (Boolean newValue) -> {
					field.setAccessible(true);
					try {
						field.set(receptacle, newValue);
					} catch (IllegalAccessException e) {
						throw new GetOptSetupException("Exception setting field " + field, e);
					}
				});
			}
		}
		for (Method method : definitionClass.getDeclaredMethods()) {
			ByArgument byArgument = method.getAnnotation(ByArgument.class);
			ByFlag byFlag = method.getAnnotation(ByFlag.class);
			if (byArgument == null && byFlag == null) {
				continue;
			}
			if (byArgument != null && byFlag != null) {
				throw new GetOptSetupException(
						"Both @Flag and @Parameter set on " + method + "; " + "should be one or the other, but not both");
			}
			if (method.getReturnType() != Void.TYPE || method.getParameters().length > 1) {
				throw new GetOptSetupException("Method " + method + ": methods annotated " +
						"with @Flag or @Parameter must be 'setters'; that is, they must take " + "one parameter and return 'void'");
			}
			Class<?> type = method.getParameters()[0].getType();
			if (byArgument != null) {
				processParameterAnnotation(byArgument, type, (Object newValue) -> {
					method.setAccessible(true);
					try {
						method.invoke(receptacle, newValue);
					} catch (IllegalAccessException | InvocationTargetException e) {
						throw new GetOptSetupException("Exception invoking " + method, e);
					}
				});
			} else {
				if (type != Boolean.class && type != Boolean.TYPE) {
					throw new GetOptSetupException("Method " + method + " must take boolean to " + "be annotated with @Flag");
				}
				processFlagAnnotation(byFlag, (Boolean newValue) -> {
					method.setAccessible(true);
					try {
						method.invoke(receptacle, newValue);
					} catch (IllegalAccessException | InvocationTargetException e) {
						throw new GetOptSetupException("Exception invoking " + method, e);
					}
				});
			}
		}
	}

	private void processParameterAnnotation(ByArgument byArgument, Class<?> type, Consumer<Object> setter) {
		TypeConverter<?> converter;
		Class<? extends TypeConverter<?>> converterClass = byArgument.converter();
		if (byArgument.converter() != UseTheDefaultConverter.class) {
			try {
				converter = converterClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new GetOptSetupException("Cannot instantiate type converter " + converterClass, e);
			}
		} else {
			converter = ConverterUtil.getDefaultConverter(type);
			if (converter == null) {
				throw new GetOptSetupException("Could not find a type converter class for " + "type " + type.getName());
			}
		}

		OptionSpecification def =
				OptionSpecification.makeOption(this, byArgument.mnemonic(), byArgument.documentation(), byArgument.required(),
						ArgumentSpecification.REQUIRED, null,
						(argument, opt) -> setter.accept(converter.convert(argument, GetOpt.this, opt)));
		for (String opt : byArgument.shortOpt()) {
			Character shortOptChar = null;
			if (opt != null && !opt.isEmpty()) {
				if (opt.length() > 1) {
					throw new GetOptSetupException("Short option string " + opt + " is more than one character");
				}
				shortOptChar = opt.charAt(0);
			}
			if (shortOptChar != null) {
				def.addShortOpt(shortOptChar);
			}
		}
		for (String opt : byArgument.longOpt()) {
			if (opt != null && !opt.isEmpty()) {
				def.addLongOpt(opt);
			}
		}
	}

	private void processFlagAnnotation(ByFlag byFlag, Consumer<Boolean> setter) {
		OptionSpecification def = OptionSpecification.makeFlag(this, byFlag.documentation(), setter);
		for (String opt : byFlag.shortOpt()) {
			Character shortOptChar = null;
			if (opt != null && !opt.isEmpty()) {
				if (opt.length() > 1) {
					throw new GetOptSetupException("Short option string '" + opt + "' is more than one character");
				}
				shortOptChar = opt.charAt(0);
			}
			if (shortOptChar != null) {
				def.addShortOpt(shortOptChar);
			}
		}
		for (String opt : byFlag.longOpt()) {
			if (opt != null && !opt.isEmpty()) {
				def.addLongOpt(opt);
			}
		}
	}

	/**
	 * Add an option without an argument (a flag - true or false)
	 *
	 * @param documentation Documentation to display when generating the usage message. May not be
	 *                      null.
	 * @throws IllegalArgumentException if both ShortOpt and LongOpt are null
	 * @throws IllegalArgumentException if Documentation is null or zero length (Work with me here!
	 *                                  I'm building a freakin' usage message for you!)
	 * @throws IllegalArgumentException if either ShortOpt or LongOpt has already been added
	 */

	public OptionSpecification addFlag(String documentation, Consumer<Boolean> onEncounter) {
		// Step 1 - determine whether we need to throw any exceptions,
		// so that 'this' will be left in a consistent state

		if (documentation == null || documentation.isEmpty()) {
			throw new GetOptSetupException("documentation is not specified");
		}

		// Step 2 - add the options to this
		OptionSpecification ph = OptionSpecification.makeFlag(this, documentation, onEncounter);
		options.add(ph);
		return ph;
	}

	/**
	 * Add an option with argument (with a value to be passed in).
	 *
	 * @param paramMnemonic A short name for the parameter. May not be null.
	 * @param documentation Documentation to display when generating the usage message. May not be
	 *                      null.
	 * @param required      Whether the option is required to be specified on the command line
	 * @param type          the type that the argument should be converted to
	 * @param onEncounter   the function to invoke when the option is encountered
	 * @throws GetOptSetupException if documentation is null or zero length
	 * @throws GetOptSetupException if paramMnemonic is null or zero length
	 */
	public <T> OptionSpecification addParam(String paramMnemonic, String documentation, boolean required, Class<T> type,
	                                        Consumer<T> onEncounter) {
		if (documentation == null || documentation.isEmpty()) {
			throw new GetOptSetupException("documentation is not specified");
		}
		if (paramMnemonic == null || paramMnemonic.isEmpty()) {
			throw new GetOptSetupException("argument mnemonic is not specified");
		}

		OptionSpecification ph =
				OptionSpecification.makeOption(this, paramMnemonic, documentation, required, ArgumentSpecification.REQUIRED, null,
						(argument, opt) -> onEncounter.accept(ConverterUtil.getDefaultConverter(type).convert(argument, this, opt)));
		options.add(ph);
		return ph;
	}

	/**
	 * Add an option with argument (with a value to be passed in).
	 *
	 * @param paramMnemonic A short name for the parameter. May not be null.
	 * @param documentation Documentation to display when generating the usage message. May not be
	 *                      null.
	 * @param required      Whether the option is required to be specified on the command line
	 * @param onEncounter   the function to invoke when the option is encountered
	 * @throws GetOptSetupException if documentation is null or zero length
	 * @throws GetOptSetupException if paramMnemonic is null or zero length
	 */
	public OptionSpecification addParam(String paramMnemonic, String documentation, boolean required, Consumer<String> onEncounter) {
		return addParam(paramMnemonic, documentation, required, String.class, onEncounter);
	}

	public String usage() {
		StringBuilder stringBuilder = new StringBuilder();
		usage(stringBuilder);
		return stringBuilder.toString();
	}

	/**
	 * Generate usage string.
	 *
	 * @param errStr the output string builder
	 */
	public void usage(StringBuilder errStr) {
		errStr.append("usage:\n");
		errStr.append(programName);

		SortedMap<String, OptionSpecification> shortFlagsAlpha = new TreeMap<>();

		// dump all the short options
		for (OptionSpecification def : options) {
			if (def.getShortOptList().size() != 0 && def.isFlag()) {
				shortFlagsAlpha.put(String.valueOf(def.getShortOptList().get(0)), def);
			}
		}

		Set<OptionSpecification> sampleCommandDisplayed = new HashSet<>();
		boolean needsDash = true;
		for (Entry<String, OptionSpecification> entry : shortFlagsAlpha.entrySet()) {
			if (needsDash) {
				errStr.append(" -");
				needsDash = false;
			}
			errStr.append(entry.getKey());
			sampleCommandDisplayed.add(entry.getValue());
		}

		// now dump any long options that we haven't already dumped
		SortedMap<String, OptionSpecification> otherOptsAlpha = new TreeMap<>();
		for (OptionSpecification def : options) {
			if (!sampleCommandDisplayed.contains(def)) {
				if (def.getShortOptList().size() != 0) {
					otherOptsAlpha.put(String.valueOf(def.getShortOptList().get(0)), def);
				} else if (def.getLongOptList().size() != 0) {
					otherOptsAlpha.put(def.getLongOptList().get(0), def);
				}
			}
		}

		for (Entry<String, OptionSpecification> entry : otherOptsAlpha.entrySet()) {
			OptionSpecification def = entry.getValue();
			if (def.isCommandLineOptional()) {
				errStr.append(" [");
			}
			errStr.append(" --");
			errStr.append(entry.getKey());
			String mnemonic = def.getMnemonic();
			if (mnemonic != null) {
				errStr.append(" <").append(mnemonic).append(">");
			}
			if (def.isCommandLineOptional()) {
				errStr.append(" ]");
			}
		}

		if (restOfParamsDescription != null) {
			errStr.append(" ");
			errStr.append(restOfParamsDescription);
		}

		// generate long descriptions
		SortedMap<String, OptionSpecification> all = new TreeMap<>();
		for (OptionSpecification option : options) {
			if (option.getShortOptList().size() != 0) {
				all.put("-" + option.getShortOptList().get(0), option);
			} else if (option.getLongOptList().size() != 0) {
				all.put("--" + option.getLongOptList().get(0), option);
			}
		}

		for (OptionSpecification def : all.values()) {
			ParameterDescription desc = def.getDescription();
			errStr.append('\n');
			for (String opt : desc.getOptionDescriptions()) {
				errStr.append("  ");
				errStr.append(opt);
				errStr.append('\n');
			}
			for (String doc : desc.getBrokenDocumentation()) {
				errStr.append("    ");
				errStr.append(doc);
				errStr.append('\n');
			}
		}
	}

	public void addShortOpt(OptionSpecification optionSpecification, Character opt) {
		if (hasShortOpt(opt)) {
			throw new GetOptSetupException("Short option -" + opt + " specified more than once");
		}
		byShort_.put(opt, optionSpecification);
		options.add(optionSpecification);
	}

	public void addLongOpt(OptionSpecification optionSpecification, String opt) {
		if (byLong_.containsKey(opt)) {
			throw new GetOptSetupException("Long option " + opt + " has already been defined");
		}
		byLong_.put(opt, optionSpecification);
		options.add(optionSpecification);
	}

	public boolean isFlagSet(char shortOpt) {
		OptionSpecification optionSpecification = byShort_.get(shortOpt);
		if (optionSpecification == null) {
			throw new GetOptSetupException("Short option '" + shortOpt + "' not defined");
		}
		return optionSpecification.isSpecified();
	}

	public Set<OptionSpecification> getOptions() {
		return options;
	}

	/**
	 * Process the command line
	 *
	 * @param params The command line parameters
	 * @return The remaining parameters after processing is over
	 * @throws CommandLineProcessingException if any of the command line processing semantics are
	 *                                        violated. The message will contain a usage message in
	 *                                        traditional unix style.
	 */
	public List<String> processParams(String... params) {
		return flavor.processParams(params);
	}

	/**
	 * Process the command line
	 *
	 * @param params The command line parameters
	 * @return The remaining parameters after processing is over
	 * @throws CommandLineProcessingException if any of the command line processing semantics are
	 *                                        violated
	 */
	public List<String> processParams(List<String> params) {
		return flavor.processParams(params);
	}

}
