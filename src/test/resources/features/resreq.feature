@api
Feature: GET list of users from reqres
    find list of users page by page from public api reqres

    Background:
        Given James is at the base URL
    @test
    Scenario Outline: James would like to fetch list of users page by page
        When James wants to fetch list of users by page "<count>"
        Then James should be able to list all the users

        Examples:
        |count|
        | 1 |
        | 2 |

    @test
    Scenario: James would like to fetch single user
        find single user
        When James wants to fetch single user
        Then James should be able to get single user

    @test
    @add
    Scenario: James would like to add new user
        Add a new user
        When James wants to add new user
        Then James should be able to add new user

    @test
    @delete
    Scenario: James would like to delete a user
        Delete a user
        When James wants to delete a user
        Then James should be able to delete the user

    @test
    @patch
    Scenario: James would like to alter a user
        update the existing user
        When James wants to alter a user
        Then James should be able to alter the user