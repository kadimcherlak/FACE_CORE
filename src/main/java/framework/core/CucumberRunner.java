/*===============================================================================================================================
        CLASS Name:    CucumberRunner
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   CucumberRunner class to Run the cucumber based execution
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.PickleEventWrapper;
import cucumber.api.testng.TestNGCucumberRunner;
import framework.core.drivers.Core;
import framework.core.models.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

@CucumberOptions()
public class CucumberRunner {

	private static final Logger logger = LogManager.getLogger(CucumberRunner.class);
	private static TestNGCucumberRunner runner;
	private static Config config;

	public CucumberRunner() {
	}

	@BeforeClass
	public void setup() {
		try {

			StringWriter writer = new StringWriter();
			Properties props = System.getProperties();
			props.list(new PrintWriter(writer));
			logger.info("System properties set at runtime: {}", writer.toString());

			Core.initialize();
			config = Core.getConfig();
			CucumberRunnerOptions.load(config.getTestConfig());
			runner = new TestNGCucumberRunner(CucumberRunner.class);
		} catch (Exception e) {
			logger.error("Exception:setup = Failed to set up Cucumber - {}", e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(dataProvider = "scenarios")
	public void scenario(PickleEventWrapper pickleEvent, CucumberFeatureWrapper cucumberFeature) throws Throwable {
		runner.runScenario(pickleEvent.getPickleEvent());
	}

	@AfterClass
	public void teardown() {
		if (!Core.isDriverNull()) {
			Core.stopWebDriver();
		}
		logger.debug("Writing reports");
		runner.finish();
	}

	@DataProvider
	private Object[][] scenarios() {
		return runner.provideScenarios();
	}

}
