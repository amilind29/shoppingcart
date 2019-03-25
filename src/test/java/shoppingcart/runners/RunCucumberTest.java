package shoppingcart.runners;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(	plugin = { 
								"pretty",
                            }, 
					features = { 
							"src/test/resources/shoppingcart/features/ShoppingCart.feature" 
							}, 
					glue= {
							"shared.steps","shoppingcart.steps"
					},
//					dryRun = true, 
					monochrome = true
					)
public class RunCucumberTest {
}