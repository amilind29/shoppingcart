package shared.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LeftSideMenu extends BasePage {

	@FindBy(css = ".category-0 li>a[href*='teakettles']")
	private WebElement teaKettleSubMenu;

	public void selectTeaKettle() {
		
		action.moveToElement(waitTillVisible(teaKettleSubMenu)).build().perform();
		action.click().build().perform();
		//teaKettleSubMenu.click();
	}

	public boolean isDisplayedTeaKettleLink() {
		
		return waitTillVisible(teaKettleSubMenu).isDisplayed();
	}
	
	public String labelTeaKetlelink() {
		return teaKettleSubMenu.getText();
	}

}