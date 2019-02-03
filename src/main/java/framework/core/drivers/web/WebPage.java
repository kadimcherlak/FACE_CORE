package framework.core.drivers.web;

import framework.core.drivers.Page;
import framework.core.models.Context;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WebPage<T> extends Page {

	public WebDriver driver;
	public Context context;

	int longTimeOut = 10000;
	int normalTimeOut = 5000;
	int shortTimeOut = 2000;

	public WebPage(Context context) {
		super(context);
		this.context = context;
		this.driver = context.getDriver();
	}

	public String getPageTitle() {
		return driver.getTitle();
	}

	public void waitForLoad() {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver)
					.executeScript("return document.readyState")
					.equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, normalTimeOut);
		wait.until(pageLoadCondition);
	}

	public <T> T waitFor(ExpectedCondition<T> waitCondition, long timeout) {
		return new WebDriverWait(driver, timeout).until(waitCondition);
	}

	public <T> T waitFor(ExpectedCondition<T> waitCondition) {
		return new WebDriverWait(driver, normalTimeOut)
			.ignoring(StaleElementReferenceException.class)
			.until(waitCondition);
	}

	public void waitLongTime() {
		try {
			Thread.sleep(new Long(longTimeOut));
		} catch (Exception e) {
			logger.error("Exception:wait = problem encountered while attempting to sleep the thread - {}",
				e.getMessage());
			e.printStackTrace();
		}
	}

	public void waitNormalTime() {
		try {
			Thread.sleep(new Long(normalTimeOut));
		} catch (Exception e) {
			logger.error("Exception:wait = problem encountered while attempting to sleep the thread - {}",
				e.getMessage());
			e.printStackTrace();
		}
	}

	public void waitShortTime() {
		try {
			Thread.sleep(new Long(shortTimeOut));
		} catch (Exception e) {
			logger.error("Exception:wait = problem encountered while attempting to sleep the thread - {}",
				e.getMessage());
			e.printStackTrace();
		}
	}

	public void clickWebElement(WebElement element) throws Exception {
		try {
			isElementClickable(element);
			element.click();
		} catch (Exception e) {
			logger.error("Exception:clickWebElement =" + e.getMessage());
			throw new Exception(e);
		}
	}

	public void mouseHover(WebElement element) throws Exception {
		hardWaitInMilliseconds(1000);
		isElementClickable(element);
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
	}

	public void sendKeysToWebElementTextArea(WebElement element, String text) throws Exception {
		try {
			sendKeysToWebElement(element, text);
			element.sendKeys(Keys.ENTER);
		} catch (Exception e) {
			logger.error("Exception:sendKeysToWebElementTextArea =" + e.getMessage());
			throw new Exception(e);
		}
	}

	public void sendKeysToWebElement(WebElement element, String text) throws Exception {
		try {
			element.clear();
			isElementVisible(element);
			element.sendKeys(text);
		} catch (Exception e) {
			logger.error("Exception:sendKeysToWebElement =" + e);
			throw new Exception(e);
		}
	}

	public void sendKeysToWebElementWithArrowDown(WebElement element, String text) throws Exception {
		try {
			sendKeysToWebElement(element, text);
			this.hardWaitInMilliseconds(4000);
			element.sendKeys(Keys.ARROW_LEFT);
			this.hardWaitInMilliseconds(1000);
			element.sendKeys(Keys.ARROW_DOWN);
			element.sendKeys(Keys.ENTER);
		} catch (Exception e) {
			logger.error("Exception =" + e.getMessage());
			throw new Exception(e);
		}
	}

	public void selectElementFromDivOptions(WebElement divOptionElement, String selectText, boolean partialTextSearch)
		throws Exception {
		// Enter the text
		String findSearchText = "";
		String[] wordsInText = selectText.split(" ", -1);
		if (partialTextSearch) {
			for (int idx = 0; idx < wordsInText.length; idx++) {
				if (wordsInText[idx].trim().length() > findSearchText.length())
					findSearchText = wordsInText[idx].trim();
			}
		} else
			findSearchText = selectText.trim();
		// sendKeysToWebElementTextArea(driver,
		// divOptionElement,findSearchText);// "Staffing");
		sendKeysToWebElement(divOptionElement, findSearchText);// "Staffing");
		hardWaitInMilliseconds(3000);
		String selectTextWithoutSpacefromTestData = selectText.replaceAll(" ", "");
		By selectOptionBy = By.cssSelector("div[class*=\"portal-\"][id*=\"auto\"]");
		List<WebElement> selectOptionWebElement = driver.findElements(selectOptionBy);
		int i = 0;
		boolean elementFound = false;
		while (i < selectOptionWebElement.size() && !elementFound) {
			divOptionElement.sendKeys(Keys.ARROW_DOWN);
			By selectedOptionBy = By.cssSelector("div[class=\"portal-selected\"][id*=auto]");
			List<WebElement> selectedOptionWebElement = driver.findElements(selectedOptionBy);
			int j = 0;
			while (j < selectedOptionWebElement.size()) {
				String textWithOutSpaceFromOptions = selectedOptionWebElement.get(j).getAttribute("innerHTML")
					.replaceAll("&amp;", "&").replaceAll(" ", "");
				if (selectTextWithoutSpacefromTestData.equalsIgnoreCase(textWithOutSpaceFromOptions)) {
					logger.debug("Match found");
					divOptionElement.sendKeys(Keys.ENTER);
					elementFound = true;
					break;
				}
				j++;
			}
			i++;
		}
	}

	private void isElementClickable(WebElement element) throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, longTimeOut);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element));
			// get Screenshot
		} catch (Exception e) {
			logger.error("Exception:isElementClickable =" + e.getMessage());
			throw new Exception(e);
		}
	}

	public void isElementClickable(By element) throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, longTimeOut);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element));
			// get Screenshot
		} catch (Exception e) {
			logger.error("Exception:isElementClickable(By) =" + e.getMessage());
			throw new Exception(e);
		}
	}

	public void isElementVisible(WebElement element) throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, longTimeOut);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			// get Screenshot
		} catch (Exception e) {
			logger.error("Exception:isElementVisible =" + e.getMessage());
			throw new Exception(e);
		}
	}

	public void isElementVisible(By element) throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, longTimeOut);
		try {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(element));
			// get Screenshot
		} catch (Exception e) {
			logger.error("Exception:isElementVisible =" + e.getMessage());
			throw new Exception(e);
		}
	}

	public void isElementInVisible(WebElement element) throws Exception {

		if (!isElementDisplayed(element)) // If element is not visible, no need to wait till its invisibility
			return;
		WebDriverWait wait = new WebDriverWait(driver, normalTimeOut);
		try {

			wait.until(ExpectedConditions.invisibilityOf(element));
			// get Screenshot
		} catch (Exception e) {
			logger.error("Exception:isElementInvisible =" + e);
			throw new Exception(e);
		}
	}

	public void isElementInVisibleCareerSite(WebElement element) throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, longTimeOut);
		try {
			wait.until(ExpectedConditions.invisibilityOf(element));
			// get Screenshot
		} catch (Exception e) {
			logger.error("Exception:isElementInvisible =" + e);
			throw new Exception(e);
		}
	}

	public void isElementPresent(By element) throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, shortTimeOut);

		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(element));
			// get Screenshot
		} catch (Exception e) {
			logger.error("Exception:isElementPresent =" + e);
			throw new Exception(e);
		}
	}

	public void isElementPresent(By element, int timeout) throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, timeout);// .LARGE_TIMEOUT);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(element));
			// get Screenshot
		} catch (Exception e) {
			logger.error("Exception:isElementPresent =" + e);
			throw new Exception(e);
		}
	}

	public void selectValueFromDropDownWithContainsText(WebElement element, String text)
		throws Exception {
		try {
			isElementVisible(element);
			Select dropDown = new Select(element);
			int index = 0;
			for (WebElement option : dropDown.getOptions()) {
				if (option.getText().replaceAll(" ", "").indexOf(text.replaceAll(" ", "")) != -1)
					break;
				index++;
			}
			dropDown.selectByIndex(index);

		} catch (Exception e) {
			logger.error("Exception:selectValueFromDropdown =" + e);
			throw new Exception(e);
		}
	}

	public void selectValueFromDropDown(WebElement element, String text) throws Exception {
		try {
			isElementVisible(element);
			Select dropDown = new Select(element);
			int index = 0;
			for (WebElement option : dropDown.getOptions()) {
				if (option.getText().replaceAll(" ", "").equalsIgnoreCase(text.replaceAll(" ", "")))
					break;
				index++;
			}
			dropDown.selectByIndex(index);

		} catch (Exception e) {
			logger.error("Exception:selectValueFromDropdown =" + e);
			throw new Exception(e);
		}
	}

	public String getSelectedOption(WebElement element) throws Exception {
		String selectText = null;
		try {
			isElementVisible(element);
			Select dropDown = new Select(element);
			WebElement option = dropDown.getFirstSelectedOption();
			selectText = option.getText();
		} catch (Exception e) {
			logger.error("Exception:selectValueFromDropdown =" + e);
			throw new Exception(e);
		}
		return (selectText != null && selectText.trim().length() > 0) ? selectText.trim() : "";
	}

	public void switchToNextWindowHandle(String parentHandle, WebDriver driver) throws Exception {
		Set<String> windowHandles = null;// Some browsers take time to recognize new windows, very imp.
		do {
			this.hardWaitInMilliseconds(1000);
			windowHandles = driver.getWindowHandles();
		} while (windowHandles.size() < 2);
		Iterator<String> i1 = windowHandles.iterator();

		while (i1.hasNext()) {

			String childWindow = i1.next();

			if (!parentHandle.equalsIgnoreCase(childWindow)) {
				driver.switchTo().window(childWindow);
			}
		}

	}

	public String getTextFromWebElement(WebElement element) throws Exception {
		String text = null;
		try {
			isElementVisible(element);
			text = element.getText();
		} catch (Exception e) {
			logger.error("Exception:getTextFromWebElement =" + e.getMessage());
			throw new Exception(e);
		}
		return text;
	}

	public void clickOnAlertToAccept(WebDriver driver) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			alert.accept();
			try {
				alert.accept();
			} catch (Exception e) {
			}
		} catch (Exception e) {
			logger.error("Exception:clickOnAlertToAccept =" + e.getMessage());
			throw new Exception(e);
		}

	}

	public void clickOnAlertToDismiss(WebDriver driver) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, normalTimeOut);
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			alert.dismiss();
		} catch (Exception e) {
			logger.error("Exception:clickOnAlertToDismiss =" + e.getMessage());
			throw new Exception(e);
		}
	}

	public void scrollToPageTop(WebDriver driver) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollTo(0,0)", "");
	}

	public void scrollToElement(WebElement element) {
		// switchToNextWindowHandle(driver.getWindowHandle(),driver);
		// switchToNextWindowHandle(driver.getWindowHandle(),driver);
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void calendar(WebElement element, String text, String data) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("document.getElementsByName('" + data + "')[0].setAttribute('type', 'text');");
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.click();
		actions.sendKeys(text);
		actions.build().perform();
	}

	public void hardWaitInMilliseconds(int ms) throws InterruptedException {
		Thread.sleep(ms);
	}

	public String getTextFromDropdownSelection(WebElement element) {
		Select select = new Select(element);
		WebElement webElementSelected = select.getFirstSelectedOption();
		String strSelectedText = webElementSelected.getText();
		return strSelectedText;
	}

	public void deselectAllItemsInDropdown(String selectXpath) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, normalTimeOut);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(selectXpath)));

		WebElement selectElement = driver.findElement(By.xpath(selectXpath));
		this.isElementVisible(selectElement);
		Select select = new Select(selectElement);
		select.deselectAll();
		hardWaitInMilliseconds(2000);
	}

	public boolean isValueAttributePresent(WebElement element) {
		boolean result = false;
		try {
			String value = element.getAttribute("value");
			if (value.equalsIgnoreCase(null) || value.equalsIgnoreCase("") || value.equalsIgnoreCase(" ")) {
				// result = true;
			} else {
				result = true;
			}
		} catch (Exception e) {

		}
		return result;
	}

	public boolean isDisabledAttributePresent(WebElement element) {
		boolean result = false;
		try {
			String value = element.getAttribute("disabled");
			if (value.equalsIgnoreCase(null) || value.equalsIgnoreCase("") || value.equalsIgnoreCase(" ")) {

			} else {
				result = true;
			}
		} catch (Exception e) {

		}
		return result;
	}

	public void doubleClick(WebElement element) throws Exception {
		try {
			isElementClickable(element);
			Actions action = new Actions(driver).doubleClick(element);
			action.build().perform();
		} catch (Exception e) {
			System.out.println("Element " + element + " was not clickable " + e.getMessage());
			throw new Exception(e);
		}
	}

	public boolean isElementDisplayed(WebElement element) {
		try {
			return element.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public void refreshPage(WebDriver driver) {
		driver.navigate().to(driver.getCurrentUrl());
	}

	public void javascriptClickWebElement(WebElement element) throws Exception {
		try {
			isElementVisible(element);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		} catch (Exception e) {
			logger.error("Exception:javaScriptClickWebElement =" + e.getMessage());
			throw new Exception(e);
		}

	}

	public void zoomInZoomOut(WebDriver driver, String zoomLevel) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("document.body.style.zoom = '" + zoomLevel + "%';");
	}

	public String getWindowHandle(WebDriver driver) {
		return driver.getWindowHandle();
	}

	public void switchToWindowHandle(String WindowHandle) {
		driver.switchTo().window(WindowHandle);
	}

}
