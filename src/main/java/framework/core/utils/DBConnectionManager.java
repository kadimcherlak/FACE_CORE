/*===============================================================================================================================
        CLASS Name:    DBConnectionManager
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   DBConnectionManager utility class to enable DB connection
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import framework.core.exceptions.FrameworkException;
import framework.core.models.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionManager {

    private static final Logger frameworkLogger = LogManager.getLogger(DBConnectionManager.class);

    private static DBConnectionManager connMan;
    private static Environment environment;
    private ComboPooledDataSource connPool;

    private DBConnectionManager() {
        try {
            connPool = new ComboPooledDataSource();
            connPool.setJdbcUrl(environment.getDbUrl());
            connPool.setUser(environment.getDbUsername());
            connPool.setPassword(environment.getDbPassword());
            connPool.setInitialPoolSize(5);
            connPool.setMinPoolSize(5);
            connPool.setAcquireIncrement(5);
            connPool.setMaxPoolSize(environment.getDbPoolSize());
            connPool.setMaxStatements(environment.getDbPoolSize() * 10);
        } catch (Exception e) {
            frameworkLogger.error("Error while creating connection manager", e.getMessage());
        }
    }

    public static DBConnectionManager create(Environment env) {
        environment = env;
        if (connMan == null)
            connMan = new DBConnectionManager();
        return connMan;
    }

    public Connection getConnection() {
        Connection con = null;
        try {
            con = connPool.getConnection();
        } catch (SQLException e) {
            frameworkLogger.error("Error while obtaining db connection", e.getMessage());
        }
        return con;
    }

    public void destroy() {
        try {
            connPool.close();
        } catch (Exception e) {
            frameworkLogger.error("Error while closing db connection pool", e);
            throw new FrameworkException("Error while closing the db connection pool", e);
        }
        frameworkLogger.info("Connection manager shutdown complete");
    }

}
