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

