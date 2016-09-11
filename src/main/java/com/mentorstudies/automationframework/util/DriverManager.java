package com.mentorstudies.automationframework.util;

import org.openqa.selenium.WebDriver;

import com.mentorstudies.automationframework.exception.AutomationFrameworkException;

public interface DriverManager {

    public WebDriver getDriver() throws AutomationFrameworkException;

}
