package com.mentorstudies.automationframework.util.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class ConfigReader {

    private static Map<String, Map<String, String>> propertyCache = null;

    static {
	propertyCache = new HashMap<String, Map<String, String>>(1);
    }

    public static String getProperty(String propertyFileName, String propertyName) {

	String returnValue = null;

	// if the property file is not read alreday
	if (propertyCache.get(propertyFileName) == null) {

	    // read data from property file and put them into cache
	    loadProperties(propertyFileName);
	}

	Map<String, String> propertyData = propertyCache.get(propertyFileName);
	returnValue = propertyData.get(propertyName);

	return returnValue;
    }

    public static Map<String, String> getAllProperties(String propertyFileName) {

	if (propertyCache.get(propertyFileName) == null) {
	    loadProperties(propertyFileName);
	}

	return propertyCache.get(propertyFileName);
    }

    private static void loadProperties(String propertyFileName) {
	Properties propertyFile = new Properties();
	try {
	    propertyFile.load(ConfigReader.class.getClassLoader().getResourceAsStream(propertyFileName));
	    Map<String, String> propertyFileData = new HashMap<String, String>();
	    for (Entry<Object, Object> keyValEntry : propertyFile.entrySet()) {
		String key = keyValEntry.getKey().toString();
		String value = keyValEntry.getValue().toString();
		propertyFileData.put(key, value);
	    }
	    propertyCache.put(propertyFileName, propertyFileData);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
