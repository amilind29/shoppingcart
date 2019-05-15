package shoppingcart.steps;

import static org.junit.Assert.assertTrue;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import shared.pages.BasePage;
import shared.steps.BaseStep;
import shoppingcart.pages.CartSummaryPopp;

public class CartSummaryPopupStep {

	CartSummaryPopp cartSummaryPopp = BasePage.loadPageObjects(CartSummaryPopp.class);

	@Then("I should see cart summary pop-up")
	public void i_should_see_cart_summary_pop_up() {
		assertTrue("cart summary popup is not displaed",cartSummaryPopp.isDisplayedCartpopUp());
	}

	@When("I click on checkout")
	public void i_click_on_checkout() {
		cartSummaryPopp.clickcheckoutButton();
	}
}
