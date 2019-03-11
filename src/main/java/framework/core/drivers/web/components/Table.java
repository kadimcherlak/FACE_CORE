/*===============================================================================================================================
        CLASS Name:    Table
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Table for Web framework
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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class Table extends BaseElement<Table> {

    private static final Logger logger = LogManager.getLogger(Table.class);

    public Table(ElementLocatorWrapper wrapper, WebPage page) {
        super(wrapper, page);
    }

    public int getRowSize() {
        return getElement().findElements(By.tagName("tr")).size();
    }

    public int getColumnSize() {
        return getElement().findElements(By.tagName("td")).size();
    }

    public String getTableCellValue(int rowNum, int colNum) {
        List<WebElement> rows = getElement().findElements(By.tagName("tr"));
        return rows.get(rowNum - 1).findElements(By.tagName("td")).get(colNum - 1).getText();
    }

}
