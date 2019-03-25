package shared.utils;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class Config implements Cloneable {
	private static Config config;
	private static final String DEFAULT_PROPERTIES = "default.properties";
	private static final String PROPERTY_ROOT = "shoppingcart/properties/";
	private Properties proerties;

	private Config() {

	}

	public static Config getConfig() {
		if (config == null)
			config = new Config();
		return config;
	}

	private void loadConfig() {
		proerties=new Properties();
		String configPath = Optional.ofNullable(System.getenv("EXECUTION_ENV_PROPERTIES")).orElse(DEFAULT_PROPERTIES);
		InputStream input = Config.class.getClassLoader().getResourceAsStream(PROPERTY_ROOT + configPath);
		if (input != null) {
			try {
				proerties.load(input);
			} catch (IOException e) {
				assertTrue("error in config file loading" + configPath + "-->" + e.getMessage(), false);
			}
		}
	}

	public String getProperty(String key) {
		if (proerties == null)
			loadConfig();
		return proerties.getProperty(key);
	}
}
