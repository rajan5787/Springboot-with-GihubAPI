# Spring Boot Coding Exercise

This is a simple coding exercise that will allow you to demonstrate your knowledge
of spring boot by using a microservice to call a downstream service and return
some results.

## Project Structure

This is a multi module maven project with two modules:

- The `micoservice` module produces a spring boot application.
- The `functional-tests` is used to run functional tests using the [karate](https://github.com/intuit/karate) library.


## The Exercises

Example curl api calls for these exercises are listed in the following gist https://gist.github.com/bartonhammond/0a19da4c24c0f644ae38

**1. Find the hottest repositories created in the last week**

Use the [GitHub API][1] to expose an endpoint in this microservice the will get a list of the
highest starred repositories in the last week.

The endpoint should accept a parameter that sets the number of repositories to return.

The following fields should be returned:

      html_url
      watchers_count
      language
      description
      name

**2. Find the oldest user accounts with zero followers**

Use the [GitHub API][1] to expose an endpoint in this microservice that will find the oldest
accounts with zero followers.

The endpoint should accept a parameter that sets the number of accounts to return.

The following fields should be returned:

      id
      login
      html_url

[1]: http://developer.github.com/v3/search/#search-repositories

APIs : 

To get Repository : http://localhost:8080/repositories/hottest?count=2
To get User : http://localhost:8080/users/zeroFollowers?count=2

You can access the report from below path : 
file:///Users/rajanpipaliya/Downloads/c967784-spring-boot-coding-exercise-f54741b9143b/coverage%20report/index.html