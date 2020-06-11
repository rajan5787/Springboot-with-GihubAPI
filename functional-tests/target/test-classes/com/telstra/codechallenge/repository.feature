# See
# https://github.com/intuit/karate#syntax-guide
# for how to write feature scenarios
Feature: As an api user I want to retrieve some spring boot quotes

  Scenario: Get a Repository
    Given url microserviceUrl
    And path "/repositories/hottest"
    When method GET
    Then status 200
    And match header Content-Type contains 'application/json'
    # see https://github.com/intuit/karate#schema-validation
    * def repositorySchema = { url : '#string', watchers_count : '#string',  language : '##string', description : '#string', name : '#string' }
    And match response.items == '#[] repositorySchema'

  Scenario: Get a 5 hottest Repository
    Given url microserviceUrl
    And path "/repositories/hottest"
    And param count = 5
    When method GET
    Then status 200
    And match header Content-Type contains 'application/json'
    # see https://github.com/intuit/karate#schema-validation
    * def repositorySchema = { url : '#string', watchers_count : '#string',  language : '##string', description : '#string', name : '#string' }
    And match response.items == '#[] repositorySchema'

  Scenario: Get a 0 hottest Repository
    Given url microserviceUrl
    And path "/repositories/hottest"
    And param count = 0
    When method GET
    Then status 200
    And match header Content-Type contains 'application/json'
    # see https://github.com/intuit/karate#schema-validation
    And match response.errorMessage == 'Count value is less than 1'