/*===============================================================================================================================
        CLASS Name:    TestReport
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   TestReport class to enable TestReport models
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.models;

import java.util.LinkedList;
import java.util.List;

public class TestReport {

    private String featureId;
    private String scenarioId;
    private String testId;
    private String environmentId;
    private long startTime;
    private long endTime;
    private String description;
    private TestStatus status;
    private List<TestStatus> results = new LinkedList<TestStatus>();

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(String environmentId) {
        this.environmentId = environmentId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public List<TestStatus> getResults() {
        return results;
    }

    public void setResults(List<TestStatus> results) {
        this.results = results;
    }

    public void addResult(TestStatus result) {
        this.results.add(result);
    }

}
