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

package com.bluecirclesoft.open.getopt.flavors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.bluecirclesoft.open.getopt.ArgumentSpecification;
import com.bluecirclesoft.open.getopt.CommandLineProcessingException;
import com.bluecirclesoft.open.getopt.GetOpt;
import com.bluecirclesoft.open.getopt.OptionSpecification;

/**
 * Option processing flavor for GNU getopt style processing.
 */
public class GnuGetoptFlavor implements CommandLineProcessingFlavor {

	private final GetOpt creator;

	private final boolean processOptionsAfterNonOptions;

	/**
	 * Construct a GNU getopt-flavor command line processor.
	 *
	 * @param creator                       GetOpt that created me
	 * @param processOptionsAfterNonOptions if false, stop processing options when the first
	 *                                      non-option argument is encountered (a.k.a.
	 *                                      POSIXLY_CORRECT).  If true, only stop processing options
	 *                                      when "--" is encountered.
	 */
	public GnuGetoptFlavor(GetOpt creator, boolean processOptionsAfterNonOptions) {
		this.creator = creator;
		this.processOptionsAfterNonOptions = processOptionsAfterNonOptions;
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
	@Override
	public List<String> processParams(String... params) {
		List<String> x = new ArrayList<>();
		Collections.addAll(x, params);
		return processParams(x);
	}

	/**
	 * Process the command line
	 *
	 * @param params The command line parameters
	 * @return The remaining parameters after processing is over
	 * @throws CommandLineProcessingException if any of the command line processing semantics are
	 *                                        violated
	 */
	@Override
	public List<String> processParams(List<String> params) {
		Collection<String> problems = new ArrayList<>();
		List<String> nonOptions = new ArrayList<>();

		for (int paramNum = 0; paramNum < params.size(); paramNum++) {
			String param = params.get(paramNum);
			if ("--".equals(param)) {
				// stop processing; skip this parameter, and return the rest
				paramNum++;
				extractRestOfLine(params, paramNum, nonOptions);
				break;
			} else if (param.length() < 2) {
				// this is a non-option arg
				if (processOptionsAfterNonOptions) {
					nonOptions.add(params.get(paramNum));
				} else {
					extractRestOfLine(params, paramNum, nonOptions);
					break;
				}
			} else if (param.charAt(0) == '-') {
				// this is an option parameter
				if (param.charAt(1) == '-') {
					// long option processing
					paramNum = processLongOption(params, paramNum, problems, param);
				} else {
					// short option processing
					paramNum = processShortOption(params, paramNum, problems, param);
				}
			} else {
				// non-option arg
				if (processOptionsAfterNonOptions) {
					nonOptions.add(params.get(paramNum));
				} else {
					extractRestOfLine(params, paramNum, nonOptions);
					break;
				}
			}
		}

		// Look for required options that were not passed
		for (OptionSpecification x : creator.getOptions()) {
			if (x.isRequired() && !x.isSpecified()) {
				problems.add("required option " + x.makeOptionDescriptor() + " was not given");
			}
		}

		// Were there problems?
		if (!problems.isEmpty()) {
			StringBuilder errStr = new StringBuilder();
			errStr.append("\n");
			for (String problem : problems) {
				errStr.append("error: ").append(problem).append("\n");
			}
			throw new CommandLineProcessingException(errStr.toString(), creator);
		}

		return nonOptions;
	}

	private static void extractRestOfLine(List<String> params, int paramNum, List<String> nonOptions) {
		for (int i = paramNum; i < params.size(); i++) {
			nonOptions.add(params.get(i));
		}
	}

	private int processShortOption(List<String> params, int paramNum, Collection<String> problems, String param) {
		for (int j = 1; j < param.length(); j++) {
			char pChar = param.charAt(j);
			OptionSpecification shortOpt = creator.getShortOptProcessing(pChar);

			if (shortOpt != null) {
				if (shortOpt.getArgumentSpecification() == ArgumentSpecification.REQUIRED ||
						shortOpt.getArgumentSpecification() == ArgumentSpecification.OPTIONAL) {
					if (j < param.length() - 1) {
						shortOpt.encounter(param.substring(j + 1));
						paramNum++;
						break;
					} else {
						if (paramNum + 1 == params.size()) {
							problems.add("Option -" + pChar + " requires a parameter, but the command line doesn't have any more");
						} else {
							paramNum++;
							shortOpt.encounter(params.get(paramNum));
							break;
						}
					}
				} else {
					shortOpt.encounter(true);
				}
			} else {
				problems.add("Unknown option: -" + pChar);
			}
		}
		return paramNum;
	}

	private int processLongOption(List<String> params, int paramNum, Collection<String> problems, String param) {
		String longOptIn = param.substring(2);
		int equalPos = longOptIn.indexOf('=');
		String paramValue = null;
		if (equalPos > 0) {
			paramValue = longOptIn.substring(equalPos + 1);
			longOptIn = longOptIn.substring(0, equalPos);
		}
		OptionSpecification match = creator.getLongOpt(longOptIn, problems);
		if (match != null) {
			if (match.getArgumentSpecification() == ArgumentSpecification.REQUIRED) {
				if (paramValue == null && paramNum + 1 == params.size()) {
					problems.add("Option --" + longOptIn + " requires a parameter, but the command line doesn't have any more");
				} else {
					if (paramValue == null) {
						paramNum++;
						paramValue = params.get(paramNum);
					}
					match.encounter(paramValue);
				}
			} else {
				match.encounter(true);
			}
		}
		return paramNum;
	}


}
