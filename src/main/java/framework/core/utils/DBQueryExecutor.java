/*===============================================================================================================================
        CLASS Name:    DBQueryExecutor
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   DBQueryExecutor utility class to enable DB query
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.utils;

import framework.core.models.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DBQueryExecutor implements Task {

    private static final Logger frameworkLogger = LogManager.getLogger(DBQueryExecutor.class);

    private List<QueryResults> results = new ArrayList<>();
    private Queries queries;
    private Config config;
    private Environment environment;
    private Data data;

    public DBQueryExecutor(Context context) {
        this.config = context.getConfig();
        this.data = context.getData();
        this.environment = context.getEnvironment();
    }

    public List<QueryResults> getResults() {
        return results;
    }

    public void execute(List<String> queryNames) {
        DBConnectionManager connMan = DBConnectionManager.create(environment);

        try {
            queries = populateQueries(queryNames);
            long start = System.currentTimeMillis();
            int threadcounter;
            frameworkLogger.info("Starting queries: time start: {}", (System.currentTimeMillis() - start));

            PreparedStatement[] statements = new PreparedStatement[queries.getAllQueryValues().size()];
            threadcounter = 0;
            for (String query : queries.getAllQueryValues()) {
                query = StringUtils.chomp(query);

                Connection conn = connMan.getConnection();
                statements[threadcounter] = conn.prepareStatement(query);

                threadcounter++;
            }

            threadcounter = 0;
            for (PreparedStatement st : statements) {
                st.execute();
                frameworkLogger.info("statement: {}, time start: {}", threadcounter, (System.currentTimeMillis()));
                threadcounter++;
            }

            threadcounter = 0;
            for (PreparedStatement st : statements) {
                processResult(st);
                st.close();
                frameworkLogger
                        .info("statement: {}, time completed: {}", threadcounter, (System.currentTimeMillis() - start));
            }

            frameworkLogger
                    .info("Completed traversal of result sets, time taken = {}", (System.currentTimeMillis() - start));

        } catch (Exception e) {
            frameworkLogger.error("Error while processing sqls {}", queries.toString(), e);
        }
    }

    private void processResult(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.getResultSet();

        if (rs.getFetchSize() > 0) {
            HashSet<String> columns = extractColumnNames(rs);

            while (rs.next()) {
                QueryResults qrs = new QueryResults();

                for (String column : columns) {
                    String val = rs.getString(column);
                    if (!StringUtils.isEmpty(val)) {
                        qrs.add(column, val);
                    }
                }
                results.add(qrs);

            }
        } else {
            frameworkLogger.info("No records found for the current template");
        }
    }

    private HashSet<String> extractColumnNames(ResultSet queryResult) throws SQLException {
        ResultSetMetaData metaData = queryResult.getMetaData();
        HashSet<String> resultColumns = new HashSet<>();
        for (int count = 0; count < metaData.getColumnCount(); count++) {
            resultColumns.add(metaData.getColumnName(count + 1).toUpperCase());
        }
        return resultColumns;
    }

    private Queries populateQueries(List<String> queriesToPopulate) {
        Queries returnQueries = new Queries();

        // load all query yamls to query objects
        List<Queries> allQueriesObjects = ConfigLoader.parseQueries();

        for (String name : queriesToPopulate) {
            for (Queries queriesObject : allQueriesObjects) {
                if (queriesObject.contains(name)) {
                    String test = queriesObject.getQueryByName(name);
                    returnQueries.addQuery(name, queriesObject.getQueryByName(name));
                }
            }
        }
        return returnQueries;
    }

}