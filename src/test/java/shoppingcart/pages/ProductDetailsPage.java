package shoppingcart.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import shared.pages.BasePage;

public class ProductDetailsPage extends BasePage {
	
	@FindBy (css="button[id*='addToCart']")
	private WebElement addToCartButton;
	
	public void clickOnaddToCart() {
		addToCartButton.click();
	}
	
}
