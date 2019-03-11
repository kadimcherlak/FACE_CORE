/*===============================================================================================================================
        CLASS Name:    Dropdown
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Drop down methods for Web framework
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.drivers.web.components;

import framework.core.drivers.utils.ElementLocatorWrapper;
import framework.core.drivers.web.WebPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.Select;

public class Dropdown extends BaseElement<Dropdown> {

    private static final Logger logger = LogManager.getLogger(Dropdown.class);

    public Dropdown(ElementLocatorWrapper wrapper, WebPage page) {
        super(wrapper, page);
    }

    public Dropdown select(String value) {
        Select select = new Select(getElement());
        select.selectByVisibleText(value);
        logger.info(" " + value + " : is selected in the drop down ");
        return this;
    }
}
