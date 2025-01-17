/*===============================================================================================================================
        CLASS Name:    Config
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Config methods to enable Config models
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.models;

import java.util.Map;

public class Config {

	private TestConfig testConfig;
	private Map<String, Environment> environments;
	private DriverConfig driverConfig;

	public TestConfig getTestConfig() {
		return testConfig;
	}

	public void setTestConfig(TestConfig testConfig) {
		this.testConfig = testConfig;
	}

	public Map<String, Environment> getEnvironments() {
		return environments;
	}

	public void setEnvironments(Map<String, Environment> environments) {
		this.environments = environments;
	}

	public DriverConfig getDriverConfig() {
		return driverConfig;
	}

	public void setDriverConfig(DriverConfig driverConfig) {
		this.driverConfig = driverConfig;
	}

}

