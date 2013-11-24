package com.trees.merger;

import java.io.File;
import java.io.FileNotFoundException;

import com.trees.merger.scanner.FileParserWithScanner;

public class TreesMergerMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String firstFilePath = Constants.SAMPLES_DIR + File.separator + "File1.txt";
		String secondFilePath = Constants.SAMPLES_DIR + File.separator + "File2.txt";
		String resultFilePath = Constants.SAMPLES_DIR + File.separator + "Result.txt";
		FileParserWithScanner firstScanner = new FileParserWithScanner(firstFilePath);
		try {
			firstScanner.initParser();
		} catch (FileNotFoundException e) {
		}
		FileParserWithScanner secondScanner = new FileParserWithScanner(secondFilePath);
		try {
			secondScanner.initParser();
		} catch (FileNotFoundException e) {
		}
		TreesMerger treesMerger = new TreesMerger();
		treesMerger.mergeTwoTrees(firstScanner, secondScanner, resultFilePath);

		/*FileParserWithBuffer firstBuffer = new FileParserWithBuffer(firstFilePath);
		FileParserWithScanner secondBuffer = new FileParserWithScanner(secondFilePath);
		treesMerger.mergeTwoTrees(firstBuffer, secondBuffer, resultFilePath);*/
	}

}
