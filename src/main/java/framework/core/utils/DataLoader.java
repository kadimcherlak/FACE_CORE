package framework.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import framework.core.models.Data;
import framework.core.models.DataStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class DataLoader {

	private static final Logger logger = LogManager.getLogger(DataLoader.class);

	public static DataStore loadDataStoreFromYaml(String yamlToLoad, String directoryToSearch, Data dataClassToMap) {
		return loadDataStoreFromYaml(FileUtils.findFile(yamlToLoad, directoryToSearch), dataClassToMap);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static DataStore loadDataStoreFromYaml(String yamlToLoad, Data dataClass) {
		logger.debug("Mapping {}", yamlToLoad);
		DataStore ds = new DataStore();
		try {
			Representer rep = new Representer();
			rep.getPropertyUtils().setSkipMissingProperties(true);
			String yamlIn = new String(Files.readAllBytes(Paths.get(yamlToLoad)));
			Map<String, Map<String, String>> dataToMap = (Map<String, Map<String, String>>) new Yaml(rep).load(yamlIn);

			ObjectMapper mapper = new ObjectMapper();
			for (Map.Entry<String, Map<String, String>> data : dataToMap.entrySet()) {
				ds.addNode(data.getKey(), mapper.convertValue(data.getValue(), dataClass.getClass()));
			}

		} catch (Exception e) {
			logger.error("Exception:loadDataStoreFromYaml = Error loading {} - {}", yamlToLoad, e.getMessage());
			e.printStackTrace();
		}
		return ds;
	}

}
