package framework.core.models;

import java.util.List;

@FunctionalInterface
public interface Task {
	void execute(List<String> queryNames);
}
