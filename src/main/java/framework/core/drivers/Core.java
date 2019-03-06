package framework.core.drivers;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import framework.core.drivers.utils.DriverFactory;
import framework.core.models.*;
import framework.core.utils.ConfigLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//import mmarquee.automation.controls.AutomationWindow;

//TODO: BB - need to remove dependency on WebDriver to allow generic methods handled by "Driver"
public abstract class Core<T> {

	private static Logger logger = LogManager.getLogger(Core.class);

	private static Config config;
	private static TestConfig testConfig;
	private static DriverConfig driverConfig;
	private static Environment environment;
	private static String outputDir;
	private static String screenShotDir;
	private static String dataDir;
	private static String reportDir;
	private static String htmlReportDir;
	private static WebDriver driver;

	public static void initialize() {
		logger.debug("Initializing Core");
		loadConfigs();
		initializePaths();
	}

	private static void loadConfigs() {
		try {
			logger.debug("Loading configs");
			config = ConfigLoader.getConfig();
			testConfig = config.getTestConfig();
			driverConfig = config.getDriverConfig();
			environment = config.getEnvironments().get(System.getProperty("environment"));
		} catch (Exception e) {
			logger.error("Exception:loadConfigs = {}", e.getMessage());
		}
	}

	private static void initializePaths() {
		logger.debug("Creating output directories");

		outputDir = testConfig.getOutputDirectory();
		screenShotDir = outputDir + "/screenshots";
		dataDir = outputDir + "/data";
		reportDir = outputDir + "/reports";
		htmlReportDir = reportDir + "/html";
		String[] paths = { outputDir, screenShotDir, dataDir, reportDir, htmlReportDir };

		for (String path : paths) {
			try {
				Path p = Paths.get(path);
				if (!Files.exists(p)) {
					Files.createDirectory(p);
				}
			} catch (Throwable e) {
				logger.error("Exception:initializePaths = {}", e.getMessage());
			}
		}
	}

	public static Config getConfig() {
		return config;
	}

	public static TestConfig getTestConfig() {
		return testConfig;
	}

	public static Environment getEnvironment() {
		return environment;
	}

	public static DriverConfig getDriverConfig() {
		return driverConfig;
	}

	public static String getOutputDir() {
		return outputDir;
	}

	public static String getScreenShotDir() {
		return screenShotDir;
	}

	public static String getDataDir() {
		return dataDir;
	}

	public static String getReportDir() {
		return reportDir;
	}

	public static String getHtmlReportDir() {
		return htmlReportDir;
	}

	public static boolean isDriverNull() {
		return (driver == null);
	}

	public static WebDriver getWebDriver() {
        if (driver != null && !driver.toString().contains("(null)")) {
			return driver;
		} else {
			return startWebDriver();
		}
	}

	public static synchronized WebDriver startWebDriver() {
		DriverFactory factory = new DriverFactory(getDriverConfig());
		driver = factory.createDriver();
		//driver.manage().window().setSize(new org.openqa.selenium.Dimension(1024, 768));
		driver.manage().window().maximize();
		String window = driver.getWindowHandle();
		((JavascriptExecutor) driver).executeScript("alert('Test')");
		driver.switchTo().alert().accept();
		driver.switchTo().window(window);
		return driver;
	}

	public static synchronized void stopWebDriver() {
		driver.quit();
		driver = null;
	}

	public static void report(String reportMessage) {
		Context.getScenario().write(reportMessage);
	}

	public static void reportWithScreenshot(String reportMessage) {
		report(reportMessage);
		embedScreenshot();
	}

	public static void embedScreenshot() {
		try {
			BufferedImage inputImage = null;
			if (driver != null) {

				logger.debug("Getting screenshot from webdriver");
				byte[] screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				inputImage = ImageIO.read(new ByteArrayInputStream(screenShot));

			} else if (System.getProperty("os.name").contains("Windows")) {

				WinDef.HWND winHandle = User32.INSTANCE.GetForegroundWindow();
				WinDef.RECT rect = new WinDef.RECT();
				User32.INSTANCE.GetWindowRect(winHandle, rect);
				logger.debug("Getting screenshot from robot");
				Rectangle screenRect = rect.toRectangle();
				inputImage = new Robot().createScreenCapture(screenRect);

			} else if (System.getProperty("os.name").contains("Mac")) {

				logger.debug("Getting screenshot from robot");
				Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
				inputImage = new Robot().createScreenCapture(screenRect);
			}
			embedScreenshotInReport(inputImage);

		} catch (Exception e) {
			logger.error("Error encountered while capturing screenshot: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	private static void embedScreenshotInReport(BufferedImage inputImage) {
		try {
			int scaledWidth = ((testConfig.getScreenshotWidth() != 0) ?
				testConfig.getScreenshotWidth() :
				inputImage.getWidth());
			int scaledHeight = ((testConfig.getScreenshotHeight() != 0) ?
				testConfig.getScreenshotHeight() :
				inputImage.getHeight());
			// scales the input image to the output image
			BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
			Graphics2D g2d = outputImage.createGraphics();
			g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
			g2d.dispose();
			ByteArrayOutputStream resizedScreenShot = new ByteArrayOutputStream();
			ImageIO.write(outputImage, "png", resizedScreenShot);

			Context.getScenario().embed(resizedScreenShot.toByteArray(), "image/png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}