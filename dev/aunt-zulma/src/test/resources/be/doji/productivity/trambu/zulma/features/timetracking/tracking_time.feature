Feature: Tracking time

  As a busy person,
  in order to track the time I spent on various occupations,
  I want the system to keep tabs on the hours I spent doing things.

Scenario: Track time with one interval
  Given the timetracking service is alive
  And an occupation "Watching youtube videos" exists
  When starting the occupation at 18 December 20:00:00
  And stopping the occupation at 18 December 23:30:00
  Then I have spent 3.5 hours on the occupation




