/*===============================================================================================================================
        CLASS Name:    ListPageElementsHandler
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   List Page Elements Handler
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.drivers.utils;

import framework.core.drivers.web.WebPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ListPageElementsHandler implements InvocationHandler {

    private static final Logger logger = LogManager.getLogger(ListPageElementsHandler.class);

    private final List<Object> webElements = new ArrayList<Object>();
    private final ElementLocator locator;
    private final Field field;
    private final WebPage page;
    private final Class<?> pageType;
    private boolean refresh = true;

    public ListPageElementsHandler(
            ElementLocator locator,
            Field field,
            WebPage page,
            Class<?> pageType) {
        this.locator = locator;
        this.field = field;
        this.page = page;
        this.pageType = pageType;
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            if (method.getDeclaringClass() == Map.class) {
                if (refresh) {
                    List<WebElement> locatedElements = locator.findElements();
                    if (locatedElements.isEmpty()) {
                        logger.warn("Warning:No elements were found for the locator in the field {} : {}"
                                + field.getName()
                                + "Please try refresh if elements are expected or verify the locator is correct.");
                    }
                    createList(locatedElements);
                    refresh = false;
                }
            }
            return method.invoke(webElements, args);
        } catch (Exception e) {
            logger.error("Exception:invoke = Error while lookup of field {}", e.getMessage());
            throw new NoSuchElementException(
                    "Exception:invoke = Error while locating the element in field "
                            + field.getName(), e);
        }
    }

    protected void createList(List<WebElement> locatedElements) {
        for (int count = 0; count < locatedElements.size(); count++) {
            ElementLocatorWrapper wrapper = new ElementLocatorWrapper(locatedElements, count);
            webElements.add(ElementFactory.createInstanceOfType(field, wrapper, page, pageType));
        }
    }
}