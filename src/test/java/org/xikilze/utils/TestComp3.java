package org.xikilze.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestComp3 {

	@Test
	public void test() {
		assertEquals("1234", HexUtils.decodeComp3AsString(new byte[]{0x12, 0x34}));
		assertEquals("1234", HexUtils.decodeComp3AsString(new byte[]{0x01, 0x23, 0x4F}));
		assertEquals("+1234", HexUtils.decodeComp3AsString(new byte[]{0x01, 0x23, 0x4C}));
		assertEquals("-1234", HexUtils.decodeComp3AsString(new byte[]{0x01, 0x23, 0x4D}));
//		assertEquals("0", HexUtils.decodeComp3AsString(new byte[]{0x00}));
		assertEquals("+1", HexUtils.decodeComp3AsString(new byte[]{0x1C}));
		assertEquals("-1", HexUtils.decodeComp3AsString(new byte[]{0x1D}));
	}

}
