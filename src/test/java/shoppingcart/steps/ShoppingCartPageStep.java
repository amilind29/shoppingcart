package shoppingcart.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import shared.pages.BasePage;
import shoppingcart.pages.ShoppingCartPage;

public class ShoppingCartPageStep {
	ShoppingCartPage shoppingCartPage = BasePage.loadPageObjects(ShoppingCartPage.class);

	@Then("I should see shoppingcart page")
	public void i_should_see_shoppingcart_page() {
		assertTrue("After clicking on checkout, shopping cart is not dispayed",
				shoppingCartPage.getPageTitle().contains("Shopping Cart"));
	}

	@When("I click on save for later")
	public void i_click_on_save_for_later() {
		shoppingCartPage.clickSaveForLater();
	}

	@Then("I should see the message for saved items")
	public void i_should_see_the_message_for_saved_items() {
		assertEquals("Saved for latter message is not displayed correctly","You have 1 saved item. You can select \"Move to Shopping Cart\" to purchase an item.",shoppingCartPage.getSaveForLaterMessage());
	}
}
