package shared.steps;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import shared.utils.Config;

public class Guest {
	public static void main(String[] args) {

		Config config =Config.getConfig();
		System.setProperty("webdriver.chrome.driver", config.getProperty("test.chrome.driver.win"));
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.williams-sonoma.com");
	}
	
	private static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}

	private ChromeOptions getChromeOptions() {
		if (isWindows()) {
			ChromeOptions options = new ChromeOptions();
			//options.addArguments("start-maximized");
			return options;
		} else {
			assertTrue("please add the required configuration in 'DriverManager' for " + System.getProperty("os.name"),
					false);
		}
		return null;
	}

}
