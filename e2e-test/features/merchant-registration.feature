@MerchantRegistration
Feature: Merchant Registration
Scenario: Successful Registration
	Given the new merchant provides a name and bank account
	When the merchant app registers the user
	Then the merchant is created

Scenario: Unsuccessful Registration
	Given the new merchant provides a name and bank account
	And the new merchant is already registered
	When the merchant app registers the user
	Then the merchant is not created
