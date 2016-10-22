package com.mentorstudies.automationframework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
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
	private static String testClassesPackageName = null;
	private static String testName = null;
	private static String testSuiteName = null;
	private static String testCaseExcelFileName = null;
	private static List<String> testCasesToRun = new ArrayList<String>();
	private static List<String> testCasesToSkip = new ArrayList<String>();

	public static void main(String[] args) throws IOException {

		// initialize
		initialize();

		System.out.println("Starting Test Run");

		TestNG testNG = new TestNG();

		XmlSuite suite = new XmlSuite();
		suite.setName(testSuiteName);

		XmlTest test = new XmlTest(suite);

		/*
		 * // TODO : Multiple package names through config property file
		 * List<XmlPackage> packages = new ArrayList<XmlPackage>();
		 * 
		 * for (String eachPackageName : packageNames.split(",")) {
		 * System.out.println("Addng Package :" + eachPackageName);
		 * packages.add(new XmlPackage(eachPackageName.trim())); }
		 * 
		 * test.setPackages(packages); test.setName(testName);
		 */

		// part 1 : read excel file and generate the testcases to run
		readMethodDetailsFromExcel();

		List<XmlClass> classes = new ArrayList<XmlClass>();
		// part 2 : get all test methods from the package and check if a method
		// is
		// present in the toRunTestCases list, if present then add it to the
		// class in testng.xml file
		Map<String, List<String>> testMap = prepareTestNGClassMethodMap();

		String className = null;
		List<String> methodsOfClass = null;
		XmlClass eachXMLClass = null;
		List<XmlInclude> includeList = null;
		for (Entry<String, List<String>> eachClassMethodEntry : testMap.entrySet()) {

			className = eachClassMethodEntry.getKey();
			methodsOfClass = eachClassMethodEntry.getValue();

			eachXMLClass = new XmlClass(className);
			includeList = new ArrayList<XmlInclude>();
			for (String eachMethodOfClass : methodsOfClass) {
				includeList.add(new XmlInclude(eachMethodOfClass));
			}
			eachXMLClass.setIncludedMethods(includeList);
			classes.add(eachXMLClass);
		}

		//add the classes list to the xml test 
		test.setXmlClasses(classes);

		// map the test class and methods to testng format

		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);
		testNG.setXmlSuites(suites);

		testNG.setUseDefaultListeners(true);
		testNG.setOutputDirectory("output");

		testNG.run();

	}

	private static Map<String, List<String>> prepareTestNGClassMethodMap() {

		Map<String, List<String>> classMethodMap = new HashMap<String, List<String>>();

		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(
						new SubTypesScanner(
								false /* don't exclude Object.class */),
						new ResourcesScanner(), new MethodAnnotationsScanner())
				.setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
				.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(testClassesPackageName))));

		String eachMethodName = null;
		String eachClassName = null;
		List<String> classMethodsAdded = null;
		for (Method methodRef : reflections.getMethodsAnnotatedWith(org.testng.annotations.Test.class)) {
			eachMethodName = methodRef.getName();
			eachClassName = methodRef.getDeclaringClass().getName();

			if (testCasesToRun.contains(eachMethodName)) {
				classMethodsAdded = classMethodMap.get(eachClassName);
				if (classMethodsAdded == null) { // if this class was not added
													// to map earlier,
													// initialize
					classMethodsAdded = new ArrayList<String>();
				}
				classMethodsAdded.add(eachMethodName);
				classMethodMap.put(eachClassName, classMethodsAdded);
			}
		}

		return classMethodMap;
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
		testClassesPackageName = ConfigReader.getProperty("config.properties", "TESTCLASS_PACKAGE");
		testName = ConfigReader.getProperty("config.properties", "TEST_NAME");
		testSuiteName = ConfigReader.getProperty("config.properties", "TEST_SUITE_NAME");
		testCaseExcelFileName = ConfigReader.getProperty("config.properties", "TESTCASE_EXCEL_FILE");

	}

	private static void readMethodDetailsFromExcel() throws IOException {

		File myFile = new File(testCaseExcelFileName);
		FileInputStream fis = new FileInputStream(myFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		Iterator<Row> rowIterator = mySheet.iterator();
		rowIterator.next();// skipping the first row as it is a header
		String methodName = null;
		String run = null;
		Row row = null;
		Cell cell = null;
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			cell = row.getCell(0);
			methodName = cell.getStringCellValue().trim();

			cell = row.getCell(1);
			run = cell.getStringCellValue().trim();
			if (methodName != null && methodName.length() > 0 && run != null && run.length() > 0) {
				if ("YES".equalsIgnoreCase(run)) {
					testCasesToRun.add(methodName);
				} else if ("NO".equalsIgnoreCase(run)) {
					testCasesToSkip.add(methodName);
				}
			}
		}

		myWorkBook.close();
	}

}
