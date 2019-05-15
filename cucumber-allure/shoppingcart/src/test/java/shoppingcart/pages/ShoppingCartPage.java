package shoppingcart.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import shared.pages.BasePage;

public class ShoppingCartPage extends BasePage {
	
	@FindBy(css="a.save-for-later-link")
	private WebElement linkSaveForLater;
	
	@FindBy(css=".save-for-later-message")
	private WebElement saveForLaterMessage;
	
	public void clickSaveForLater() {
		linkSaveForLater.click();
	}
	
	public String getSaveForLaterMessage() {
		return saveForLaterMessage.getText();
	}
}
