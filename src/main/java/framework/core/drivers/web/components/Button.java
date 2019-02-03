package framework.core.drivers.web.components;

import framework.core.drivers.utils.ElementLocatorWrapper;
import framework.core.drivers.web.WebPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Button extends BaseElement<Button> {

	private static final Logger logger = LogManager.getLogger(Button.class);

	public Button(ElementLocatorWrapper wrapper, WebPage page) {
		super(wrapper, page);
	}

	public Button click() {
		getElement().click();
		logger.info("Clicked on the button ");
		return this;
	}
}
