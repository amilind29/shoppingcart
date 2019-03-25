package shared.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TopMenu extends BasePage{

	@FindBy(css="a.topnav-cookware")
	private WebElement cookwareMenu;
	
	public void selectCookwareFromTopMenu() {
		action.moveToElement(waitTillVisible(cookwareMenu)).build().perform();
	}
}
