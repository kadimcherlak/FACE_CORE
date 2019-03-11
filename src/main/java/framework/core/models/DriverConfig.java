/*===============================================================================================================================
        CLASS Name:    DriverConfig
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Driver Config abstract class to enable DriverConfig models
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.models;

import java.util.HashMap;
import java.util.Map;

public class DriverConfig extends Config {

    private Map<String, String> desiredCapabilities = new HashMap<>();
    private Map<String, String> ie = new HashMap<>();
    private Map<String, String> edge = new HashMap<>();
    private Map<String, String> chrome = new HashMap<>();
    private Map<String, String> firefox = new HashMap<>();
    private Map<String, String> android = new HashMap<>();
    private Map<String, String> iphone = new HashMap<>();
    private Map<String, String> ipad = new HashMap<>();
    private Map<String, String> opera = new HashMap<>();
    private Map<String, String> safari = new HashMap<>();
    private Map<String, String> remote = new HashMap<>();

    public Map<String, String> getDesiredCapabilities() {
        return this.desiredCapabilities;
    }

    public void setDesiredCapabilities(Map<String, String> desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
    }

    public Map<String, String> getIe() {
        return this.ie;
    }

    public void setIe(Map<String, String> ie) {
        this.ie = ie;
    }

    public Map<String, String> getEdge() {
        return this.edge;
    }

    public void setEdge(Map<String, String> edge) {
        this.edge = edge;
    }

    public Map<String, String> getChrome() {
        return this.chrome;
    }

    public void setChrome(Map<String, String> chrome) {
        this.chrome = chrome;
    }

    public Map<String, String> getFirefox() {
        return this.firefox;
    }

    public void setFirefox(Map<String, String> firefox) {
        this.firefox = firefox;
    }

    public Map<String, String> getAndroid() {
        return this.android;
    }

    public void setAndroid(Map<String, String> android) {
        this.android = android;
    }

    public Map<String, String> getIphone() {
        return this.iphone;
    }

    public void setIphone(Map<String, String> iphone) {
        this.iphone = iphone;
    }

    public Map<String, String> getIpad() {
        return this.ipad;
    }

    public void setIpad(Map<String, String> ipad) {
        this.ipad = ipad;
    }

    public Map<String, String> getOpera() {
        return this.opera;
    }

    public void setOpera(Map<String, String> opera) {
        this.opera = opera;
    }

    public Map<String, String> getSafari() {
        return this.safari;
    }

    public void setSafari(Map<String, String> safari) {
        this.safari = safari;
    }

    public Map<String, String> getRemote() {
        return this.remote;
    }

    public void setRemote(Map<String, String> remote) {
        this.remote = remote;
    }

}

