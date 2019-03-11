/*===============================================================================================================================
        CLASS Name:    TestConfig
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   TestConfig class to enable TestConfig models
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.models;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestConfig extends Config {

    private String queriesDirectory;
    private String outputDirectory;
    private List<String> glue = null;
    private List<String> listeners = null;
    private List<String> reporters = null;
    private List<String> plugins = null;
    private Boolean returnScreenshotOnEveryAction;
    private int scriptTimeout;
    private int pageLoadTimeout;
    private int screenshotHeight;
    private int screenshotWidth;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getQueriesDirectory() {
        return queriesDirectory;
    }

    public void setQueriesDirectory(String queriesDirectory) {
        this.queriesDirectory = queriesDirectory;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public List<String> getGlue() {
        return glue;
    }

    public void setGlue(List<String> glue) {
        this.glue = glue;
    }

    public List<String> getListeners() {
        return listeners;
    }

    public void setListeners(List<String> listeners) {
        this.listeners = listeners;
    }

    public List<String> getReporters() {
        return reporters;
    }

    public void setReporters(List<String> reporters) {
        this.reporters = reporters;
    }

    public List<String> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<String> plugins) {
        this.plugins = plugins;
    }

    public Boolean getReturnScreenshotOnEveryAction() {
        return returnScreenshotOnEveryAction;
    }

    public void setReturnScreenshotOnEveryAction(Boolean returnScreenshotOnEveryAction) {
        this.returnScreenshotOnEveryAction = returnScreenshotOnEveryAction;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public int getScriptTimeout() {
        return this.scriptTimeout;
    }

    public void setScriptTimeout(int scriptTimeout) {
        this.scriptTimeout = scriptTimeout;
    }

    public int getPageLoadTimeout() {
        return this.pageLoadTimeout;
    }

    public void setPageLoadTimeout(int pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
    }

    public int getScreenshotHeight() {
        return this.screenshotHeight;
    }

    public void setScreenshotHeight(int screenshotHeight) {
        this.screenshotHeight = screenshotHeight;
    }

    public int getScreenshotWidth() {
        return this.screenshotWidth;
    }

    public void setScreenshotWidth(int screenshotWidth) {
        this.screenshotWidth = screenshotWidth;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("queriesDirectory", queriesDirectory)
                .append("outputDirectory", outputDirectory)
                .append("glue", glue)
                .append("listeners", listeners)
                .append("reporters", reporters)
                .append("plugins", plugins)
                .append("returnScreenshotOnEveryAction", returnScreenshotOnEveryAction)
                .append("scriptTimeout", scriptTimeout)
                .append("pageLoadTimeout", pageLoadTimeout)
                .append("screenshotHeight", screenshotHeight)
                .append("screenshotWidth", screenshotWidth)
                .append("additionalProperties", additionalProperties).toString();

    }

}

