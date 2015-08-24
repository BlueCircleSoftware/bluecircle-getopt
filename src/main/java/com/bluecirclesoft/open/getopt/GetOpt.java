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
import com.bluecirclesoft.open.getopt.converters.UseTheDefaultConverter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
 * <p/>
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

	private final String programName;

	private final Map<Character, ParameterDef<?>> byShort_ = new HashMap<>();

	private final SortedMap<String, ParameterDef<?>> byLong_ = new TreeMap<>();

	private final Set<ParameterDef> options = new HashSet<>();

	private boolean hasShortOpt(Character ch) {
		return byShort_.containsKey(ch);
	}

	private ParameterDef<?> getShortOptSetup(Character ch) {
		ParameterDef<?> def = byShort_.get(ch);
		if (def == null) {
			throw new OptionProcessingException("No such option -" + ch);
		}
		return def;
	}

	private ParameterDef<?> getShortOptProcessing(Character ch) throws CommandLineOptionException {
		ParameterDef<?> def = byShort_.get(ch);
		if (def == null) {
			throw new CommandLineOptionException("No such option -" + ch);
		}
		return def;
	}

	private List<String> getLongMatches(String str) {
		SortedMap<String, ParameterDef<?>> tail = byLong_.tailMap(str);
		Iterator<Entry<String, ParameterDef<?>>> possibleMatchIt = tail.entrySet().iterator();
		List<String> possibleMatches = new ArrayList<>();
		while (possibleMatchIt.hasNext()) {
			Entry<String, ParameterDef<?>> ent = possibleMatchIt.next();
			if (ent.getKey().startsWith(str)) {
				possibleMatches.add(ent.getKey());
			} else {
				break;
			}
		}
		return possibleMatches;
	}

	private ParameterDef<?> getLongOpt(String str, Collection<String> problems) {
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
	private GetOpt(String programName) {
		this.programName = programName;
	}

	/**
	 * Factory method.
	 *
	 * @param programName The name of the program (for the usage message)
	 */
	public static GetOpt create(String programName) {
		return new GetOpt(programName);
	}

	/**
	 * Constructor.
	 *
	 * @param mainClass The class name of the program (for the usage message)
	 */
	public static GetOpt create(Class mainClass) {
		return create(mainClass.getName());
	}

	public static GetOpt createFromReceptacle(Object receptacle, String programName) {
		GetOpt getOpt = create(programName);
		getOpt.defineFromClass(receptacle);
		return getOpt;
	}

	public static GetOpt createFromReceptacle(Object receptacle, Class mainClass) {
		GetOpt getOpt = create(mainClass);
		getOpt.defineFromClass(receptacle);
		return getOpt;
	}

	private void defineFromClass(Object receptacle) {
		Class definitionClass = receptacle.getClass();
		for (Field field : definitionClass.getDeclaredFields()) {
			Parameter parameter = field.getAnnotation(Parameter.class);
			Flag flag = field.getAnnotation(Flag.class);
			Class type = field.getType();
			if (parameter != null && flag != null) {
				throw new OptionProcessingException(
						"Both @Flag and @Parameter set on " + field + "; " +
								"should be one or the other, but not both");
			} else if (parameter != null) {
				processParameterAnnotation(parameter, type, (Object newValue) -> {
					field.setAccessible(true);
					try {
						field.set(receptacle, newValue);
					} catch (IllegalAccessException e) {
						throw new OptionProcessingException("Exception setting field " + field, e);
					}
				});
			} else if (flag != null) {
				if (type != Boolean.class && type != Boolean.TYPE) {
					throw new OptionProcessingException("Field " + field + " must be boolean to " +
							"be annotated with @Flag");
				}
				processFlagAnnotation(flag, (Boolean newValue) -> {
					field.setAccessible(true);
					try {
						field.set(receptacle, newValue);
					} catch (IllegalAccessException e) {
						throw new OptionProcessingException("Exception setting field " + field, e);
					}
				});
			}
		}
		for (Method method : definitionClass.getDeclaredMethods()) {
			Parameter parameter = method.getAnnotation(Parameter.class);
			Flag flag = method.getAnnotation(Flag.class);
			if (parameter == null && flag == null) {
				continue;
			}
			if (parameter != null && flag != null) {
				throw new OptionProcessingException(
						"Both @Flag and @Parameter set on " + method + "; " +
								"should be one or the other, but not both");
			}
			if (method.getReturnType() != Void.TYPE || method.getParameters().length > 1) {
				throw new OptionProcessingException("Method " + method + ": methods annotated " +
						"with @Flag or @Parameter must be 'setters'; that is, they must take " +
						"one parameter and return 'void'");
			}
			Class type = method.getParameters()[0].getType();
			if (parameter != null) {
				processParameterAnnotation(parameter, type, (Object newValue) -> {
					method.setAccessible(true);
					try {
						method.invoke(receptacle, newValue);
					} catch (IllegalAccessException | InvocationTargetException e) {
						throw new OptionProcessingException("Exception invoking " + method, e);
					}
				});
			} else {
				if (type != Boolean.class && type != Boolean.TYPE) {
					throw new OptionProcessingException(
							"Method " + method + " must take boolean to " +
									"be annotated with @Flag");
				}
				processFlagAnnotation(flag, (Boolean newValue) -> {
					method.setAccessible(true);
					try {
						method.invoke(receptacle, newValue);
					} catch (IllegalAccessException | InvocationTargetException e) {
						throw new OptionProcessingException("Exception invoking " + method, e);
					}
				});
			}
		}
	}

	private <M> void processParameterAnnotation(Parameter parameter, Class<M> type,
	                                            Consumer<M> setter) {
		TypeConverter<M> converter;
		Class<? extends TypeConverter<M>> converterClass =
				(Class<? extends TypeConverter<M>>) parameter.converter();
		if (parameter.converter() != UseTheDefaultConverter.class) {
			try {
				converter = converterClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new OptionProcessingException(
						"Cannot instantiate type converter " + converterClass, e);
			}
		} else {
			converter = ConverterUtil.getDefaultConverter(type);
			if (converter == null) {
				throw new OptionProcessingException("Could not find a type converter class for " +
						"type " + type.getName());
			}
		}

		ParameterDef<M> def = ReceptacleParameterDef.makeParameter(this, type, parameter.mnemonic(),
				parameter.documentation(), parameter.required(), converter, setter);
		for (String opt : parameter.shortOpt()) {
			Character shortOptChar = null;
			if (opt != null && !opt.isEmpty()) {
				if (opt.length() > 1) {
					throw new OptionProcessingException("Short option string " + opt +
							" is more than one character");
				}
				shortOptChar = opt.charAt(0);
			}
			if (shortOptChar != null) {
				def.addShortOpt(shortOptChar);
			}
		}
		for (String opt : parameter.longOpt()) {
			if (opt != null && !opt.isEmpty()) {
				def.addLongOpt(opt);
			}
		}
	}

	private void processFlagAnnotation(Flag flag, Consumer<Boolean> setter) {
		ParameterDef<Boolean> def =
				ReceptacleParameterDef.makeFlag(this, flag.documentation(), setter);
		for (String opt : flag.shortOpt()) {
			Character shortOptChar = null;
			if (opt != null && !opt.isEmpty()) {
				if (opt.length() > 1) {
					throw new OptionProcessingException("Short option string '" + opt +
							"' is more than one character");
				}
				shortOptChar = opt.charAt(0);
			}
			if (shortOptChar != null) {
				def.addShortOpt(shortOptChar);
			}
		}
		for (String opt : flag.longOpt()) {
			if (opt != null && !opt.isEmpty()) {
				def.addLongOpt(opt);
			}
		}
	}

	/**
	 * Add a flag-type option (true or false)
	 *
	 * @param documentation Documentation to display when generating the usage message. May not be
	 *                      null.
	 * @throws IllegalArgumentException if both ShortOpt and LongOpt are null
	 * @throws IllegalArgumentException if Documentation is null or zero length (Work with me here!
	 *                                  I'm building a freakin' usage message for you!)
	 * @throws IllegalArgumentException if either ShortOpt or LongOpt has already been added
	 */

	public ParameterDef<Boolean> addFlag(String documentation) {
		// Step 1 - determine whether we need to throw any exceptions,
		// so that 'this' will be left in a consistent state

		if (documentation == null || documentation.isEmpty()) {
			throw new IllegalArgumentException("must give documentation for parameters");
		}

		// Step 2 - add the options to this
		ParameterDef<Boolean> ph =
				ManualParameterDef.makeFlag(this, Boolean.class, null, documentation);
		options.add(ph);
		return ph;
	}

	/**
	 * Add a parameter-type option (with a value to be passed in). The wrapper method is usually
	 * handier, except when the short opt is <b>null </b>.
	 *
	 * @param paramMnemonic A short name for the parameter. May not be null.
	 * @param documentation Documentation to display when generating the usage message. May not be
	 *                      null.
	 * @param required      Whether the flag is required.
	 * @throws IllegalArgumentException if both ShortOpt and LongOpt are null
	 * @throws IllegalArgumentException if Documentation is null or zero length
	 * @throws IllegalArgumentException if ParamMnemonic is null or zero length
	 * @throws IllegalArgumentException if either ShortOpt or LongOpt has already been added
	 */
	public <T> ParameterDef<T> addParam(Class<T> type, String paramMnemonic, String documentation,
	                                    boolean required) {
		if (documentation == null || documentation.isEmpty()) {
			throw new IllegalArgumentException("must give documentation for parameters");
		}
		if (paramMnemonic == null || paramMnemonic.isEmpty()) {
			throw new IllegalArgumentException("must give parameter mnemonic for parameters");
		}

		ParameterDef<T> ph =
				ManualParameterDef.makeParameter(this, type, paramMnemonic, documentation,
						required);
		options.add(ph);
		return ph;
	}

	public ParameterDef<String> addParam(String paramMnemonic, String documentation,
	                                     boolean required) {
		return addParam(String.class, paramMnemonic, documentation, required);
	}

	/**
	 * Process the command line
	 *
	 * @param params The command line parameters
	 * @return The remaining parameters after processing is over
	 * @throws CommandLineOptionException if any of the command line processing semantics are
	 *                                    violated. The message will contain a usage message in
	 *                                    traditional unix style.
	 */
	public ArrayList<String> processParams(String... params) throws CommandLineOptionException {
		List<String> x = new ArrayList<>();
		Collections.addAll(x, params);
		return processParams(x);
	}

	/**
	 * Process the command line
	 *
	 * @param params The command line parameters
	 * @return The remaining parameters after processing is over
	 * @throws CommandLineOptionException if any of the command line processing semantics are
	 *                                    violated
	 */
	public ArrayList<String> processParams(List<String> params) throws CommandLineOptionException {
		int paramNum;
		Collection<String> problems = new ArrayList<>();

		for (paramNum = 0; paramNum < params.size(); paramNum++) {
			String param = params.get(paramNum);
			if ("--".equals(param)) {
				// stop processing; skip this parameter, and return
				// the rest
				paramNum++;
				break;
			} else if (param.length() < 2) {
				// this is a non-option parameter; stop processing
				break;
			} else if (param.charAt(0) == '-') {
				// this is an option parameter
				if (param.charAt(1) == '-') {
					// long option processing
					String longOptIn = param.substring(2);
					ParameterDef match = getLongOpt(longOptIn, problems);
					if (match != null) {
						if (match.isTakesParam()) {
							if (paramNum + 1 == params.size()) {
								problems.add("Option --" + longOptIn +
										" requires a parameter, but the command line doesn't have any more");
							} else {
								paramNum++;
								match.setParameterValue(params.get(paramNum));
							}
						} else {
							match.setFlagPresent();
						}
					}
				} else {
					// single dash
					for (int j = 1; j < param.length(); j++) {
						char pChar = param.charAt(j);
						ParameterDef<?> shortOpt = getShortOptProcessing(pChar);

						if (shortOpt != null) {
							if (shortOpt.isTakesParam()) {
								if (paramNum + 1 == params.size()) {
									problems.add("Option -" + pChar +
											" requires a parameter, but the command line doesn't have any more");
								} else {
									paramNum++;
									shortOpt.setParameterValue(params.get(paramNum));
									break;
								}
							} else {
								shortOpt.setFlagPresent();
								break;
							}
						} else {
							problems.add("Unknown option: -" + pChar);
						}
					}
				}
			} else {
				// this is a non-option parameter; stop processing
				break;
			}
		}

		// Look for required options that were not passed
		for (ParameterDef x : options) {
			if (x.isTakesParam() && x.isRequired() && !x.isSet()) {
				String p;
				if (!x.getLongOptList().isEmpty()) {
					p = "--" + x.getLongOptList().get(0);
				} else {
					p = "-" + x.getShortOptList().get(0);
				}
				problems.add("required option " + p + " was not given");
			}
		}

		// Were there problems?
		if (!problems.isEmpty()) {
			StringBuilder errStr = new StringBuilder();
			errStr.append("\n");
			for (String problem : problems) {
				errStr.append("error: ").append(problem).append("\n");
			}
			usage(errStr);
			throw new CommandLineOptionException(errStr.toString());
		}

		// build results
		ArrayList<String> out = new ArrayList<>();
		for (int q = paramNum; q < params.size(); q++) {
			out.add(params.get(q));
		}
		return out;
	}

	/**
	 * Generate usage string.
	 *
	 * @param errStr the output string builder
	 */
	private void usage(StringBuilder errStr) {
		errStr.append("usage:\n");
		for (ParameterDef def : options) {
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

	public <T> void addShortOpt(ParameterDef parameterDef, Character opt) {
		if (hasShortOpt(opt)) {
			throw new OptionProcessingException(
					"Short option -" + opt + " specified more than once");
		}
		byShort_.put(opt, parameterDef);
	}

	public <T> void addLongOpt(ParameterDef parameterDef, String opt) {
		if (byLong_.containsKey(opt)) {
			throw new OptionProcessingException("Long option " + opt + " has already been defined");
		}
		byLong_.put(opt, parameterDef);
	}

	public boolean isFlagSet(char shortOpt) {
		ParameterDef<?> parameterDef = byShort_.get(shortOpt);
		if (parameterDef == null) {
			throw new OptionProcessingException("Short option '" + shortOpt + "' not defined");
		}
		return parameterDef.isSet();
	}
}
