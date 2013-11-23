package com.trees.merger.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import org.apache.log4j.Logger;

import com.trees.merger.Constants;

/**
 * <p>
 * A class that merges two trees into one tree.
 * The trees are given in a flat text files.
 * A text file contains a list of rows corresponding to a line in the file.
 * A row represents a path to a node of the tree, and a value associated to the node. The node path and the node value are separated by " : ".
 * Example of a row : "A/B/C : 3", where "A/B/C" is the path to the node "C", and 3 is the value associated to the node "C".
 * </p>
 * @author Idir.
 */
public class TreesMergerWithScanner {

	/**
	 * A logger.
	 */
	private static final Logger fLogger = Logger.getLogger(TreesMergerWithScanner.class);

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
		Scanner firstTreeFileScanner = getFileScanner(pFirstTreeFilePath);
		Scanner secondTreeFileScanner = getFileScanner(pSecondTreeFilePath);
		if (firstTreeFileScanner == null && secondTreeFileScanner == null) {
			return;
		}

		//No need to merge. The result file contains the content of the file with path pSecondTreeFilePath.
		if (firstTreeFileScanner == null || !firstTreeFileScanner.hasNextLine()) {
			String secondTreeFileContent = copyFileDataWithScanner(secondTreeFileScanner, pMergedTreesFilePath);
			serializeDataIntoFile(pMergedTreesFilePath, secondTreeFileContent);
			if (secondTreeFileScanner != null) {
				secondTreeFileScanner.close();
			}
			return;
		}

		//No need to merge. The result file contains the content of the file with path pFirstTreeFilePath.
		if (secondTreeFileScanner == null || !secondTreeFileScanner.hasNextLine()) {
			String firstTreeFileContent = copyFileDataWithScanner(firstTreeFileScanner, pMergedTreesFilePath);
			serializeDataIntoFile(pMergedTreesFilePath, firstTreeFileContent);
			if (firstTreeFileScanner != null) {
				firstTreeFileScanner.close();
			}
			return;
		}

		getMapFromFileByScanner(firstTreeFileScanner, mergedTreesMap);
		getMapFromFileByScanner(secondTreeFileScanner, mergedTreesMap);
		String mergedTreesMapFormmatted = formatMap(mergedTreesMap);
		serializeDataIntoFile(pMergedTreesFilePath, mergedTreesMapFormmatted);
		if (firstTreeFileScanner != null) {
			firstTreeFileScanner.close();
		}
		if (secondTreeFileScanner != null) {
			secondTreeFileScanner.close();
		}
	}

	/**
	 * <p>
	 * Create a <code>java.util.Scanner</code> from a file given by its path.
	 * </p>
	 * @param pFilePath the path of the file used to construct the <code>java.util.Scanner</code>
	 * @return a <code>java.util.Scanner</code>
	 */
	public Scanner getFileScanner(final String pFilePath) {
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
	public void getMapFromFileByScanner(final Scanner pFileScanner, Map<String, Integer> pMap) {
		if (pMap == null) {
			pMap = new TreeMap<String, Integer>();
		}
		while (pFileScanner.hasNextLine()) {
			RowParserWithScanner rowScanner = new RowParserWithScanner(pFileScanner.nextLine());
			//No handle for lines with empty path part.
			String nodePath = rowScanner.getNodePath();
			if (nodePath == null || nodePath.length() == 0) {
				continue;
			}
			Integer nodeValue = rowScanner.getNodeValue();
			List<String> nodePathList = rowScanner.nodePathAsMapKeys();
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
	public String formatMap(final Map<String, Integer> pMap) {
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
	 * @param pData the <code>java.lang.String</code> to write into a file.
	 */
	public void serializeDataIntoFile(final String pResultFilePath, final String pData) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(pResultFilePath);
			fileWriter.write(pData);
			fileWriter.close();
		} catch (IOException e) {
			fLogger.warn("Error while writing into file : " + pResultFilePath, e);
		}
	}

	/**
	 * Create a copy of a file using a <code>java.util.Scanner</code>
	 * @param pSourcePath the path to the file to copy.
	 * @param pCopyFilePath the path to the created copy.
	 * @throws IOException when an error occurs.
	 */
	public String copyFileDataWithScanner(final Scanner pFileScanner, final String pCopyFilePath) {
		StringBuilder fileContent = new StringBuilder();
		while (pFileScanner.hasNextLine()) {
			String row = pFileScanner.nextLine();
			RowParserWithScanner rowScanner = new RowParserWithScanner(row);
			//No handle for lines with empty path part.
			String nodePath = rowScanner.getNodePath();
			if (nodePath == null || nodePath.length() == 0) {
				continue;
			}
			/*if (row.indexOf(Constants.DOT_STR) > -1) {
				fileContent.append(row.replaceAll(Constants.DOT_DELIMITER, Constants.SLASH_DELIMITER));
			} else {
				fileContent.append(row);
			}*/
			fileContent.append(rowScanner.replaceDelimiters());
			fileContent.append(Constants.NEW_LINE_STR);
		}
		return fileContent.substring(0, fileContent.length() - 1);
	}
}
