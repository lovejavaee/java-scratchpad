package com.earldouglas.filtre;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public class AddressManagerTest {

	private void testAddressManager(String address, String compositeWhiteList,
			String compositeBlackList, String configLocation, boolean accessible)
			throws Exception {

		AddressManager addressManager = new AddressManager();
		addressManager.extendWhiteList(compositeWhiteList);
		addressManager.extendBlackList(compositeBlackList);

		if (accessible) {
			assertTrue(addressManager.isAccessPermitted(address));
		} else {
			assertFalse(addressManager.isAccessPermitted(address));
		}
	}

	@Test
	public void testFiltre() throws Exception {
		testAddressManager("127.0.0.1", null, null, null, true);

		testAddressManager("127.0.0.1", "127.0.0.0,127.0.0.2", null, null,
				false);
		testAddressManager("127.0.0.1", "127.0.0.0,127.0.0.1,127.0.0.2", null,
				null, true);

		testAddressManager("127.0.0.1", "127.0.0.1", null, null, true);
		testAddressManager("127.0.0.1", "127.0.0.1/0", null, null, true);
		testAddressManager("127.0.0.1", "127.0.0.1/32", null, null, true);

		testAddressManager("127.0.0.1", "127.0.0.2", null, null, false);
		testAddressManager("127.0.0.1", "127.0.0.2/0", null, null, true);
		testAddressManager("127.0.0.1", "127.0.0.2/32", null, null, false);

		testAddressManager("127.0.0.1", null, "127.0.0.1", null, false);
		testAddressManager("127.0.0.1", null, "127.0.0.1/0", null, false);
		testAddressManager("127.0.0.1", null, "127.0.0.1/32", null, false);

		testAddressManager("127.0.0.1", null, "127.0.0.2", null, true);
		testAddressManager("127.0.0.1", null, "127.0.0.2/0", null, false);
		testAddressManager("127.0.0.1", null, "127.0.0.2/31", null, true);
		testAddressManager("127.0.0.1", null, "127.0.0.2/32", null, true);

		testAddressManager("127.0.0.1", null, "127.0.0.3", null, true);
		testAddressManager("127.0.0.1", null, "127.0.0.3/0", null, false);
		testAddressManager("127.0.0.1", null, "127.0.0.3/31", null, true);
		testAddressManager("127.0.0.1", null, "127.0.0.3/32", null, true);

	}

}
