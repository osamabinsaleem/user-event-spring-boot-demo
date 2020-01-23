Feature: Test User API

  Background:
    * url 'http://localhost:8080'
    * def result = callonce read('./login.feature')

  Scenario: Get all predefined users
    * def token = $result.responseHeaders['Authorization'][0]
    * print 'temp =  ',token
    * configure headers = { 'Content-Type': 'application/json', 'Authorization' : '#(token)' }
    Given path '/user_event/2051'
    When method GET
    Then status 200
    * print 'Response is: ',response
    And match $ == [{createdAt:'#notnull', updatedAt:'#notnull', id:'#notnull', name:'#notnull', event:'#notnull'}]