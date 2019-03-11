/*===============================================================================================================================
        CLASS Name:    TextBox
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Text Box for Web framework
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.drivers.web.components;

import framework.core.drivers.utils.ElementLocatorWrapper;
import framework.core.drivers.web.WebPage;

public class TextBox extends BaseElement<TextBox> {

    public TextBox(ElementLocatorWrapper wrapper, WebPage page) {
        super(wrapper, page);
    }

    public TextBox enterText(CharSequence textBox) {
        clear();
        getElement().sendKeys(textBox);
        return this;
    }

    public TextBox clear() {
        getElement().clear();
        return this;
    }
}
