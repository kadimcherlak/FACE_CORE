package framework.core.drivers.web.components;

import framework.core.drivers.utils.ElementLocatorWrapper;
import framework.core.drivers.web.WebPage;

public class TextBox extends BaseElement<TextBox> {

	public TextBox(ElementLocatorWrapper wrapper, WebPage page) {
		super(wrapper, page);
	}

	public TextBox enterText(CharSequence textBox) {
		clear();
		getElement().sendKeys(textBox);
		return this;
	}

	public TextBox clear() {
		getElement().clear();
		return this;
	}
}
