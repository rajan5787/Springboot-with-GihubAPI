# See
# https://github.com/intuit/karate#syntax-guide
# for how to write feature scenarios
Feature: As a developer i want to test the user uri

  Scenario: Get User with zero followers
    Given url microserviceUrl
    And path '/users/zeroFollowers'
    When method GET
    Then status 200
    And match header Content-Type contains 'application/json'
    # see https://github.com/intuit/karate#schema-validation
    * def userSchema = { html_url : '#string', id : '#number',  login : '#string' }
    And match response.items == '#[] userSchema'

  Scenario: Get 5 User with zero followers
    Given url microserviceUrl
    And path '/users/zeroFollowers'
    And param count = 5
    When method GET
    Then status 200
    And match header Content-Type contains 'application/json'
    # see https://github.com/intuit/karate#schema-validation
    * def userSchema = { html_url : '#string', id : '#number',  login : '#string' }
    And match response.items == '#[] userSchema'

  Scenario: Get -1 User with zero followers
    Given url microserviceUrl
    And path '/users/zeroFollowers'
    And param count = -1
    When method GET
    Then status 200
    And match header Content-Type contains 'application/json'
    # see https://github.com/intuit/karate#schema-validation
    And match response.errorMessage == 'Count value is less than 1'