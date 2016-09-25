package com.mentorstudies.automationframework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.TestNG;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.mentorstudies.automationframework.util.common.ConfigReader;

/**
 * 
 * @author aryasindhusahu
 *
 */
public class TestRunner {

    private static String packageNames = null;
    private static String testName = null;

    public static void main(String[] args) {

	// initialize
	initialize();

	System.out.println("Starting Test Run");

	TestNG testNG = new TestNG();

	XmlSuite suite = new XmlSuite();
	suite.setName("My Test Suite");

	XmlTest test = new XmlTest(suite);

	// TODO : Multiple package names through config property file
	List<XmlPackage> packages = new ArrayList<XmlPackage>();

	for (String eachPackageName : packageNames.split(",")) {
	    System.out.println("Addng Package :" + eachPackageName);
	    packages.add(new XmlPackage(eachPackageName.trim()));
	}

	test.setPackages(packages);
	test.setName(testName);

	List<XmlSuite> suites = new ArrayList<XmlSuite>();
	suites.add(suite);
	testNG.setXmlSuites(suites);

	testNG.setUseDefaultListeners(true);
	testNG.setOutputDirectory("output");

	testNG.run();

    }

    // method to initialize system related things eg :include system.properties
    private static void initialize() {

	// set system properties for chrome and ie drivers
	Map<String, String> systemProperties = ConfigReader.getAllProperties("system.properties");
	for (Entry<String, String> eachProperty : systemProperties.entrySet()) {
	    System.setProperty(eachProperty.getKey(), eachProperty.getValue());
	}

	// initialize other properties
	packageNames = ConfigReader.getProperty("config.properties", "TEST_PACKAGES");
	testName = ConfigReader.getProperty("config.properties", "TEST_NAME");

    }

}
