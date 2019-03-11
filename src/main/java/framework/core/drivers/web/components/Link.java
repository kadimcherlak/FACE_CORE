/*===============================================================================================================================
        CLASS Name:    Link
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Link methods for Web framework
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.drivers.web.components;

import framework.core.drivers.utils.ElementLocatorWrapper;
import framework.core.drivers.web.WebPage;

public class Link extends BaseElement<Link> {

    public Link(ElementLocatorWrapper wrapper, WebPage page) {
        super(wrapper, page);
    }

    public String href() {
        return getElement().getAttribute("href");
    }

    public String desc() {
        return getElement().getText();
    }

    public Link click() {
        getElement().click();
        return this;
    }
}
