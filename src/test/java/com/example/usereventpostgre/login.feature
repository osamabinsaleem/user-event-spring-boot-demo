Feature: Login Feature

  Background:
    * url 'http://localhost:8080'

  Scenario: Login User
    * configure headers = { 'Content-Type': 'application/json'}
    Given path '/login'
    And request {username:'ali',password:'1234'}
    When method POST
    Then status 200

    #* temp = responseHeaders['Authorization'][0]
    #* print 'temp =  ',temp