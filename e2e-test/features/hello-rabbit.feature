
@HelloRabbit
Feature: Hello Rabbit
Scenario: send and receive string using rabbitmq
	Given the string "rabbit"
	When a post is made to the hellorabbit endpoint on the rest service
	Then the string "hello rabbit" is returned


