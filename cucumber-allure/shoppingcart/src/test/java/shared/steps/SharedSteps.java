package shared.steps;

import static org.junit.Assert.assertTrue;

import cucumber.api.java.en.Given;
import shared.pages.BasePage;

public class SharedSteps{
	
	BasePage basepage=new BasePage();

	@Given("I am on homepage")
	public void i_am_on_homepage() {
	assertTrue(basepage.getPageTitle().contains("Williams Sonoma"));
	}
}