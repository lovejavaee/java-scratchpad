package com.earldouglas.filtre;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AddressBlockTest {

	@Test
	public void testSingleAddress() throws AddressFormatException {
		AddressBlock addressBlock = new AddressBlock("127.0.0.1");
		assertTrue(addressBlock.contains(2130706433L));
		assertFalse(addressBlock.contains(2130706433L - 1));
		assertFalse(addressBlock.contains(2130706433L + 1));
	}

	@Test
	public void testAddressRange() throws AddressFormatException {
		AddressBlock addressBlock = new AddressBlock("127.0.0.1/32");
		assertTrue(addressBlock.contains(2130706433L));
		assertFalse(addressBlock.contains(2130706433L - 1));
		assertFalse(addressBlock.contains(2130706433L + 1));

		addressBlock = new AddressBlock("127.0.0.1/0");
		assertTrue(addressBlock.contains(2130706433L));
		assertTrue(addressBlock.contains(0));
		assertTrue(addressBlock.contains(4294967295L));
	}
}
