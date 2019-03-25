Feature: Cart checkout

Scenario: Save for later
	Given I am on homepage
	When I click on cookware menu
	Then I see Tea Kettle option on left side menu
	When I click on Tea Kettle option
	Then I should be navigated to tea kettles page
	When I select any tea kettle 
	Then I should see product details page
	When I click on add to cart
	Then I should see cart summary pop-up
	When I click on checkout
	Then I should see shoppingcart page
	When I click on save for later
	Then I should see the message for saved items