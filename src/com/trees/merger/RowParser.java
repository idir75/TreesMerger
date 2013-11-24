package com.trees.merger;

import java.util.List;

public interface RowParser {

	public String getNodePath();
	public List<String> nodePathAsMapKeys();
	public String replaceDelimiters();
	public Integer getNodeValue();
}
