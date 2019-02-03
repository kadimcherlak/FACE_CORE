package framework.core.models;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
	Map<String, Data> nodes = new HashMap<>();

	public Map<String, Data> getNodes() {
		return nodes;
	}

	public void addNodes(Map<String, Data> nodes) {
		this.nodes = nodes;
	}

	public void addNode(String key, Data data) {
		nodes.put(key, data);
	}

	public Data getNode(String key) {
		return nodes.get(key);
	}
}
