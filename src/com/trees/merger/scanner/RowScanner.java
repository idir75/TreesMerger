package com.trees.merger.scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * <p>
 * A scanner class used to scan a text file tree row.
 * The scanner can be used to scan an entire row or to scan the row's node path string.
 * When the scanner is used to scan a row, it sets the node path string and the node value.
 * When the scanner is used to scan a node path (by using <code>nodePathAsMapKeys</code> method), it creates a list of all possible node paths.
 * Example : by using the scanner to scan the path "A/B/C", the result is the list of paths : "A", "A/B", "A/B/C"
 * <p>
 * @author Idir DAHMOUH.
 */
public class RowScanner {

	/**
	 * A <code>java.util.Scanner</code>
	 */
	private Scanner fScanner;
	/**
	 * A <code>java.lang.String</code> that represents the node path.
	 */
	private String fNodePath;
	/**
	 * An <code>java.lang.Integer</code> that represents the value associated to the node.
	 */
	private Integer fNodeValue;
	/**
	 * A <code>java.lang.String</code> used as a delimiter when scanning a row.
	 */
	private String fDelimiter;
	/**
	 * The string that is scanned by the scanner.
	 */
	private String fStringToScan;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param pRow the row to scan.
	 */
	public RowScanner(final String pRow) {
		fStringToScan = pRow;
		setDelimiter();
		fScanner = new Scanner(pRow);
		if (fDelimiter != null) {
			fScanner.useDelimiter(fDelimiter);
		}

		//Only when the row to scan is a line (a node path followed by " : " and followed by a node value) in the flat text file.
		if (Constants.COLON_DELIMITER.equals(fDelimiter)) {
			if (fScanner.hasNext()) {
				fNodePath = fScanner.next();
			}
			if (fScanner.hasNextInt()) {
				fNodeValue = fScanner.nextInt();
			}
		}
	}

	/**
	 * <p>
	 * Return the <code>java.lang.String</code> representing the node path.
	 * </p>
	 * @return a <code>java.lang.String</code> representing the node path.
	 */
	public String getNodePath() {
		return fNodePath;
	}

	/**
	 * <p>
	 * Return a <code>java.lang.Integer</code> representing the node associated value.
	 * </p>
	 * @return a <code>java.lang.Integer</code> representing the node associated value.
	 */
	public Integer getNodeValue() {
		return fNodeValue;
	}

	/**
	 * <p>
	 * Construct a list of all possible paths of a row.
	 * </p>
	 * @return a list of the keys of the row.
	 */
	public List<String> nodePathAsMapKeys() {
		List<String> rowKeys = new ArrayList<String>();
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
	 * Set the delimiter according to the row to scan.
	 * </p>
	 */
	public void setDelimiter() {
		//Start with the COLON_DELIMITER
		if (fStringToScan.indexOf(Constants.COLON_DELIMITER) > -1) {
			fDelimiter = Constants.COLON_DELIMITER;
		} else if (fStringToScan.indexOf(Constants.DOT_STR) > -1) {
			fDelimiter = Constants.DOT_DELIMITER;
		} else if (fStringToScan.indexOf(Constants.SLASH_DELIMITER) > -1) {
			fDelimiter = Constants.SLASH_DELIMITER;
		}
	}

	/**
	 * @return the delimiter of the used scanner.
	 */
	public String getDelimiter() {
		return fDelimiter;
	}
}
