/*===============================================================================================================================
        CLASS Name:    Queries
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Queries abstract class to enable Queries models
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.models;

import java.util.Collection;
import java.util.HashMap;

public class Queries {

    private HashMap<String, String> queries = new HashMap<>();

    public HashMap<String, String> getQueries() {
        return queries;
    }

    public void setQueries(HashMap<String, String> queries) {
        this.queries = queries;
    }

    public String getQueryByName(String name) {
        return queries.get(name);
    }

    public void addQuery(String name, String query) {
        queries.put(name, query);
    }

    public Boolean contains(String name) {
        return queries.containsKey(name);
    }

    public Collection<String> getAllQueryValues() {
        return queries.values();
    }

}

