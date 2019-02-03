package framework.core.drivers;

import framework.core.models.Context;
import framework.core.models.Environment;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class Page<T> {

	public WebDriver driver;
	public Environment environment;
	public Logger logger;
	private Context context;

	public Page(Context context) {
		this.context = context;
		this.driver = context.getDriver();
		this.environment = context.getEnvironment();
		this.logger = context.getLogger();
	}

	public static void report(String reportMessage) {
		Core.report(reportMessage);
	}

	public static void reportWithScreenShot(String reportMessage) {
		Core.reportWithScreenshot(reportMessage);
	}

	public Context getContext() {
		return context;
	}

}