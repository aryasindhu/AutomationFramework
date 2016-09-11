package com.mentorstudies.automationframework;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

/**
 * 
 * @author aryasindhusahu
 *
 */
public class TestRunner {

    public static void main(String[] args) {

	System.out.println("Starting Test Run");

	TestNG testNG = new TestNG();

	XmlSuite suite = new XmlSuite();
	suite.setName("My Test Suite");

//	suite.addListener("org.uncommons.reportng.HTMLReporter.class");
//	suite.addListener("org.uncommons.reportng.JUnitXMLReporter.class");

	XmlTest test = new XmlTest(suite);
	test.setName("Calculator Service Tests");
	List<XmlClass> classes = new ArrayList<XmlClass>();
	classes.add(new XmlClass("com.automation.test.CalculatorTest"));
	test.setXmlClasses(classes);

	test = new XmlTest(suite);
	test.setName("Login Tests");
	classes = new ArrayList<XmlClass>();
	classes.add(new XmlClass("com.automation.test.LoginTest"));
	test.setXmlClasses(classes);

	List<XmlSuite> suites = new ArrayList<XmlSuite>();
	suites.add(suite);
	testNG.setXmlSuites(suites);

	testNG.setUseDefaultListeners(false);
	testNG.setOutputDirectory("output");

	testNG.run();

    }

}
