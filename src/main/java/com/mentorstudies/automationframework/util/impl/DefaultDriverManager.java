package com.mentorstudies.automationframework.util.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.mentorstudies.automationframework.common.Browser;
import com.mentorstudies.automationframework.exception.AutomationFrameworkException;
import com.mentorstudies.automationframework.util.DriverManager;
import com.mentorstudies.automationframework.util.common.ConfigReader;

public class DefaultDriverManager implements DriverManager {

    @Override
    public WebDriver getDriver() throws AutomationFrameworkException {

	// create a Web Driver and return it to the client
	String browser = ConfigReader.getProperty("config.properties", "BROWSER");
	WebDriver driver = null;

	if (browser.equals(Browser.FIREFOX)) {
	    driver = new FirefoxDriver();

	} else if (browser.equals(Browser.CHROME)) {
	    driver = new ChromeDriver();

	} else if (browser.equals(Browser.IE)) {
	    driver = new InternetExplorerDriver();

	} else {
	    throw new AutomationFrameworkException("Browser [" + browser + "] Not Supported");
	}

	return driver;
    }

}
