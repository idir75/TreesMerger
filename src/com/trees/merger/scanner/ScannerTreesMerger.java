package com.trees.merger.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * <p>
 * A class that merges two trees into one tree.
 * The trees are given in a flat text files.
 * A text file contains a list of rows corresponding to a line in the file.
 * A row represents a path to a node of the tree, and a value associated to the node. The node path and the node value are separated by " : ".
 * Example of a row : "A/B/C : 3", where "A/B/C" is the path to the node "C", and 3 is the value associated to the node "C".
 * </p>
 * @author Idir DAHMOUH.
 */
public class ScannerTreesMerger {

	/**
	 * A logger.
	 */
	public static final Logger fLogger = Logger.getLogger(ScannerTreesMerger.class);

	/**
	 * <p>
	 * Merge two trees into one tree.
	 * The main method that implements the algorithm of merging two trees.
	 * </p>
	 * @param pFirstTreeFilePath The path to the file representing the first tree to merge.
	 * @param pSecondTreeFilePath The path to the file representing the second tree to merge.
	 * @param pMergedTreesFilePath the path to the file representing the tree obtained after merging operation.
	 */
	public void mergeTwoTreesByScanner(final String pFirstTreeFilePath, final String pSecondTreeFilePath, final String pMergedTreesFilePath) {
		Map<String, Integer> mergedTreesMap = new TreeMap<String, Integer>();
		Scanner firstTreeFileScanner = getScannerFromFile(pFirstTreeFilePath);
		Scanner secondTreeFileScanner = getScannerFromFile(pSecondTreeFilePath);
		if (firstTreeFileScanner == null && secondTreeFileScanner == null) {
			return;
		}

		//No need to merge. The result file contains the content of the file with path pSecondTreeFilePath.
		if (firstTreeFileScanner == null || !firstTreeFileScanner.hasNextLine()) {
			copyFile(pSecondTreeFilePath, pMergedTreesFilePath);
			return;
		}

		//No need to merge. The result file contains the content of the file with path pFirstTreeFilePath.
		if (secondTreeFileScanner == null || !secondTreeFileScanner.hasNextLine()) {
			copyFile(pFirstTreeFilePath, pMergedTreesFilePath);
			return;
		}
		parseFileIntoMapByScanner(firstTreeFileScanner, mergedTreesMap);
		parseFileIntoMapByScanner(secondTreeFileScanner, mergedTreesMap);
		String mergedTreesAsString = mapToString(mergedTreesMap);
		writeStringIntoFile(pMergedTreesFilePath, mergedTreesAsString);
		if (firstTreeFileScanner != null) {
			firstTreeFileScanner.close();
		}
		if (secondTreeFileScanner != null) {
			secondTreeFileScanner.close();
		}
	}

	/**
	 * <p>
	 * Construct a <code>java.util.Scanner</code> from a file given by its path.
	 * </p>
	 * @param pFilePath the path of the file used to construct the <code>java.util.Scanner</code>
	 * @return a <code>java.util.Scanner</code>
	 */
	public Scanner getScannerFromFile(final String pFilePath) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(pFilePath));
		} catch (FileNotFoundException e) {
			fLogger.warn("The file : " + pFilePath + " was not found", e);
		}
 		return scanner;
	}

	/**
	 * <p>
	 * Create a <code>java.util.Map</code> from a text file by using a <code>java.util.Scanner</code>
	 * </p>
	 * @param pFileScanner the <code>java.util.Scanner</code> used to parse the text file.
	 * @param pMap the <code>java.util.Map</code> created from the text file.
	 */
	public void parseFileIntoMapByScanner(final Scanner pFileScanner, Map<String, Integer> pMap) {
		if (pMap == null) {
			pMap = new TreeMap<String, Integer>();
		}
		while (pFileScanner.hasNextLine()) {
			RowScanner rowScanner = new RowScanner(pFileScanner.nextLine());
			String nodePath = rowScanner.getNodePath();
			if (nodePath == null || nodePath.length() == 0) {
				continue;
			}
			Integer nodeValue = rowScanner.getNodeValue();
			RowScanner nodePathScanner = new RowScanner(nodePath);
			List<String> nodePathList = nodePathScanner.nodePathAsMapKeys();
			for (String nodePathAsKey : nodePathList) {
				Integer oldNodeValue = pMap.get(nodePathAsKey);
				if (oldNodeValue != null && oldNodeValue.intValue() > 0) {
					pMap.put(nodePathAsKey, oldNodeValue + nodeValue);
				} else {
					pMap.put(nodePathAsKey, nodeValue);
				}
			}
		}
	}

	/**
	 * <p>
	 * Create and return a <code>java.lang.String</code> representation of a <code>java.util.Map</code>
	 * </p>
	 * @param pMap the <code>java.util.Map</code> used to create the returned <code>java.lang.String</code>
	 * @return a <code>java.lang.String</code>
	 */
	public String mapToString(final Map<String, Integer> pMap) {
		StringBuilder result = new StringBuilder();
		for (Entry<String, Integer> entry : pMap.entrySet()) {
			result.append(entry.getKey());
			result.append(Constants.COLON_DELIMITER);
			result.append(entry.getValue());
			result.append(Constants.NEW_LINE_STR);
		}
		if (result.length() == 0) {
			return result.toString();
		}
		return result.substring(0, result.length() - 1);
	}

	/**
	 * <p>
	 * Write a <code>java.lang.String</code> into a file given by its parameter.
	 * </p>
	 * @param pResultFilePath the path to the file used to serialize the <code>java.lang.String</code>
	 * @param pStr the <code>java.lang.String</code> to write into a file.
	 */
	public void writeStringIntoFile(final String pResultFilePath, final String pStr) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(pResultFilePath);
			fileWriter.write(pStr);
			fileWriter.close();
		} catch (IOException e) {
			fLogger.warn("Error while writing into file : " + pResultFilePath, e);
		}
	}

	/**
	 * Create a copy of a file.
	 * @param pSourcePath the path to the file to copy.
	 * @param pCopyPath the path to the created copy.
	 */
	public void copyFile(final String pSourcePath, final String pCopyPath) {
		FileReader fileReader = null;
		FileWriter fileWriter = null;
		try {
			fileReader = new FileReader(new File(pSourcePath));
		} catch (FileNotFoundException e) {
			fLogger.warn("Error while opening the file : " + pSourcePath, e);
			return;
		}
		try {
			fileWriter = new FileWriter(new File(pCopyPath));
		} catch (IOException e) {
			fLogger.warn("Error while opening the file : " + pCopyPath, e);
			return;
		}
		int charsRead;
		char[] charsBuffer = new char[Constants.BUFFER_SIZE];
		try {
			while ((charsRead = fileReader.read(charsBuffer, 0, Constants.BUFFER_SIZE)) > 0) {
				fileWriter.write(charsBuffer, 0, charsRead);
			}
			fileReader.close();
			fileWriter.close();
		} catch (IOException e) {
			fLogger.warn("Error while copying the file : " + pSourcePath + " into the file : " + pCopyPath, e);
		}
		/*InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(new File(pSourcePath));
		} catch (FileNotFoundException e) {
			fLogger.warn("Error while trying to open the file : " + pSourcePath, e);
			return;
		}
		try {
			outputStream = new FileOutputStream(new File(pCopyPath));
		} catch (FileNotFoundException e) {
			fLogger.warn("Error while trying to open the file : " + pCopyPath, e);
			return;
		}
		byte[] buffer = new byte[Constants.BUFFER_SIZE];
		int bytesRead;
		try {
			while ((bytesRead = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			outputStream.close();
		} catch (IOException e) {
			fLogger.warn("Error while trying to create a copy of the file : " + pSourcePath + ", into the file : " + pCopyPath, e);
		}*/
	}

}

