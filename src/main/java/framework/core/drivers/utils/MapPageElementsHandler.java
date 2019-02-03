package framework.core.drivers.utils;

import framework.core.annotations.GroupBy;
import framework.core.drivers.web.WebPage;
import framework.core.exceptions.FrameworkException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapPageElementsHandler implements InvocationHandler {

	private static final Logger logger = LogManager.getLogger(ListPageElementsHandler.class);

	private final Map<String, Object> webElements = new HashMap<String, Object>();
	private final ElementLocator locator;
	private final Field field;
	private final String propertyName;
	private final WebPage page;
	private final Class<?> pageType;
	private final boolean compositeKey;
	private boolean refresh = true;

	public MapPageElementsHandler(
		ElementLocator locator,
		Field field,
		WebPage page,
		Class<?> pageType) {
		this.locator = locator;
		this.field = field;
		this.page = page;
		this.pageType = pageType;

		GroupBy annotation = field.getAnnotation(GroupBy.class);
		GroupBy groupBy = annotation;
		if (groupBy == null) {
			logger.error("Error:MapPageElementHandler = {} : {}",
				field.getName(),
				"groupBy is null. Cannot create a map without GroupBy annotation for field.");
			throw new FrameworkException(
				"Exception:MapPageElementHandler = "
					+ field.getName()
					+ ": groupBy is null. Cannot create a map without GroupBy annotation for field.");
		}
		propertyName = annotation.value();
		compositeKey = annotation.compositeKey();
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
					createMap(locatedElements);
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

	protected void createMap(List<WebElement> locatedElements)
		throws NoSuchMethodException,
		IllegalAccessException,
		InvocationTargetException {

		int counter = 0;
		for (WebElement element : locatedElements) {
			ElementLocatorWrapper wrapper = new ElementLocatorWrapper(locatedElements, counter);
			Object mapIt = ElementFactory.createInstanceOfType(field, wrapper, page, pageType);
			String keyName = null;
			if (!compositeKey) {
				keyName = fromWebElement(element);
			} else {
				keyName = fromObjectGetter(mapIt);
			}
			webElements.put(keyName, mapIt);
			counter++;
		}
	}

	private String fromWebElement(WebElement element)
		throws NoSuchMethodException,
		IllegalAccessException,
		InvocationTargetException {
		String keyName = element.getAttribute(propertyName);
		if (keyName == null) {
			keyName = fromObjectGetter(element);
		}
		return keyName;
	}

	private String fromObjectGetter(Object element)
		throws NoSuchMethodException,
		IllegalAccessException,
		InvocationTargetException {
		String apiLookup = new StringBuilder("get").append(
			StringUtils.capitalize(propertyName)).toString();
		Method getApi = element.getClass().getDeclaredMethod(apiLookup);
		getApi.setAccessible(true);
		return (String) getApi.invoke(element);
	}
}
