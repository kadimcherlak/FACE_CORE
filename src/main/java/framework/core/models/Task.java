/*===============================================================================================================================
        CLASS Name:    Task
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Task Interface class to enable Task models
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.models;

import java.util.List;

@FunctionalInterface
public interface Task {
    void execute(List<String> queryNames);
}
