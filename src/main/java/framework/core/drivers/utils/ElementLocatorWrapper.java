package framework.core.drivers.utils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.util.List;

/**
 * A lighweight wrapper around element locator that provides a mapping between a element locator and the web element.
 * Each web element will a single wrapper locator. The locator wrapper handles the logic of using the list locator or
 * single element locator when looking up elements.
 *
 * @author smahalingam@prokarma.com
 * @verion 1.0
 */
public class ElementLocatorWrapper {

	private final ElementLocator elementLocator;
	private final List<WebElement> locatedElements;
	private final boolean isListMode;
	private int listIndex;

	/**
	 * Initializw with element locator.
	 *
	 * @param elementLocator
	 */
	public ElementLocatorWrapper(ElementLocator elementLocator) {
		this.elementLocator = elementLocator;
		isListMode = false;
		this.locatedElements = null;
	}

	/**
	 * Initializw with element locator.
	 *
	 * @param listIndex
	 */
	public ElementLocatorWrapper(List<WebElement> list, int listIndex) {
		this.elementLocator = null;
		isListMode = true;
		this.locatedElements = list;
		this.listIndex = listIndex;
	}

	/**
	 * Finds the web element using the locator.
	 *
	 * @return found web element.
	 */
	public WebElement findElement() {
		WebElement element = null;
		if (isListMode) {
			element = locatedElements.get(listIndex);
		} else {
			element = elementLocator.findElement();
		}
		return element;
	}
}
