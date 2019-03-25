package shared.steps;

import cucumber.api.java.en.When;
import shared.pages.BasePage;
import shared.pages.TopMenu;

public class TopMenuNavigationSteps {
	
	TopMenu topMenu=BasePage.loadPageObjects(TopMenu.class);

	@When("I click on cookware menu")
	public void i_navigate_to_cook_ware_menu() {
		topMenu.selectCookwareFromTopMenu();
	}
}
