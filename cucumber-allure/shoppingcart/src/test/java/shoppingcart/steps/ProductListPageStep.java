package shoppingcart.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import shared.pages.BasePage;
import shoppingcart.pages.ProductListPage;

public class ProductListPageStep {
	ProductListPage productListPage = BasePage.loadPageObjects(ProductListPage.class);

	@Then("I should be navigated to tea kettles page")
	public void i_see_the_tea_kettles_page_opened() {
		assertTrue(productListPage.getPageTitle().contains("Tea Kettles"));
		assertEquals("Tea Kettles", productListPage.getSelectedCatagory());
	}

	@When("I select any tea kettle")
	public void i_select_first_tea_kettle() {
		productListPage.selectRandomProduct();
	}
}
