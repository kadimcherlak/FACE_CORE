/*===============================================================================================================================
        CLASS Name:    QueryResults
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   QueryResults abstract class to enable QueryResults models
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.models;

import java.util.HashMap;
import java.util.Map;

public class QueryResults {

    private final Map<String, String> data = new HashMap<>();

    public void add(String columnName, String value) {
        this.data.put(columnName, value);
    }

    public String get(String columnName) {
        return data.get(columnName);
    }

    public void copyData(QueryResults toCopy) {
        this.data.putAll(toCopy.data);
    }

    public boolean isColumn(String columnName) {
        return data.containsKey(columnName);
    }

    @Override
    public String toString() {
        return "QueryResults [models=" + data + "]";
    }
}