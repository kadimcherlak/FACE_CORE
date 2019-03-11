/*===============================================================================================================================
        CLASS Name:    TestLog
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   TestLog class to enable TestLog models
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.models;

import framework.core.utils.TestUtils;

public class TestLog {

    private final TestStatus status;
    private final String description;
    private final String details;
    private String screenshotPath;
    private long startTime;
    private long endTime;

    public TestLog(TestStatus status, String description, String details) {
        this.status = status;
        this.description = description;
        this.details = details;
        this.startTime = TestUtils.getTime();
        this.endTime = startTime;
    }

    public TestLog(TestStatus status, String description, String details, long startTime, long endTime) {
        this.status = status;
        this.description = description;
        this.details = details;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TestStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getDetails() {
        return details;
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

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public void setScreenshotPath(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }

}
