@RequestPayment
Feature: Request Payment
Scenario: Payment successful
	Given a customer with a bank account with balance 1000
	And that the customer is registered with DTU Pay
	Given a merchant with a bank account with balance 2000
	And that the merchant is registered with DTU Pay
	When the merchant initiates a payment for 100 kr by the customer
	Then the payment is successful
