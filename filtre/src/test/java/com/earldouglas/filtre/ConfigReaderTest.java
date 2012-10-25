package com.earldouglas.filtre;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ConfigReaderTest {

	@Test
	public void testConfigReader() {
		ConfigReader configReader = new ConfigReader(
				"classpath:filtre-test1.properties");
		assertEquals("127.0.0.1", configReader.getCompositeWhiteList());
		assertEquals(null, configReader.getCompositeBlackList());
		assertNull(configReader.getCompositeBlackList());
	}
}
