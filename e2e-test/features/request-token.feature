@RequestTokens
Feature: Request Tokens
Scenario: request tokens when client as 0 tokens
	Given a registered user with 0 tokens
	When the user requests 5 new tokens
	Then the user receives 5 tokens

