@component=backend

Feature: Creating and organizing actions

  As a busy person,
  I want to be able to create actions, and organize them.

Scenario: Create an action
  Given the planning service is alive
  When creating an action named "Writing Trambu Code"
  Then I can retreive the created action

Scenario: Creating projects
  Given the planning service is alive
  When creating a project named "Shopping"
  And creating a project "Work"
  Then I can retrieve 2 projects from the system

Scenario: Assign action to a project
  Given the planning service is alive
  And an action "Cleaning my room" exists
  When assigning this action to the project "Household"
  Then the action is returned when looking up the "Household" project

Scenario: Assign multiple actions to a project
  Given the planning service is alive
  And an action "Cleaning my room" exists
  And an action "Take out the garbage" exists
  When assigning these actions to the project "Household"
  And retrieving the "Household" project
  Then I retrieve actions "Cleaning my room" and "Take out the garbage"




