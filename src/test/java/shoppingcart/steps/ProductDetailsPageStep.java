package shoppingcart.steps;


import static org.junit.Assert.assertTrue;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import shared.pages.BasePage;
import shoppingcart.pages.ProductDetailsPage;

public class ProductDetailsPageStep  {
	
	ProductDetailsPage productDetailsPage=BasePage.loadPageObjects(ProductDetailsPage.class);

	@Then("I should see product details page")
	public void i_should_see_tea_kettle_details_page() {
		assertTrue("ProductDetails page is not displayed", productDetailsPage.verifyPage("product"));
	}

	@When("I click on add to cart")
	public void i_click_on_add_to_cart() {
		productDetailsPage.clickOnaddToCart();
	}

}
