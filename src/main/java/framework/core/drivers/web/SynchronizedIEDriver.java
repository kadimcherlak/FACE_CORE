/*===============================================================================================================================
        CLASS Name:    SynchronizedIEDriver
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Synchronized IE Driver for Web framework
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.drivers.web;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.Response;

import java.util.Map;

public class SynchronizedIEDriver extends InternetExplorerDriver {

    public SynchronizedIEDriver() {
        super();
    }

    public SynchronizedIEDriver(Capabilities capabilities) {
        super(capabilities);
    }

    public SynchronizedIEDriver(int port) {
        super(port);
    }

    public SynchronizedIEDriver(InternetExplorerDriverService service, Capabilities capabilities, int port) {
        super(service, capabilities, port);
    }

    public SynchronizedIEDriver(InternetExplorerDriverService service, Capabilities capabilities) {
        super(service, capabilities);
    }

    public SynchronizedIEDriver(InternetExplorerDriverService service) {
        super(service);
    }

    @Override
    protected Response execute(String driverCommand, Map<String, ?> parameters) {
        synchronized (this) {
            return super.execute(driverCommand, parameters);
        }
    }
}
