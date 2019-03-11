/*===============================================================================================================================
        CLASS Name:    ConfigLoader
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   ConfigLoader utility class to load runtime config
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.utils;

import framework.core.exceptions.FrameworkException;
import framework.core.models.Config;
import framework.core.models.Queries;
import framework.core.models.TestConfig;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigLoader {

    private static final Logger logger = LogManager.getLogger(ConfigLoader.class);
    private static Config config;

    public static Config getConfig() {
        try {
            Yaml yaml = new Yaml(new Constructor(Config.class));
            InputStream input = FileUtils.openInputStream(new File("src/main/resources/config.yaml"));
            config = (Config) yaml.load(input);

        } catch (Exception e) {
            logger.error("Error encountered while loading config.yaml: {}", e.getMessage());
            e.printStackTrace();
            System.out.println(System.getProperty("user.dir"));
        }
        return config;
    }

    public static List<Queries> parseQueries() {
        List<Queries> queries = new ArrayList<>();
        TestConfig test = config.getTestConfig();

        try {
            List<File> queryFiles = Files.walk(Paths.get(test.getQueriesDirectory()))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            for (File queryFile : queryFiles) {
                Yaml yaml = new Yaml(new Constructor(Queries.class));

                InputStream input = null;
                try {
                    input = new FileInputStream(queryFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                queries.add(yaml.loadAs(input, Queries.class));
            }
        } catch (IOException ioe) {
            throw new FrameworkException("IO Exception", ioe);
        }
        return queries;
    }

}
