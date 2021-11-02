# pokerDemo
Demo project for poker card game

## Assignment
Write an application in Java that would determine the winning poker player when given two sets of
cards (5 cards for each player).

The way you will input and output the information is not important – choose whatever you think is best.
It could be a simple console application, advanced web application or even a mobile app that we must
buy from the AppStore (ok, maybe not) - whatever is most comfortable for you and gets the job done.
Luckily it does not have to be a complete solution (it takes time and we know programmers have a life
too), but the code should represent you, your solution and how you see production code.

## Build and run instructions:
* build and run - ./mvnw spring-boot:run

## Request mappings
REST API:
* GET - http://localhost:8080/poker/5CardDrawExpress?players=5
  * players - optional request param, can be omitted, has default value of 2
  * deals random 5 cards for requested number of players from a single card deck and determines the winning hand

## Explanations
Unfortunately due to my health issues I couldn't complete the assignment in a proper manner,
the current solution is lacking in quality as a production code.