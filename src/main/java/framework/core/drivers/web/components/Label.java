package framework.core.drivers.web.components;

import com.google.common.base.Optional;
import framework.core.drivers.utils.ElementLocatorWrapper;
import framework.core.drivers.web.WebPage;

public class Label extends BaseElement<Label> {

	public Label(ElementLocatorWrapper elementLocatorWrapper, WebPage page) {
		super(elementLocatorWrapper, page);
	}

	public String content() {
		return getElement().getText();
	}

	public Optional<String> optionalContent() {
		String content = null;
		if (isDisplayed()) {
			content = getElement().getText();
		}
		return Optional.fromNullable(content);
	}
}