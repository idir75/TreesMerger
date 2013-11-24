package com.trees.merger;

import java.io.FileNotFoundException;
import java.util.Map;


public abstract class FileParser {

	public abstract String nextLine();
	public abstract boolean hasNextLine();
	public abstract void close();
	public abstract RowParser getRowParser(String pRow);
	public abstract void initParser() throws FileNotFoundException;
	public abstract String copyFileData();
	public abstract void getMapFromFile(Map<String, Integer> pMap);
	public abstract boolean isEmpty();
}
