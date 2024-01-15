@CustomerRegistration
Feature: Customer Registration
Scenario: Successful Registration
	Given the user provides a name and bank account
	When the customer app registers the user
	Then the customer is created

Scenario: Unsuccessful Registration
	Given the user provides a name and bank account
	And the user is already registered
	When the customer app registers the user
	Then the customer is not created
