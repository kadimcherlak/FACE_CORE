package framework.core.drivers.utils;

import com.google.common.base.Optional;
import framework.core.exceptions.FrameworkException;
import framework.core.utils.ObjectCreatorUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PageFactory {

	private final WebDriver driver;
	private WebElement contextElement;

	/**
	 * Initialize web driver.
	 *
	 * @param driver
	 */
	public PageFactory(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Initialize web driver.
	 *
	 * @param driver
	 */
	public PageFactory(WebDriver driver, WebElement contextElement) {
		this.driver = driver;
		this.contextElement = contextElement;
	}

	/**
	 * Returns a instance of the given page class initialized with relevant
	 * parameters.
	 *
	 * @param pageClass
	 * @return
	 */
	public <T> T get(Class<T> pageClass, Object... parameters) {
		if (parameters == null) {
			throw new IllegalArgumentException("Null parameters are provided to the page class");
		}
		int size = contextElement != null ? parameters.length + 2 : parameters.length + 1;
		Object[] params = new Object[size];
		params[0] = driver;
		int startIndex = 1;
		if (contextElement != null) {
			params[1] = contextElement;
			startIndex = 2;
		}
		System.arraycopy(parameters, 0, params, startIndex, parameters.length);
		Optional<T> pageObj = ObjectCreatorUtil.selectConstructorAndInvoke(pageClass, params);
		if (!pageObj.isPresent()) {
			throw new FrameworkException(
				"Expected page object is not found with required parameters in page class " + pageClass);
		}
		return pageObj.get();
	}

}
