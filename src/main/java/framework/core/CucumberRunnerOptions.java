/*===============================================================================================================================
        CLASS Name:    CucumberRunnerOptions
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   CucumberRunnerOptions class to set CucumberRunnerOptions
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import framework.core.models.CukeOptions;
import framework.core.models.TestConfig;
import framework.core.utils.AnnotationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CucumberRunnerOptions {

	private static final Logger logger = LogManager.getLogger(CucumberRunnerOptions.class);

	public static void load(TestConfig testConfig) {
		CukeOptions options = populateOptions(testConfig);
		AnnotationUtils.alterAnnotationOn(CucumberRunner.class, CucumberOptions.class, options);
	}

	private static CukeOptions populateOptions(TestConfig config) {

		String featureDir = System.getProperty("featureDir");
		logger.info("featureDir parameter passed from maven : {}", featureDir);

		String environment = System.getProperty("environment");
		logger.info("environment parameter passed from maven : {}", environment);

		String tags = System.getProperty("tags");
		logger.info("tags parameter passed from maven : {}", tags);

		String features = System.getProperty("user.dir") + "/src/main/resources/features/" + featureDir;
		logger.info("Features loaded from : {}", features);

		String glue = "framework.tests.steps." + featureDir;
		logger.info("Glue parameter set to : {}", glue);

		CukeOptions cukeOptions = new CukeOptions();
		try {
			cukeOptions.setStrict(true);
			cukeOptions.setDryRun(false);
			cukeOptions.setMonochrome(true);
			cukeOptions.setFeatures(new String[] { features });
			cukeOptions.setTags(new String[] { tags });
			cukeOptions.setGlue(new String[] { glue });
			cukeOptions.setPlugin(config.getPlugins().toArray(new String[config.getPlugins().size()]));
			cukeOptions.setName(new String[] {});
			cukeOptions.setSnippets(SnippetType.UNDERSCORE);
			cukeOptions.setJunit(new String[] {});

		} catch (Exception e) {
			logger.error("Exception:populateOptions = Problem setting options values - {}", e.getMessage());
			e.printStackTrace();
		}
		return cukeOptions;
	}

}