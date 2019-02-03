package framework.core.drivers.web;

import framework.core.drivers.Core;
import framework.core.models.Data;
import framework.core.utils.TestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.internal.TestResult;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class WebCore extends Core<WebDriver> {

	public static final Logger logger = LogManager.getLogger(WebCore.class);

	private static WebDriver getDriverFromResult(ITestResult result) {
		return (WebDriver) result.getAttribute("web.driver");
	}

	/**
	 * Opens the browser as part of the test class.
	 */
	@BeforeMethod(alwaysRun = true)
	public void openBrowser(Method testMethod, ITestResult result, Data data) {
		try {

			WebDriver driver = getWebDriver();
			if (driver != null) {
				result.setAttribute("web.driver", driver);
				driver.manage().timeouts().pageLoadTimeout(getTestConfig().getPageLoadTimeout(), TimeUnit.SECONDS);
				driver.manage().timeouts().setScriptTimeout(getTestConfig().getScriptTimeout(), TimeUnit.SECONDS);
				driver.manage().window().maximize();

				if (driver instanceof RemoteWebDriver) {
					logger.debug(
						"The thread id is {} and session id is {} {} {}",
						Thread.currentThread().getId(),
						((RemoteWebDriver) driver).getSessionId()
					);
				}
				beforeStartTest(testMethod, result, data, driver);
			}
		} catch (Exception e) {
			result.setStatus(TestResult.SKIP);
			closeBrowser(result);
			logger.error("Error while initializing the test", e);
			throw new SkipException("Error while initializing the test", e);
		}
	}

	protected void beforeStartTest(Method testMethod, ITestResult result, Data data, WebDriver driver) {
	}

	protected void beforeEndTest(Method testMethod, ITestResult result, Data data) {
	}

	@AfterMethod(alwaysRun = true)
	public void endTest(ITestResult result, Method testMethod, Data data) {

		WebDriver driver = null;
		try {
			driver = getDriverFromResult(result);
			logger.debug("Web driver is {} {}", driver, Thread.currentThread().getId());
		} catch (Exception e) {
			logger.error("Error while ending the test {}", getDriverFromResult(result), e);
		} finally {
			if (driver != null) {
				TestUtils.wait(10000);
				closeBrowser(result);
			}
		}
	}

	public void closeBrowser(ITestResult result) {
		try {
			WebDriver driver = getDriverFromResult(result);
			if (driver != null) {
				driver.quit();
			}
		} catch (Exception excp) {
			logger.error("Error while closing browser", excp);
		} finally {
			result.removeAttribute("web.driver");
			logger.debug("Web driver is {} {}", getDriverFromResult(result), Thread.currentThread().getId());
		}
	}

}