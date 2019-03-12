/*===============================================================================================================================
        CLASS Name:    DriverFactory
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Driver Factory class to initialize WebDriver
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.drivers.utils;

import framework.core.drivers.Driver;
import framework.core.exceptions.FrameworkException;
import framework.core.models.DriverConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static framework.core.drivers.Driver.*;

public class DriverFactory<T extends WebDriver> {

	private static final Logger logger = LogManager.getLogger(DriverFactory.class);
	private DriverConfig driverConfig;

	public DriverFactory(DriverConfig driverConfig) {
		this.driverConfig = driverConfig;
	}

	@SuppressWarnings("unchecked")
	public WebDriver createDriver() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		WebDriver webDriver = null;
		Driver driver = null;
		WebDriver driver1 = null;
		// default to chrome
		String browserName = "chrome";
		try {
			for (Map.Entry<String, String> capability : driverConfig.getDesiredCapabilities().entrySet()) {
				capabilities.setCapability(capability.getKey(), capability.getValue());
			}

			if (System.getProperty("browser").isEmpty()) {
				browserName = capabilities.getBrowserName();
			} else {
				browserName = System.getProperty("browser");
			}


			if (browserName.equals(chrome.getDriverName())) {
				WebDriverManager.chromedriver().setup();
				//driver = Driver.getChromeDriver(browserName, capabilities);
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments(driverConfig.getChrome().get("args"));
				if (driverConfig.getChrome().containsKey("path")) {
					if (driverConfig.getChrome().get("path") != null)
						System.setProperty("webdriver.chrome.driver", driverConfig.getChrome().get("path"));
					//chromeOptions.setBinary(driverConfig.getChrome().get("path"));
				}
				capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
				webDriver = new ChromeDriver(chromeOptions);
			}

			if (browserName.equals(firefox.getDriverName())) {
				WebDriverManager.firefoxdriver().setup();
				//driver = Driver.getFirefoxDriver(browserName, capabilities);
				//FirefoxProfile firefoxProfile = new FirefoxProfile();
				/*for (Map.Entry<String, String> capability : driverConfig.getFirefox().entrySet()) {
					firefoxProfile.setPreference(capability.getKey(), capability.getValue());
				}
                if (driverConfig.getFirefox().containsKey("path")) {
                    if (driverConfig.getFirefox().get("path") != null)
                        System.setProperty("webdriver.gecko.driver", driverConfig.getFirefox().get("path"));
                }
				capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);*/
				webDriver = new FirefoxDriver();
			}

			if (browserName.equals(ie.getDriverName())) {
				//driver = Driver.getIeDriver(browserName, capabilities);
				WebDriverManager.iedriver().setup();
				for (Map.Entry<String, String> capability : driverConfig.getIe().entrySet()) {
					capabilities.setCapability(capability.getKey(), capability.getValue());
				}
				if (driverConfig.getIe().containsKey("path")) {
					if (driverConfig.getFirefox().get("path") != null)
						System.setProperty("webdriver.ie.driver", driverConfig.getIe().get("path"));
				}
				capabilities.setCapability(InternetExplorerDriver.
						INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				webDriver = new InternetExplorerDriver();
			}

			if (browserName.equals(edge.getDriverName())) {
				//driver = Driver.getEdgeDriver(browserName, capabilities);
				WebDriverManager.edgedriver().setup();
				for (Map.Entry<String, String> capability : driverConfig.getEdge().entrySet()) {
					capabilities.setCapability(capability.getKey(), capability.getValue());
				}
				if (driverConfig.getEdge().containsKey("path")) {
					if (driverConfig.getFirefox().get("path") != null)
						System.setProperty("webdriver.edge.driver", driverConfig.getEdge().get("path"));
				}
				webDriver = new EdgeDriver();
			}

			if (browserName.equals(safari.getDriverName())) {
				driver = Driver.getSafariDriver(browserName, capabilities);
				//WebDriverManager.().setup();
				for (Map.Entry<String, String> capability : driverConfig.getSafari().entrySet()) {
					capabilities.setCapability(capability.getKey(), capability.getValue());
				}
				if (driverConfig.getSafari().containsKey("path")) {
					if (driverConfig.getFirefox().get("path") != null)
						System.setProperty("webdriver.safari.driver", driverConfig.getEdge().get("path"));
				}
			}

			if (browserName.equals(remote.getDriverName())) {
				driver = Driver.getRemoteDriver(browserName, capabilities);
				for (Map.Entry<String, String> capability : driverConfig.getRemote().entrySet()) {
					capabilities.setCapability(capability.getKey(), capability.getValue());
				}
			}

			//logger.debug("Desired capabilties {}", capabilities.toString());
			//webDriver = driver.getDriverClass().getConstructor(Capabilities.class).newInstance(capabilities);

		} catch (Exception e) {
			logger.error("Error while initializing driver {} : error message: {}", browserName, e.getMessage());
			throw new FrameworkException("Error while intializing web driver", e);
		}

		return webDriver;
	}

	private boolean isHubEnabled(String hubURL) {
		logger.debug("Is Hub enabled {} {} {}", hubURL, hubURL != null, !StringUtils.isEmpty(hubURL));
		return !StringUtils.isEmpty(hubURL);
	}
}