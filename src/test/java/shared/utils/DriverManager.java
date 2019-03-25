package shared.utils;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class DriverManager {

	private static DriverManager driverManager;
	private WebDriver driver;
	private Config config = Config.getConfig();

	private DriverManager() {

	}

	public static DriverManager getDriverManager() {
		if (driverManager == null)
			driverManager = new DriverManager();
		return driverManager;
	}

	public WebDriver getDriver() {
		if (driver == null)
			initializeDriver(config.getProperty("test.browser"));
		return driver;
	}

	public void initializeDriver(String browser) {
		switch (browser) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", config.getProperty("test.chrome.driver.win"));
			driver = new ChromeDriver();
			break;
		case "ie":
			System.setProperty("webdriver.ie.driver",config.getProperty("test.ie.driver.win"));
			driver = new InternetExplorerDriver();
			break;
		case "firefox":
			System.setProperty("webdriver.gecko.driver",config.getProperty("test.firefox.driver.win"));
			driver = new FirefoxDriver();
			break;
		default:
			assertTrue("please add the required configuration in 'DriverManager' for " + browser, false);
		}

		driver.get((String)config.getProperty("test.url").trim());
		driver.manage().timeouts().implicitlyWait(30000, TimeUnit.MILLISECONDS);
		driver.manage().timeouts().pageLoadTimeout(30000, TimeUnit.MILLISECONDS);
		driver.manage().window().fullscreen();
	}

	

}
