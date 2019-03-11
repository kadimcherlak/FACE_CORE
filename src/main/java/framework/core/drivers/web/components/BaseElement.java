/*===============================================================================================================================
        CLASS Name:    BaseElement
        CREATED BY:    Raghavendran Ramasubramanian (Raghavendran.R1@cognizant.com)
        DATE CREATED:  Nov 2018
        DESCRIPTION:   Base Element methods for Web framework
        PARAMETERS:
        RETURNS:
        COMMENTS:
        Modification Log:
        Date                             Initials                                                Modification
-------------------------------------------------------------------------------------------------------------------------------------------------------*/

package framework.core.drivers.web.components;

import framework.core.drivers.utils.ElementLocatorWrapper;
import framework.core.drivers.web.WebPage;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.lang.reflect.Method;

public class BaseElement<T> {

    private static final Logger logger = LogManager.getLogger(BaseElement.class);
    private final ElementLocatorWrapper wrapper;
    private final WebPage page;
    private WebElement element;

    public BaseElement(ElementLocatorWrapper elementLocatorWrapper, WebPage page) {
        this.wrapper = elementLocatorWrapper;
        this.page = page;
        this.element = createElement();
    }

    private WebElement createElement() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(WebElement.class);
        enhancer.setCallback(new WebElementHandler());
        return (WebElement) enhancer.create();
    }

    public WebElement getElement() {
        return element;
    }

    @SuppressWarnings("unchecked")
    public T waitUntilVisible() {
        if (!isDisplayed()) {
            page.waitFor(ExpectedConditions.visibilityOf(getElement()));
        }
        return (T) this;
    }

    public boolean isDisplayed() {
        boolean isDisplayed = false;
        try {
            isDisplayed = getElement().isDisplayed();
        } catch (Exception e) {
            logger.warn("Exception:isDisplayed = {}", e.getMessage());
        }
        return isDisplayed;
    }

    @SuppressWarnings("unchecked")
    public T click() {
        getElement().click();
        return (T) this;
    }

    protected void initializeSubElements() {
        PageFactory.initElements(page.getContext().getDriver(), this);
    }

    private final class WebElementHandler implements MethodInterceptor {

        public Object intercept(
                Object instance,
                Method calledMethod,
                Object[] args,
                MethodProxy proxy) throws Throwable {
            WebElement baseElement = BaseElement.this.wrapper
                    .findElement();
            return calledMethod.invoke(baseElement, args);

        }
    }
}