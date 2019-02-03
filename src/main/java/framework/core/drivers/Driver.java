package framework.core.drivers;

import framework.core.drivers.web.SynchronizedIEDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.ArrayList;
import java.util.List;

public enum Driver {

	chrome(DesiredCapabilities.chrome(), ChromeDriver.class, RemoteWebDriver.class),
	firefox(DesiredCapabilities.firefox(), FirefoxDriver.class, RemoteWebDriver.class),
	ie(DesiredCapabilities.internetExplorer(), SynchronizedIEDriver.class, RemoteWebDriver.class),
	edge(DesiredCapabilities.edge(), EdgeDriver.class, RemoteWebDriver.class),
	safari(DesiredCapabilities.safari(), SafariDriver.class, RemoteWebDriver.class),
	remote(new DesiredCapabilities(), ChromeDriver.class, RemoteWebDriver.class);

	private final Class<? extends WebDriver> driverClass;
	private final Class<? extends WebDriver> hubEnabledDriver;
	private final String driverName;
	private DesiredCapabilities capabilities;

	Driver(DesiredCapabilities capabilities, Class<? extends WebDriver> driverClass,
		   Class<? extends WebDriver> hubEnabledDriver) {
		this.driverName = name();
		this.capabilities = capabilities;
		this.driverClass = driverClass;
		this.hubEnabledDriver = hubEnabledDriver;

	}

	Driver(DesiredCapabilities capabilities, Class<? extends WebDriver> driverClass) {
		this(capabilities, driverClass, driverClass);
	}

	Driver(DesiredCapabilities capabilities) {
		this(capabilities, null);
	}

	public static Driver get(String browserName, DesiredCapabilities capabilities) {
		Driver returnDriver = Driver.remote;
		for (Driver driver : Driver.values()) {
			if (driver.driverName.equals(browserName)) {
				returnDriver = driver;
				returnDriver.capabilities = capabilities;
				break;
			}
		}
		return returnDriver;
	}

	public static List<String> supportedBrowsers() {
		List<String> names = new ArrayList<String>();
		for (Driver driver : Driver.values()) {
			names.add(driver.name());
		}
		return names;
	}

	public Class<? extends WebDriver> getDriverClass() {
		return driverClass;
	}

	public DesiredCapabilities getCapabilities() {
		return new DesiredCapabilities(capabilities.asMap());
	}

	public String getDriverName() {
		return driverName;
	}
}