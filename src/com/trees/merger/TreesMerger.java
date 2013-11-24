package com.trees.merger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class TreesMerger {

	/**
	 * A logger.
	 */
	private static final Logger fLogger = Logger.getLogger(TreesMerger.class);

	public void mergeTwoTrees(final FileParser pFirstFileParser, final FileParser pSecondFileParser, final String pResultFilePath) {
		Map<String, Integer> mergedTreesMap = new TreeMap<String, Integer>();
		if (pFirstFileParser == null && pSecondFileParser == null) {
			return;
		}

		//No need to merge. The result file contains the content of the file with path pSecondTreeFilePath.
		if (pFirstFileParser == null || pFirstFileParser.isEmpty()) {
			/*String secondTreeFileContent = pSecondFileParser.copyFileData();
			serializeDataIntoFile(pResultFilePath, secondTreeFileContent);
			if (pSecondFileParser != null) {
				pSecondFileParser.close();
			}
			return;*/
			serializeDataWithoutMerging(pSecondFileParser, pResultFilePath);
			return;
		}
		//No need to merge. The result file contains the content of the file with path pFirstTreeFilePath.
		if (pSecondFileParser == null || pSecondFileParser.isEmpty()) {
			/*String firstTreeFileContent = pFirstFileParser.copyFileData();
			serializeDataIntoFile(pResultFilePath, firstTreeFileContent);
			if (pFirstFileParser != null) {
				pFirstFileParser.close();
			}
			return;*/
			serializeDataWithoutMerging(pFirstFileParser, pResultFilePath);
			return;
		}
		pFirstFileParser.getMapFromFile(mergedTreesMap);
		pSecondFileParser.getMapFromFile(mergedTreesMap);
		String mergedTreesMapFormatted = formatMapToString(mergedTreesMap);
		serializeDataIntoFile(pResultFilePath, mergedTreesMapFormatted);
		if (pFirstFileParser != null) {
			pFirstFileParser.close();
		}
		if (pSecondFileParser != null) {
			pSecondFileParser.close();
		}
	}

	private void serializeDataWithoutMerging(final FileParser pFileParser, final String pResultFilePath) {
		//No need to merge. The result file contains the content of the file with path pSecondTreeFilePath.
		serializeDataIntoFile(pResultFilePath, pFileParser.copyFileData());
		if (pFileParser != null) {
			pFileParser.close();
		}
		return;
	}

	/*public String copyFileData(final FileParser pFileParser) {
		StringBuilder fileContent = new StringBuilder();
		while (pFileParser.hasNextLine()) {
			RowParser rowParser = pFileParser.getRowParser(pFileParser.nextLine());
			//No handle for lines with empty path part.
			String nodePath = rowParser.getNodePath();
			if (nodePath == null || nodePath.length() == 0) {
				continue;
			}
			fileContent.append(rowParser.replaceDelimiters());
			fileContent.append(Constants.NEW_LINE_STR);
		}
		return fileContent.substring(0, fileContent.length() - 1);
	}*/

	/*public void getMapFromFile(final FileParser pFileParser, Map<String, Integer> pMap) {
		if (pMap == null) {
			pMap = new TreeMap<String, Integer>();
		}
		while (pFileParser.hasNextLine()) {
			//No handle for lines with empty path part.
			RowParser rowParser = pFileParser.getRowParser(pFileParser.nextLine());
			String nodePath = rowParser.getNodePath();
			if (nodePath == null || nodePath.length() == 0) {
				continue;
			}
			Integer nodeValue = rowParser.getNodeValue();
			List<String> nodePathList = rowParser.nodePathAsMapKeys();
			for (String nodePathAsKey : nodePathList) {
				Integer oldNodeValue = pMap.get(nodePathAsKey);
				if (oldNodeValue != null && oldNodeValue.intValue() > 0) {
					pMap.put(nodePathAsKey, oldNodeValue + nodeValue);
				} else {
					pMap.put(nodePathAsKey, nodeValue);
				}
			}
		}
	}*/

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
	 * <p>
	 * Create and return a <code>java.lang.String</code> representation of a <code>java.util.Map</code>
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
			result.append(Constants.NEW_LINE_STR);
		}
		if (result.length() == 0) {
			return result.toString();
		}
		return result.substring(0, result.length() - 1);
	}
}
