package com.bluecirclesoft.open.getopt;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO document me
 */
public class ParameterDescription {

	public static final int MAX_WIDTH = 60;

	private final List<String> optionDescriptions = new ArrayList<>();

	private String docString;

	private List<String> docLines;

	public void addOptionDescription(String option, String mnemonic) {
		if (mnemonic == null) {
			optionDescriptions.add(option);
		} else {
			optionDescriptions.add(option + " " + mnemonic);
		}
	}

	public void setDocumentation(String documentation) {
		docString = documentation;
		this.docLines = null;
	}

	/**
	 * Line-break a string into lines. Assumes the input string has been trimmed.
	 *
	 * @param docHolder the holder for the documentation string - will be replaced with the
	 *                  remainder of the line which has not been broken
	 * @return the next broken line, or {@code null} if there's nothing else to break;
	 */
	private static String findLine(String[] docHolder) {
		final String str = docHolder[0];
		if (str.isEmpty()) {
			return null;
		}
		int i = MAX_WIDTH;
		if (str.length() < i) {
			docHolder[0] = "";
			return str;
		}
		final String result;
		while (i >= 0 && !Character.isSpaceChar(str.charAt(i))) {
			i--;
		}
		if (i == 0) {
			// no spaces - we started in the middle of a word - search forward
			i = MAX_WIDTH + 1;
			while (i < str.length() && !Character.isSpaceChar(str.charAt(i))) {
				i++;
			}
			// found the end of the word, get it
			result = str.substring(0, i);
			// find the next word
			while (i < str.length() && Character.isSpaceChar(str.charAt(i))) {
				i++;
			}
			docHolder[0] = str.substring(i, str.length());
		} else {
			// search backward - i will be position of next word, j will be position of last word
			int j = i;
			while (j >= 0 && Character.isSpaceChar(str.charAt(j))) {
				j--;
			}
			result = str.substring(0, j);
			docHolder[0] = str.substring(i, str.length());
		}
		return result;
	}

	public Iterable<String> getOptionDescriptions() {
		return optionDescriptions;
	}

	public Iterable<String> getBrokenDocumentation() {
		if (docLines == null) {
			docLines = new ArrayList<>();
			if (docString != null) {
				String[] docHolder = {docString.trim()};
				while (true) {
					String line = findLine(docHolder);
					if (line == null) {
						break;
					}
					this.docLines.add(line);
				}
			}
		}
		return docLines;
	}

}
