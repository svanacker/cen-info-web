package org.cen.com;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComDataUtilsTest {

	@Test
	public void testFormatPos2_1() {
		String s = ComDataUtils.format(0x12345678, 2);
		assertEquals("78", s);
	}

	@Test
	public void testFormatPos4_1() {
		String s = ComDataUtils.format(0x12345678, 4);
		assertEquals("5678", s);
	}

	@Test
	public void testFormatNeg2_1() {
		String s = ComDataUtils.format(0xFEDCBA98, 2);
		assertEquals("98", s);
	}

	@Test
	public void testFormatNeg4_1() {
		String s = ComDataUtils.format(0xFEDCBA98, 4);
		assertEquals("BA98", s);
	}

	@Test
	public void testFormatPos2_2() {
		String s = ComDataUtils.format(0x1234, 2);
		assertEquals("34", s);
	}

	@Test
	public void testFormatPos4_2() {
		String s = ComDataUtils.format(0x1234, 4);
		assertEquals("1234", s);
	}

	@Test
	public void testFormatNeg2_2() {
		String s = ComDataUtils.format(0xFEDC, 2);
		assertEquals("DC", s);
	}

	@Test
	public void testFormatNeg4_2() {
		String s = ComDataUtils.format(0xFEDC, 4);
		assertEquals("FEDC", s);
	}

	@Test
	public void testFormatBooleanTrue() {
		assertEquals("1", ComDataUtils.format(true));
	}

	@Test
	public void testFormatBooleanFalse() {
		assertEquals("0", ComDataUtils.format(false));
	}
}
