package com.trees.merger.scanner;

import java.io.File;


/**
 * Main class test.
 * @author Idir DAHMOUH.
 */
public class TreesMergerWithScannerMain {

	public static void main(String[] args) {
		TreesMergerWithScanner treeMerger = new TreesMergerWithScanner();
		treeMerger.mergeTwoTreesByScanner(Constants.SAMPLES_DIR + File.separator + "File1.txt", Constants.SAMPLES_DIR + File.separator + "File2.txt", Constants.SAMPLES_DIR + File.separator + "Result.txt");

		//File11 doesn't exist
		treeMerger.mergeTwoTreesByScanner(Constants.SAMPLES_DIR + File.separator + "File11.txt", Constants.SAMPLES_DIR + File.separator + "File2.txt", Constants.SAMPLES_DIR + File.separator + "Result.txt");

		//File22 doesn't exist
		treeMerger.mergeTwoTreesByScanner(Constants.SAMPLES_DIR + File.separator + "File11.txt", Constants.SAMPLES_DIR + File.separator + "File22.txt", Constants.SAMPLES_DIR + File.separator + "Result.txt");

		//FileEmpty1.txt is empty
		treeMerger.mergeTwoTreesByScanner(Constants.SAMPLES_DIR + File.separator + "FileEmpty1.txt", Constants.SAMPLES_DIR + File.separator + "File2.txt", Constants.SAMPLES_DIR + File.separator + "Result.txt");

		//FileEmpty2.txt is empty
		treeMerger.mergeTwoTreesByScanner(Constants.SAMPLES_DIR + File.separator + "File1.txt", Constants.SAMPLES_DIR + File.separator + "FileEmpty2.txt", Constants.SAMPLES_DIR + File.separator + "Result.txt");
	}
}
