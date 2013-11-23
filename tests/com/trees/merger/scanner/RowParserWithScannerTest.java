package com.trees.merger.scanner;

import java.util.List;
import junit.framework.TestCase;


public class RowParserWithScannerTest extends TestCase {

	public void testNodePath() {
		String row = "A/B/C : 3";
		RowParserWithScanner rowScanner = new RowParserWithScanner(row);
		assertEquals("A/B/C", rowScanner.getNodePath());
	}

	public void testNodePath2() {
		String row = "A1/B1 : 2";
		RowParserWithScanner rowScanner = new RowParserWithScanner(row);
		assertEquals("A1/B1", rowScanner.getNodePath());
	}

	public void testNodeValue() {
		String row = "A/B/C : 3";
		RowParserWithScanner rowScanner = new RowParserWithScanner(row);
		assertEquals(3, rowScanner.getNodeValue().intValue());
	}
	public void testNodeValue2() {
		String row = "A1/B1 : 2";
		RowParserWithScanner rowScanner = new RowParserWithScanner(row);
		assertEquals(2, rowScanner.getNodeValue().intValue());
	}

	public void testSetDelimiter() {
		String row = "A/B/C : 3";
		RowParserWithScanner rowScanner = new RowParserWithScanner(row);
		assertEquals("/", rowScanner.getScanner().delimiter().toString());
	}

	public void testSetDelimiter2() {
		String row = "A/B/C : 1";
		RowParserWithScanner rowScanner = new RowParserWithScanner(row);
		assertEquals("/", rowScanner.getScanner().delimiter().toString());
	}

	public void testSetDelimiter3() {
		String row = "A1.B1.C1.D : 3";
		RowParserWithScanner rowScanner = new RowParserWithScanner(row);
		assertEquals("\\.", rowScanner.getScanner().delimiter().toString());
	}

	public void testScanNodePath() {
		String row = "A/B/C : 2";
		RowParserWithScanner rowScanner = new RowParserWithScanner(row);
		List<String> nodePathKeys = rowScanner.nodePathAsMapKeys();
		assertEquals(3, nodePathKeys.size());
		assertTrue(nodePathKeys.contains("A"));
		assertTrue(nodePathKeys.contains("A/B"));
		assertTrue(nodePathKeys.contains("A/B/C"));
	}

	public void testScanNodePath2() {
		String row = "A1.B2.C4.D : 5";
		RowParserWithScanner rowScanner = new RowParserWithScanner(row);
		List<String> nodePathKeys = rowScanner.nodePathAsMapKeys();
		assertEquals(4, nodePathKeys.size());
		assertTrue(nodePathKeys.contains("A1"));
		assertTrue(nodePathKeys.contains("A1/B2"));
		assertTrue(nodePathKeys.contains("A1/B2/C4"));
		assertTrue(nodePathKeys.contains("A1/B2/C4/D"));
	}
}
