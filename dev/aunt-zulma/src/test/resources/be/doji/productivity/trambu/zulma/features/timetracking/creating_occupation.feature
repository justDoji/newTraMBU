@component=backend

Feature: Creating an occupation

    In order to keep track of the time I spend on doing things
    As a busy person
    I want to be able to start tracking an an action

Scenario: Create action with valid input
  Given the timetracking service is alive
  When registering an occupation with title="Modelling the solution domain"
  Then I can retreive the tracked time for this action

