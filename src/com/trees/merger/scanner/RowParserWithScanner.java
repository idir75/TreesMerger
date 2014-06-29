package com.trees.merger.scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * <p>
 * A parser class used to parse a row of the text file representing a tree.
 * The parser uses <code>java.util.Scanner</code> to scan the row.
 * The parser sets the node path string and the node value and create a scanner with a delimiter.
 * The scanner is used to scan the row's node path string by using <code>nodePathAsMapKeys</code> method.
 * The <code>nodePathAsMapKeys</code> method creates a list of all possible node paths.
 * Example : by using the scanner to scan the path "A/B/C", the result is the list of paths : "A", "A/B", "A/B/C"
 * <p>
 * @author
 */
public class RowParserWithScanner {

	/**
	 * The row to parse.
	 */
	private String fRow;
	/**
	 * A scanner.
	 */
	private Scanner fScanner;
	/**
	 * The node path part of the row : the path to a tree node.
	 */
	private String fNodePath;
	/**
	 * The value part : the value associated with a tree node.
	 */
	private Integer fNodeValue;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param pRow the row to scan.
	 */
	public RowParserWithScanner(final String pRow) {
		fRow = pRow;
		if (fRow.indexOf(Constants.COLON_DELIMITER) > -1) {
			String[] rowParts = fRow.split(Constants.COLON_DELIMITER);
			fNodePath = rowParts[0];
			fNodeValue = new Integer(rowParts[1]);
			fScanner = new Scanner(fNodePath);
			setDelimiter();
		}
	}

	/**
	 * @return the node path.
	 */
	public String getNodePath() {
		return fNodePath;
	}

	/**
	 * @return the node value.
	 */
	public Integer getNodeValue() {
		return fNodeValue;
	}

	/**
	 * @return the scanner.
	 */
	public Scanner getScanner() {
		return fScanner;
	}

	/**
	 * <p>
	 * Construct a list of all possible paths of a row.
	 * </p>
	 * @return a list of the keys of the row.
	 */
	public List<String> nodePathAsMapKeys() {
		List<String> rowKeys = new ArrayList<String>();
		if (fScanner == null) {
			return rowKeys;
		}
		String previousKey = fScanner.next();
		rowKeys.add(previousKey);
		while (fScanner.hasNext()) {
			String key = fScanner.next();
			previousKey = previousKey + Constants.SLASH_DELIMITER + key;
			rowKeys.add(previousKey);
		}
		return rowKeys;
	}

	/**
	 * <p>
	 * Replace all the occurrences of a {@link Constants#DOT_PATTERN} in a row by {@link Constants#SLASH_DELIMITER}.
	 * </p>
	 * @return the row with replaced delimiters.
	 */
	public String replaceRowDelimiters() {
		if (fRow.indexOf(Constants.DOT_PATTERN) > -1) {
			return fRow.replaceAll(Constants.DOT_DELIMITER, Constants.SLASH_DELIMITER);
		}
		return fRow;
	}

	/**
	 * <p>
	 * Set the delimiter according to the row to scan.
	 * </p>
	 */
	public void setDelimiter() {
		if (fNodePath.indexOf(Constants.DOT_PATTERN) > -1) {
			fScanner.useDelimiter(Constants.DOT_DELIMITER);
		} else if (fNodePath.indexOf(Constants.SLASH_DELIMITER) > -1) {
			fScanner.useDelimiter(Constants.SLASH_DELIMITER);
		}
	}
}
