package utils;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverUtil {

	private static WebDriver driver = null;
	private static WebDriver helperDriver = null;

	public static WebDriver getWebDriver() {
		if (driver == null) {
			driver = new FirefoxDriver();
			// profile['download.prompt_for_download'] = false
		}
		return driver;
	}

	public static WebDriver getHelperWebDriver() {
		if (helperDriver == null) {

			helperDriver = new FirefoxDriver();
		}
		return helperDriver;
	}

	public static void defaultGet(String url) {
		driver.get(url);
		sleep();
	}

	public static void defaultClickLink(String linkName) {
		driver.findElement(By.linkText(linkName)).click();
		sleep();
	}

	public static void defaultClickLink(String linkName, int index) {
		List<WebElement> elements = driver.findElements(By.linkText(linkName));
		elements.get(index).click();
		sleep();
	}

	public static void sleep() {
		String secondsToSleep = "2";//PropsUtil.get("webdriver.thread.sleep.seconds");
		ThreadUtil.sleep(new Integer(secondsToSleep));
	}

	public static void defaultSelectFromMultiple(String selectName, String selectValue) {
		List<WebElement> radiosMrktType = driver.findElements(By.name(selectName));
		for (WebElement option : radiosMrktType) {
			if (selectValue.equals(option.getText())) {
				option.getText();
				break;
			}
		}
	}

	public static void defaultSelectOptionFromSelectDropDown(String selectName, String selectValue) {
		WebElement selectElement = driver.findElement(By.name(selectName));
		List<WebElement> options = selectElement.findElements(By.tagName("option"));
		for (WebElement option : options) {
			if (selectValue.equals(option.getText())) {
				option.getText();
				break;
			}
		}
	}

	public static void defaultSendKeysToInput(String inputFieldName, String value) {
		WebDriverUtil.getWebDriver().findElement(By.name(inputFieldName)).sendKeys(value);
	}

	public static void defaultButtonClick(String buttonName) {
		WebDriverUtil.getWebDriver().findElement(By.name(buttonName)).click();

	}

	public static void ignoreJSPopup() {
		// ((JavascriptExecutor)
		// driver).executeScript("window.confirm = function(msg) { return true; }");
	}

	public static void closeAll() {
		if (driver != null)
			driver.close();
		if (helperDriver != null)
			helperDriver.close();
	}

}
