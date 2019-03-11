/*===============================================================================================================================
        CLASS Name:    HighLight
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   HighLight utility class to enable HighLight
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HighLight {

    private static WebDriver driver;

    public HighLight(WebDriver driver){
        this.driver = driver;
    }

    public static void highLightElement(WebElement element){

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');",element);

        try{
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        js.executeScript("arguments[0].setAttribute('style','border: 2px solid white');",element);
    }
}
