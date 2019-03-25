package shared.steps;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import shared.pages.BasePage;
import shared.pages.LeftSideMenu;

public class LeftSideMenuNavigationStep  {
	
	LeftSideMenu leftSideMenu=BasePage.loadPageObjects(LeftSideMenu.class);

	@Then("I see Tea Kettle option on left side menu")
	public void i_see_the_sub_menu_options() {
		assertTrue("Link for Tea kettles is not displayed",leftSideMenu.isDisplayedTeaKettleLink());
		assertEquals("Tea Kettles", leftSideMenu.labelTeaKetlelink());
	}
	
	@When("I click on Tea Kettle option")
	public void i_click_on_Tea_Kettle_option() {
		leftSideMenu.selectTeaKettle();
	}
}
