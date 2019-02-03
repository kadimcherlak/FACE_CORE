package framework.core.drivers.utils;

import framework.core.drivers.Driver;
import framework.core.exceptions.FrameworkException;
import framework.core.models.DriverConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
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
		WebDriver webDriver;

		// default to chrome
		String browserName = "chrome";
		try {
			for (Map.Entry<String, String> capability : driverConfig.getDesiredCapabilities().entrySet()) {
				capabilities.setCapability(capability.getKey(), capability.getValue());
			}

			browserName = capabilities.getBrowserName();
			Driver driver = Driver.get(browserName, capabilities);

			if (browserName.equals(chrome.getDriverName())) {
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments(driverConfig.getChrome().get("args"));
				if (driverConfig.getChrome().containsKey("path")) {
					if (driverConfig.getChrome().get("path") != null)
						chromeOptions.setBinary(driverConfig.getChrome().get("path"));
				}

				capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
			}

			if (browserName.equals(firefox.getDriverName())) {
				FirefoxProfile firefoxProfile = new FirefoxProfile();
				for (Map.Entry<String, String> capability : driverConfig.getFirefox().entrySet()) {
					firefoxProfile.setPreference(capability.getKey(), capability.getValue());
				}
				capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
			}

			if (browserName.equals(ie.getDriverName())) {
				for (Map.Entry<String, String> capability : driverConfig.getIe().entrySet()) {
					capabilities.setCapability(capability.getKey(), capability.getValue());
				}
			}

			if (browserName.equals(edge.getDriverName())) {
				for (Map.Entry<String, String> capability : driverConfig.getEdge().entrySet()) {
					capabilities.setCapability(capability.getKey(), capability.getValue());
				}
			}

			if (browserName.equals(safari.getDriverName())) {
				for (Map.Entry<String, String> capability : driverConfig.getSafari().entrySet()) {
					capabilities.setCapability(capability.getKey(), capability.getValue());
				}
			}

			if (browserName.equals(remote.getDriverName())) {
				for (Map.Entry<String, String> capability : driverConfig.getRemote().entrySet()) {
					capabilities.setCapability(capability.getKey(), capability.getValue());
				}
			}

			logger.debug("Desired capabilties {}", capabilities.toString());
			webDriver = driver.getDriverClass().getConstructor(Capabilities.class).newInstance(capabilities);

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