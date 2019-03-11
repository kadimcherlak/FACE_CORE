/*===============================================================================================================================
        CLASS Name:    Context
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Context Abstract class to enable Context models
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.models;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public abstract class Context {

	private static Scenario scenario;
	private Logger logger = LogManager.getLogger(Context.class);
	private WebDriver driver;
	private Config config;
	private Environment environment;
	private DataStore dataStore;
	private Data data;

	public Context() {
	}

	public static Scenario getScenario() {
		return scenario;
	}

	public static void setScenario(Scenario s) {
		scenario = s;
	}

	public Logger getLogger() {
		return logger;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public Config getConfig() {
		return config;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public Data getData() {
		return data;
	}

	public abstract void setData(String key);

	@Before
	public abstract void beforeScenario(Scenario scenario);

	@After
	public abstract void afterScenario(Scenario scenario);

}