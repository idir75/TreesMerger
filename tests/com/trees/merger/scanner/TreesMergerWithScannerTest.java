package com.trees.merger.scanner;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


import junit.framework.TestCase;


public class TreesMergerWithScannerTest extends TestCase {

	private static final String FILE1_PATH = Constants.SAMPLES_DIR + File.separator + "tFile1.txt";
	private static final String FILE2_PATH = Constants.SAMPLES_DIR + File.separator + "tFile2.txt";
	private static final String RESULT_FILE_PATH = Constants.SAMPLES_DIR + File.separator + "tResutl.txt";

	public void setUp() throws Exception {
		super.setUp();
	}

	public void tearDown() throws Exception {
		super.tearDown();
		File file1 = new File(FILE1_PATH);
		if (file1 != null) {
			file1.delete();
		}
		File file2 = new File(FILE2_PATH); 
		if (file2 != null) {
			file2.delete();
		}
		File resultFile = new File(RESULT_FILE_PATH);
		if (resultFile != null) {
			resultFile.delete();
		}
	}

	public void testSerializeDataIntoFile() {
		String str = "A.B : 1\nA.B.C : 1\nA.B.D : 3\nA.C : 1\nA2/B/C : 4";
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		scannerTreesMerger.serializeDataIntoFile(RESULT_FILE_PATH, str);
		Scanner scanner = scannerTreesMerger.getFileScanner(RESULT_FILE_PATH);
		assertEquals("A.B : 1", scanner.nextLine());
		assertEquals("A.B.C : 1", scanner.nextLine());
		assertEquals("A.B.D : 3", scanner.nextLine());
		assertEquals("A.C : 1", scanner.nextLine());
		assertEquals("A2/B/C : 4", scanner.nextLine());
		scanner.close();
	}

	public void testSerializeDataIntoFile2() {
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		String str = "A : 8\nA/B : 7\nA/B/C : 2\nA/B/D : 3\nA/C : 1\nA1 : 1\nA1/B1 : 1\nA1/B1/C : 1\nA2 : 4\nA2/B : 4\nA2/B/C : 4";
		scannerTreesMerger.serializeDataIntoFile(RESULT_FILE_PATH, str);
		Scanner scanner = scannerTreesMerger.getFileScanner(RESULT_FILE_PATH);
		assertEquals("A : 8", scanner.nextLine());
		assertEquals("A/B : 7", scanner.nextLine());
		assertEquals("A/B/C : 2", scanner.nextLine());
		assertEquals("A/B/D : 3", scanner.nextLine());
		assertEquals("A/C : 1", scanner.nextLine());
		assertEquals("A1 : 1", scanner.nextLine());
		assertEquals("A1/B1 : 1", scanner.nextLine());
		assertEquals("A1/B1/C : 1", scanner.nextLine());
		assertEquals("A2 : 4", scanner.nextLine());
		assertEquals("A2/B : 4", scanner.nextLine());
		assertEquals("A2/B/C : 4", scanner.nextLine());
		scanner.close();
	}

	public void testFormatMap() {
		Map<String, Integer> map = new TreeMap<String, Integer>();
		map.put("A/B", new Integer(1));
		map.put("A/B/C", new Integer(1));
		map.put("A1/B1/C", new Integer(1));
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		String mapAsString = scannerTreesMerger.formatMapToString(map);
		assertEquals("A/B : 1\nA/B/C : 1\nA1/B1/C : 1", mapAsString);
	}

	public void testFormatMap2() {
		Map<String, Integer> map = new TreeMap<String, Integer>();
		map.put("A.B", new Integer(1));
		map.put("A.B.C", new Integer(1));
		map.put("A.B.D", new Integer(3));
		map.put("A.C", new Integer(1));
		map.put("A2/B/C", new Integer(4));
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		String mapAsString = scannerTreesMerger.formatMapToString(map);
		assertEquals("A.B : 1\nA.B.C : 1\nA.B.D : 3\nA.C : 1\nA2/B/C : 4", mapAsString);
	}

	public void testGetMapFromFileByScanner() {
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		Map<String, Integer> map = new TreeMap<String, Integer>();
		String str = "A.B : 1\nA.B.C : 1\nA.B.D : 3\nA.C : 1\nA2/B/C : 4";
		scannerTreesMerger.serializeDataIntoFile(FILE1_PATH, str);
		Scanner fileScanner = scannerTreesMerger.getFileScanner(FILE1_PATH);
		scannerTreesMerger.getMapFromFileByScanner(fileScanner, map);
		assertEquals(8, map.size());
		List<String> keys = Arrays.asList("A","A/B","A/B/C", "A/B/D", "A/C", "A2", "A2/B", "A2/B/C"); 
		map.keySet().containsAll(keys);
		assertEquals(6, map.get("A").intValue());
		assertEquals(5, map.get("A/B").intValue());
		assertEquals(1, map.get("A/B/C").intValue());
		assertEquals(3, map.get("A/B/D").intValue());
		assertEquals(1, map.get("A/C").intValue());
		assertEquals(4, map.get("A2").intValue());
		assertEquals(4, map.get("A2/B").intValue());
		assertEquals(4, map.get("A2/B/C").intValue());
		fileScanner.close();
	}

	public void testGetMapFromFileByScanner2() {
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		Map<String, Integer> map = new TreeMap<String, Integer>();
		String str = "A/B/C : 1\nA/B : 1\nA1/B1/C : 1";
		scannerTreesMerger.serializeDataIntoFile(FILE1_PATH, str);
		Scanner fileScanner = scannerTreesMerger.getFileScanner(FILE1_PATH);
		scannerTreesMerger.getMapFromFileByScanner(fileScanner, map);
		assertEquals(6, map.size());
		List<String> keys = Arrays.asList("A", "A/B", "A/B/C", "A1", "A1/B1", "A1/B1/C"); 
		map.keySet().containsAll(keys);
		assertEquals(2, map.get("A").intValue());
		assertEquals(2, map.get("A/B").intValue());
		assertEquals(1, map.get("A/B/C").intValue());
		assertEquals(1, map.get("A1").intValue());
		assertEquals(1, map.get("A1/B1").intValue());
		assertEquals(1, map.get("A1/B1/C").intValue());
		fileScanner.close();
	}

	public void testMergeTwoTreesByScanner() {
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		String str1 = "A/B/C : 1\nA/B : 1\nA1/B1/C : 1";
		scannerTreesMerger.serializeDataIntoFile(FILE1_PATH, str1);
		String str2 = "A.C : 1\nA.B : 1\nA.B.D : 3\nA.B.C : 1\nA2/B/C : 4";
		scannerTreesMerger.serializeDataIntoFile(FILE2_PATH, str2);
		scannerTreesMerger.mergeTwoTreesByScanner(FILE1_PATH, FILE2_PATH, RESULT_FILE_PATH);
		Scanner resultScanner = scannerTreesMerger.getFileScanner(RESULT_FILE_PATH);
		assertEquals("A : 8", resultScanner.nextLine());
		assertEquals("A/B : 7", resultScanner.nextLine());
		assertEquals("A/B/C : 2", resultScanner.nextLine());
		assertEquals("A/B/D : 3", resultScanner.nextLine());
		assertEquals("A/C : 1", resultScanner.nextLine());
		assertEquals("A1 : 1", resultScanner.nextLine());
		assertEquals("A1/B1 : 1", resultScanner.nextLine());
		assertEquals("A1/B1/C : 1", resultScanner.nextLine());
		assertEquals("A2 : 4", resultScanner.nextLine());
		assertEquals("A2/B : 4", resultScanner.nextLine());
		assertEquals("A2/B/C : 4", resultScanner.nextLine());
		resultScanner.close();
	}

	public void testMergeTwoTreesByScanner2() {
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		String str1 = "A/B/C : 1\nA/B : 1\nA1/B1/C : 1";
		scannerTreesMerger.serializeDataIntoFile(FILE1_PATH, str1);
		String str2 = "A/B/C : 1\nA/B : 1\nA1/B1/C : 1";
		scannerTreesMerger.serializeDataIntoFile(FILE2_PATH, str2);
		scannerTreesMerger.mergeTwoTreesByScanner(FILE1_PATH, FILE2_PATH, RESULT_FILE_PATH);
		Scanner resultScanner = scannerTreesMerger.getFileScanner(RESULT_FILE_PATH);
		assertEquals("A : 4", resultScanner.nextLine());
		assertEquals("A/B : 4", resultScanner.nextLine());
		assertEquals("A/B/C : 2", resultScanner.nextLine());
		assertEquals("A1 : 2", resultScanner.nextLine());
		assertEquals("A1/B1 : 2", resultScanner.nextLine());
		assertEquals("A1/B1/C : 2", resultScanner.nextLine());
		resultScanner.close();
	}

	public void testMergeTwoTreesByScanner3() {
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		String str1 = "A.C : 1\nA.B : 1\nA.B.D : 3\nA.B.C : 1\nA2/B/C : 4";
		scannerTreesMerger.serializeDataIntoFile(FILE1_PATH, str1);
		String str2 = "A.C : 1\nA.B : 1\nA.B.D : 3\nA.B.C : 1\nA2/B/C : 4";
		scannerTreesMerger.serializeDataIntoFile(FILE2_PATH, str2);
		scannerTreesMerger.mergeTwoTreesByScanner(FILE1_PATH, FILE2_PATH, RESULT_FILE_PATH);
		Scanner resultScanner = scannerTreesMerger.getFileScanner(RESULT_FILE_PATH);
		assertEquals("A : 12", resultScanner.nextLine());
		assertEquals("A/B : 10", resultScanner.nextLine());
		assertEquals("A/B/C : 2", resultScanner.nextLine());
		assertEquals("A/B/D : 6", resultScanner.nextLine());
		assertEquals("A/C : 2", resultScanner.nextLine());
		assertEquals("A2 : 8", resultScanner.nextLine());
		assertEquals("A2/B : 8", resultScanner.nextLine());
		assertEquals("A2/B/C : 8", resultScanner.nextLine());
		resultScanner.close();
	}

	public void testMergeTwoTreesByScanner4() {
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		String str1 = "A : 8\nA/B : 7\nA/B/C : 2\nA/B/D : 3\nA/C : 1\nA1 : 1\nA1/B1 : 1\nA1/B1/C : 1\nA2 : 4\nA2/B : 4\nA2/B/C : 4";
		scannerTreesMerger.serializeDataIntoFile(FILE1_PATH, str1);
		String str2 = "A.C : 1\nA.B : 1\nA.B.D : 3\nA.B.C : 1\nA2/B/C : 4";
		scannerTreesMerger.serializeDataIntoFile(FILE2_PATH, str2);
		scannerTreesMerger.mergeTwoTreesByScanner(FILE1_PATH, FILE2_PATH, RESULT_FILE_PATH);
		Scanner resultScanner = scannerTreesMerger.getFileScanner(RESULT_FILE_PATH);
		assertEquals("A : 27", resultScanner.nextLine());
		assertEquals("A/B : 17", resultScanner.nextLine());
		assertEquals("A/B/C : 3", resultScanner.nextLine());
		assertEquals("A/B/D : 6", resultScanner.nextLine());
		assertEquals("A/C : 2", resultScanner.nextLine());
		assertEquals("A1 : 3", resultScanner.nextLine());
		assertEquals("A1/B1 : 2", resultScanner.nextLine());
		assertEquals("A1/B1/C : 1", resultScanner.nextLine());
		assertEquals("A2 : 16", resultScanner.nextLine());
		assertEquals("A2/B : 12", resultScanner.nextLine());
		assertEquals("A2/B/C : 8", resultScanner.nextLine());
		resultScanner.close();
	}

	public void testMergeTwoTreesByScannerEmptyFile1() {
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		String str1 = "";
		scannerTreesMerger.serializeDataIntoFile(FILE1_PATH, str1);
		String str2 = "A.C : 1\nA.B : 1\nA.B.D : 3\nA.B.C : 1\nA2/B/C : 4";
		scannerTreesMerger.serializeDataIntoFile(FILE2_PATH, str2);
		scannerTreesMerger.mergeTwoTreesByScanner(FILE1_PATH, FILE2_PATH, RESULT_FILE_PATH);
		Scanner resultScanner = scannerTreesMerger.getFileScanner(RESULT_FILE_PATH);
		assertEquals("A/C : 1", resultScanner.nextLine());
		assertEquals("A/B : 1", resultScanner.nextLine());
		assertEquals("A/B/D : 3", resultScanner.nextLine());
		assertEquals("A/B/C : 1", resultScanner.nextLine());
		assertEquals("A2/B/C : 4", resultScanner.nextLine());
		resultScanner.close();
	}

	public void testMergeTwoTreesByScannerEmptyFile2() {
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		String str1 = "A.C : 1\nA.B : 1\nA.B.D : 3\nA.B.C : 1\nA2/B/C : 4";
		scannerTreesMerger.serializeDataIntoFile(FILE1_PATH, str1);
		String str2 = "";
		scannerTreesMerger.serializeDataIntoFile(FILE2_PATH, str2);
		scannerTreesMerger.mergeTwoTreesByScanner(FILE1_PATH, FILE2_PATH, RESULT_FILE_PATH);
		Scanner resultScanner = scannerTreesMerger.getFileScanner(RESULT_FILE_PATH);
		assertEquals("A/C : 1", resultScanner.nextLine());
		assertEquals("A/B : 1", resultScanner.nextLine());
		assertEquals("A/B/D : 3", resultScanner.nextLine());
		assertEquals("A/B/C : 1", resultScanner.nextLine());
		assertEquals("A2/B/C : 4", resultScanner.nextLine());
		resultScanner.close();
	}

	public void testMergeTwoTreesByScannerNoFile1() {
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		String str2 = "A.C : 1\nA.B : 1\nA.B.D : 3\nA.B.C : 1\nA2/B/C : 4";
		scannerTreesMerger.serializeDataIntoFile(FILE2_PATH, str2);
		scannerTreesMerger.mergeTwoTreesByScanner("noFile.txt", FILE2_PATH, RESULT_FILE_PATH);
		Scanner resultScanner = scannerTreesMerger.getFileScanner(RESULT_FILE_PATH);
		assertEquals("A/C : 1", resultScanner.nextLine());
		assertEquals("A/B : 1", resultScanner.nextLine());
		assertEquals("A/B/D : 3", resultScanner.nextLine());
		assertEquals("A/B/C : 1", resultScanner.nextLine());
		assertEquals("A2/B/C : 4", resultScanner.nextLine());
		resultScanner.close();
	}

	public void testMergeTwoTreesByScannerNoFile2() {
		TreesMergerWithScanner scannerTreesMerger = new TreesMergerWithScanner();
		String str1 = "A/B/C : 1\nA/B : 1\nA1/B1/C : 1";
		scannerTreesMerger.serializeDataIntoFile(FILE1_PATH, str1);
		scannerTreesMerger.mergeTwoTreesByScanner(FILE1_PATH, "", RESULT_FILE_PATH);
		Scanner resultScanner = scannerTreesMerger.getFileScanner(RESULT_FILE_PATH);
		assertEquals("A/B/C : 1", resultScanner.nextLine());
		assertEquals("A/B : 1", resultScanner.nextLine());
		assertEquals("A1/B1/C : 1", resultScanner.nextLine());
		resultScanner.close();
	}
}
