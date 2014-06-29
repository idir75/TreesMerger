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

/**
 * <p>
 * A class that merges two trees into one tree.
 * The trees are given in a flat text files.
 * A text file contains a list of rows corresponding to a line in the file.
 * A row represents a path to a node of the tree, and a value associated to the node. The node path and the node value are separated by " : ".
 * Example of a row : "A/B/C : 3", where "A/B/C" is the path to the node "C", and 3 is the value associated to the node "C".
 * </p>
 * @author
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
		Scanner firstTreeFileScanner = getFileScanner(pFirstTreeFilePath);
		Scanner secondTreeFileScanner = getFileScanner(pSecondTreeFilePath);

		//If the two tree files doesn't exist, do nothing.
		if (firstTreeFileScanner == null && secondTreeFileScanner == null) {
			return;
		}

		//When the first tree file doesn't exist, or it's empty : no need to merge. The result file contains the content of the pSecondTreeFilePath file with "/" delimiters.
		if (firstTreeFileScanner == null || !firstTreeFileScanner.hasNextLine()) {
			copyFileWithoutMerging(secondTreeFileScanner, pMergedTreesFilePath);
			return;
		}

		//When the second tree file doesn't exist, or it's empty : no need to merge. The result file contains the content of the pFirstTreeFilePath file with "/" delimiters.
		if (secondTreeFileScanner == null || !secondTreeFileScanner.hasNextLine()) {
			copyFileWithoutMerging(firstTreeFileScanner, pMergedTreesFilePath);
			return;
		}

		//Merge the file trees into a unique map.
		Map<String, Integer> mergedTreesMap = new TreeMap<String, Integer>();
		getMapFromFileByScanner(firstTreeFileScanner, mergedTreesMap);
		getMapFromFileByScanner(secondTreeFileScanner, mergedTreesMap);

		//Format the map of the merged trees as a string.
		String mergedTreesMapFormatted = formatMapToString(mergedTreesMap);

		//Serialize the string in a file.
		serializeDataIntoFile(pMergedTreesFilePath, mergedTreesMapFormatted);

		//Close the opened scanners.
		if (firstTreeFileScanner != null) {
			firstTreeFileScanner.close();
		}
		if (secondTreeFileScanner != null) {
			secondTreeFileScanner.close();
		}
	}

	/**
	 * Copy the data of a file given by a <code>java.util.Scanner</code> into another file given by its path.
	 * @param pFileScanner the <code>java.util.Scanner</code> of the data to serialize file.
	 * @param pResultFilePath the file used to serialize the data.
	 */
	public void copyFileWithoutMerging(final Scanner pFileScanner, final String pResultFilePath) {
		String fileData = getFileDataAsString(pFileScanner);
		serializeDataIntoFile(pResultFilePath, fileData);
		if (pFileScanner != null) {
			pFileScanner.close();
		}
		return;
	}

	/**
	 * <p>
	 * Return the data of a file given by its scanner as a string.
	 * </p>
	 * @param pFileScanner the given file scanner.
	 * @return the content
	 */
	public String getFileDataAsString(final Scanner pFileScanner) {
		StringBuilder fileData = new StringBuilder();
		while (pFileScanner.hasNextLine()) {
			String row = pFileScanner.nextLine();
			RowParserWithScanner rowScanner = new RowParserWithScanner(row);

			//Case of a row that doesn't contain a " : " delimiter.
			if (rowScanner.getScanner() == null) {
				continue;
			}

			//Case of a row that contains a " : " delimiter, but the node path part is empty.
			String nodePath = rowScanner.getNodePath();
			if (nodePath == null || nodePath.length() == 0) {
				continue;
			}
			fileData.append(rowScanner.replaceRowDelimiters());
			fileData.append(Constants.NEW_LINE_PATTERN);
		}
		if (fileData.length() == 0) {
			return "";
		}
		return fileData.substring(0, fileData.length() - 1);
	}

	/**
	 * <p>
	 * Serialize given data into a file.
	 * </p>
	 * @param FilePath the path to the file used to serialize the data.
	 * @param pData the data to serialize into a file.
	 */
	public void serializeDataIntoFile(final String FilePath, final String pData) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(FilePath);
			fileWriter.write(pData);
			fileWriter.close();
		} catch (IOException e) {
			fLogger.warn("Error while writing into file : " + FilePath, e);
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
	 * Create a <code>java.util.Map</code> from a file by using a <code>java.util.Scanner</code>
	 * </p>
	 * @param pFileScanner the <code>java.util.Scanner</code> used to parse the file.
	 * @param pMap the <code>java.util.Map</code> created from the file.
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
	 * Create and return a <code>java.lang.String</code> representation of a <code>java.util.Map</code>.
	 * </p>
	 * @param pMap the <code>java.util.Map</code> used to create the returned <code>java.lang.String</code>
	 * @return a <code>java.lang.String</code>
	 */
	public String formatMapToString(final Map<String, Integer> pMap) {
		StringBuilder result = new StringBuilder();
		for (Entry<String, Integer> entry : pMap.entrySet()) {
			result.append(entry.getKey());
			result.append(Constants.COLON_DELIMITER);
			result.append(entry.getValue());
			result.append(Constants.NEW_LINE_PATTERN);
		}
		if (result.length() == 0) {
			return "";
		}
		return result.substring(0, result.length() - 1);
	}
}
