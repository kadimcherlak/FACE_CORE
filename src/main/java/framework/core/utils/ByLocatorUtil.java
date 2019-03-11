/*===============================================================================================================================
        CLASS Name:    ByLocatorUtil
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   ByLocator utility class to get the By value from the locators
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.utils;

import framework.core.exceptions.FrameworkException;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;

/**
 * This class uses reflection to get the By value from the locators.
 *
 * @author raghavendran.r1@cognizant.com
 */
public class ByLocatorUtil {

    /**
     * Returns the By locator.
     *
     * @param locator
     * @return
     */
    public static By getByLocator(ElementLocator locator) {
        try {
            // a brittle hack that depends on field name, need to do as appium does not expose the annotation class
            Field byField = locator.getClass().getDeclaredField("by");

            byField.setAccessible(true);
            return (By) byField.get(locator);
        } catch (Exception excp) {
            throw new FrameworkException("Error while gettign the By locator from the element locator");
        }
    }
}
