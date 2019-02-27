package framework.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import framework.core.models.Data;
import framework.core.models.DataStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLoader {

	private static final Logger logger = LogManager.getLogger(DataLoader.class);

	public static DataStore loadDataStoreFromYaml(String yamlToLoad, String directoryToSearch, Data dataClassToMap) {
		return loadDataStoreFromYaml(FileUtils.findFile(yamlToLoad, directoryToSearch), dataClassToMap);
	}

	public static XSSFWorkbook loadExcel(String exlToLoad) throws Exception {
		FileInputStream file = new FileInputStream(new File(exlToLoad));
		XSSFWorkbook book = new XSSFWorkbook(file);
		file.close();
		return book;
	}

	public static DataStore loadDataStroreFromExcel(String exlToLoad,String sheetToLoad, Data dataClass) throws Exception{
		XSSFWorkbook book = loadExcel(exlToLoad);
		XSSFSheet sheet = book.getSheet(sheetToLoad);
		DataStore ds = new DataStore();

		Map<String, Map<String, String>> sheetToMap = new HashMap<>();
		List<String> rowKeys = new ArrayList<>();
		DataFormatter formatter = new DataFormatter();

		int rowCounter = 0;
		for (Row row: sheet){
			HashMap<String, String> rowMap = new HashMap<>();
			if(rowCounter == 0) {
				for (Cell cell: row){
					rowKeys.add(cell.getStringCellValue());
				}
			}
			else{
				List<String> rowVals = new ArrayList<>();
				for(int i=0; i < rowKeys.size(); i++){
					Cell cell = row.getCell(i);
					if(cell==null || cell.getCellType()==Cell.CELL_TYPE_BLANK)
						rowVals.add("");
					else
						rowVals.add(formatter.formatCellValue(cell));
				}
				for(int i=0; i < rowKeys.size(); i++){
					rowMap.put(rowKeys.get(i), rowVals.get(i));
				}
				sheetToMap.put(rowMap.get("scenario"), rowMap);
			}
			rowCounter+=1;
		}

		ObjectMapper mapper = new ObjectMapper();
		for(Map.Entry<String, Map<String, String>> data : sheetToMap.entrySet()){
			ds.addNode(data.getKey(), mapper.convertValue(data.getValue(), dataClass.getClass()));
		}
		return ds;
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
