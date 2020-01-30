Feature: User Event

  Background:
    * url 'http://localhost:8080'
    #* print 'Response is: ', id

  Scenario: Get all predefined users
    * configure logPrettyResponse = true
    Given path '/user_event/', typeof id != 'undefined' ? id : ''
    When method GET
    Then status 200
