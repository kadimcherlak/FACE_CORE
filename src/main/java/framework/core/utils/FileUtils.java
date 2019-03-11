/*===============================================================================================================================
        CLASS Name:    FileUtils
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   FileUtils utility class to enable File operations
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileUtils {

    private static final Logger logger = LogManager.getLogger(FileUtils.class);

    public static String findFile(String fileToFind, String directoryToSearch) {
        String found = null;
        try (Stream<Path> matches = Files.find(Paths.get(directoryToSearch), 5,
                (path, attr) -> String.valueOf(path).endsWith(fileToFind))) {
            logger.debug("Searching {}", fileToFind);
            found = matches.map(String::valueOf).findFirst().get();
            logger.debug("Found {}", found);
        } catch (Throwable e) {
            logger.error("Problem encountered trying to list files from {}: {}", directoryToSearch, e.getMessage());
        }
        return found;
    }

}
