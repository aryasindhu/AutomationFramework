package com.mentorstudies.automationframework.util.common;

import java.util.Map;

import org.openqa.selenium.By;

import com.mentorstudies.automationframework.TestRunner;
import com.mentorstudies.automationframework.exception.AutomationFrameworkException;
import com.mentorstudies.automationframework.exception.KeywordNotFoundException;

public class KeyWordTool {

	public static By getLocator(String testCaseName, String locatorName) throws AutomationFrameworkException {

		Map<String, String> keywordMap = TestRunner.keywordDetails.get(testCaseName);
		String keyWordValue = keywordMap.get(locatorName);
		if (keyWordValue == null) {
			throw new KeywordNotFoundException(locatorName);
		}

		if (keyWordValue.startsWith("id")) {
			return By.id(keyWordValue.substring(keyWordValue.indexOf(':') + 1));
		}

		if (keyWordValue.startsWith("className")) {
			return By.className(keyWordValue.substring(keyWordValue.indexOf(':') + 1));
		}

		throw new AutomationFrameworkException("Keyword Type Unknown [" + keyWordValue + "]");
	}

}
