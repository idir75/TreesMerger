package com.trees.merger.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.trees.merger.Constants;
import com.trees.merger.FileParser;
import com.trees.merger.RowParser;

public class FileParserWithScanner extends FileParser {

	private String fFilePath;
	private Scanner fScanner;

	public FileParserWithScanner(final String pFilePath) {
		fFilePath = pFilePath;
	}

	/*public String nextLine() {
		return fScanner.hasNextLine() ? fScanner.nextLine() : null;
	}

	public boolean hasNextLine() {
		return fScanner.hasNextLine();
	}
*/
	public void close(){
		fScanner.close();
	}

	public void initParser() throws FileNotFoundException {
		fScanner = new Scanner(new File(fFilePath));
	}

	/*public void parseWithScanner() {
	}

	public void replaceDelimiters() {
	}*/

	@Override
	public RowParser getRowParser(final String pRow) {
		return new RowParserWithScanner(pRow);
	}

	@Override
	public String copyFileData() {
		StringBuilder fileContent = new StringBuilder();
		while (fScanner.hasNextLine()) {
			RowParser rowParser = getRowParser(fScanner.nextLine());
			//No handle for lines with empty path part.
			String nodePath = rowParser.getNodePath();
			if (nodePath == null || nodePath.length() == 0) {
				continue;
			}
			fileContent.append(rowParser.replaceDelimiters());
			fileContent.append(Constants.NEW_LINE_STR);
		}
		return fileContent.substring(0, fileContent.length() - 1);
	}

	@Override
	public void getMapFromFile(Map<String, Integer> pMap) {
		if (pMap == null) {
			pMap = new TreeMap<String, Integer>();
		}
		while (fScanner.hasNextLine()) {
			//No handle for lines with empty path part.
			RowParser rowParser = getRowParser(fScanner.nextLine());
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
	}

	@Override
	public boolean isEmpty() {
		return fScanner == null || !fScanner.hasNextLine();
	}

	@Override
	public String nextLine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasNextLine() {
		return false;
	}
}
