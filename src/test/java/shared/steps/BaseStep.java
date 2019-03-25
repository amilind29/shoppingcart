package shared.steps;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.BeforeStep;
import cucumber.api.java.en.Given;
import io.qameta.allure.Allure;
import shared.utils.Config;
import shared.utils.DriverManager;

public class BaseStep {

	protected WebDriver driver;
	protected Config config;

	 @Before
	public void initialize() {
		driver = DriverManager.getDriverManager().getDriver();
		config = Config.getConfig();
	}

	 @BeforeStep
	public void AfterStep() {
		while (!waitForJStoLoad()) {
			System.out.println("waiting");
		}	
		 /*final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		 InputStream is=new ByteArrayInputStream(screenshot);
		 Allure.addAttachment("Attchement", is);*/
	}

	 @After
	public void tearDown(Scenario scenario) {
		 if(scenario.isFailed()) {
			 final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				InputStream is=new ByteArrayInputStream(screenshot);
			 Allure.addAttachment("Failed Step",is);
		 }
		
		driver.close();
		driver.quit();
	}

	@Given("I am on google")
	public void i_am_on_google() {
		driver.get("https://www.google.com/");
		System.out.println("pass");
//		assertTrue(false);
	}

	public boolean waitForJStoLoad() {
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					JavascriptExecutor jse = (JavascriptExecutor) driver;

					return ((Long) jse.executeScript("return jQuery.active") == 0);
				} catch (Exception e) {
					return true;
				}
			}
		};

		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				return jse.executeScript("return document.readyState").toString().equals("complete");
			}
		};

		return wait.until(jQueryLoad) && wait.until(jsLoad);
	}

}
