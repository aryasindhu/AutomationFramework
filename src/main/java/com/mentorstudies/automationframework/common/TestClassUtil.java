package com.mentorstudies.automationframework.common;

import org.testng.annotations.DataProvider;

public class TestClassUtil {

	@DataProvider(name="defaultDP")
	public static Object[][] getTestData() {
		return new Object[][] { {} };
	}

}
