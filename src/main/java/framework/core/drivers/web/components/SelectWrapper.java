package framework.core.drivers.web.components;

import framework.core.drivers.utils.ElementLocatorWrapper;
import framework.core.drivers.web.WebPage;
import org.openqa.selenium.support.ui.Select;

public class SelectWrapper extends BaseElement<SelectWrapper> {

	public SelectWrapper(ElementLocatorWrapper wrapper, WebPage page) {
		super(wrapper, page);
	}

	public Select get() {
		return new Select(getElement());
	}
}
