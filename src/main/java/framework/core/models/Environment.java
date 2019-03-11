/*===============================================================================================================================
        CLASS Name:    Environment
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Environment class to enable Environment models
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.models;

public class Environment extends Config {

    String webUrl;
    String webUsername;
    String webPassword;
    String dbUrl;
    String dbUsername;
    String dbPassword;
    String dbSchema;
    String dbDriver;
    Boolean dbUseConnectionPool;
    int dbPoolsize;
    int dbMaxConnection;

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebUsername() {
        return webUsername;
    }

    public void setWebUsername(String webUsername) {
        this.webUsername = webUsername;
    }

    public String getWebPassword() {
        return webPassword;
    }

    public void setWebPassword(String webPassword) {
        this.webPassword = webPassword;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbSchema() {
        return dbSchema;
    }

    public void setDbSchema(String dbSchema) {
        this.dbSchema = dbSchema;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public Boolean getDbUseConnectionPool() {
        return dbUseConnectionPool;
    }

    public void setDbUseConnectionPool(Boolean dbUseConnectionPool) {
        this.dbUseConnectionPool = dbUseConnectionPool;
    }

    public int getDbPoolSize() {
        return dbPoolsize;
    }

    public void setDbPoolSize(int dbPoolSize) {
        this.dbPoolsize = dbPoolSize;
    }

    public int getMaxConnection() {
        return dbMaxConnection;
    }

    public void setDbMaxConnection(int dbMaxConnection) {
        this.dbMaxConnection = dbMaxConnection;
    }

}

