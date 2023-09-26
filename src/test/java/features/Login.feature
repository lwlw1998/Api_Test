Feature: Foodics API Testing

  Scenario: Successful Login
    Given I have a valid email and password
    When I send a POST request to "/cp_internal/login"
    Then the response status code should be 200
    And the response body should contain a token

  Scenario: Retrieve User Information
    Given I have a valid token
    When I send a GET request to "/cp_internal/whoami"
    Then the response status code should be 200
    And the response body should contain the user email
