package com.earldouglas.filtre;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigReader {

	private String compositeWhiteList;
	private String compositeBlackList;
	private Log log = LogFactory.getLog(getClass());

	public String getCompositeWhiteList() {
		return compositeWhiteList;
	}

	public String getCompositeBlackList() {
		return compositeBlackList;
	}

	public ConfigReader(String configLocation) {
		try {
			InputStream configInputStream;
			if (configLocation.startsWith("classpath:")) {
				String configFileName = configLocation.substring(10);
				configInputStream = ClassLoader.getSystemClassLoader()
						.getResourceAsStream(configFileName);
			} else {
				configInputStream = new FileInputStream(configLocation);
			}

			Properties properties = new Properties();
			properties.load(configInputStream);

			compositeWhiteList = properties.getProperty("whiteList");
			compositeBlackList = properties.getProperty("blackList");
		} catch (Exception e) {
			log.error("reading filtre config file '" + configLocation + "'", e);
		}

	}
}
