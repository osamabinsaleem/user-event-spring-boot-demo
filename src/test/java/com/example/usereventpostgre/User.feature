Feature: Test User API

  Background:
    * url baseUrl
    * def result = callonce read('./login.feature')
    * def token = $result.responseHeaders['Authorization'][0]
    * configure headers = { 'Content-Type': 'application/json', 'Authorization' : '#(token)' }

  Scenario: Get all predefined users
    * configure logPrettyResponse = true
    Given path '/user'
    When method GET
    Then status 200
    #* print 'Response is: ',response.content
    * def result = call read('./userEvent.feature') response.content
    * def created = $result[*].response
    #* print 'Response is: ',created

    #Given path '/user_event/2051'
    #When method GET
    #Then status 200
    * match each created == [{createdAt:'#notnull', updatedAt:'#notnull', id:'#notnull', name:'#notnull', event:'#notnull'}]