package shared.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import shared.utils.DriverManager;

public class BasePage {
	protected WebDriver driver = DriverManager.getDriverManager().getDriver();
	protected Actions action=new Actions(driver);

	@FindBy(tagName = "body")
	private WebElement body;

	public static <T extends BasePage> T loadPageObjects(Class<T> type) {
		return PageFactory.initElements(DriverManager.getDriverManager().getDriver(), type);
	}

	public String getPageTitle() {
		return driver.getTitle();
	}

	public boolean verifyPage(String expectedPage) {
		if (!body.getAttribute("id").toLowerCase().contains(expectedPage))
			return false;
		return true;
	}
	
	public WebElement waitTillVisible(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		return wait.until(
		        ExpectedConditions.visibilityOf(element));
	}
}
