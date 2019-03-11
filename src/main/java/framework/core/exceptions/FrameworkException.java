/*===============================================================================================================================
        CLASS Name:    FrameworkException
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Framework Exception to handle Exception methods
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.exceptions;

public class FrameworkException extends RuntimeException {

    private static final long serialVersionUID = -1L;

    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameworkException(String message) {
        super(message);
    }

}
