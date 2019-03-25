package shoppingcart.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import shared.pages.BasePage;

public class CartSummaryPopp extends BasePage{
	
	@FindBy(css="div#racOverlay")
	private WebElement popup;
	
	@FindBy(css="div#racOverlay [title='Checkout']")
	private WebElement checkoutButton;
	
	public boolean isDisplayedCartpopUp() {
		return popup.isDisplayed();
	}
	
	public void clickcheckoutButton() {
		checkoutButton.click();
	}
}
