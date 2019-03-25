package shoppingcart.pages;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import shared.pages.BasePage;
import shared.utils.Utils;

public class ProductListPage extends BasePage {
	
	@FindBy(css="#breadcrumb-list li:nth-last-child(1)>*")
	private WebElement catagorySelected;
	
	@FindBy(css="li.product-cell")
	private List<WebElement> productList;
	
	public String getSelectedCatagory() {
		return catagorySelected.getText();
	}
	
	public void selectRandomProduct() {
		productList.get(Utils.getRandomNumber(productList.size())).click();
	}
	
}
