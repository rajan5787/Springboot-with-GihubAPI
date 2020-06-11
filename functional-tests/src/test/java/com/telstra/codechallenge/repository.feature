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