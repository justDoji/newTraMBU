package be.doji.productivity.trambu.zulma.steps;

import static be.doji.productivity.trambu.zulma.messages.HttpActions.GET;
import static be.doji.productivity.trambu.zulma.messages.HttpActions.POST;

import be.doji.productivity.trambu.events.timetracking.TrackingStarted;
import be.doji.productivity.trambu.events.timetracking.TrackingStopped;
import be.doji.productivity.trambu.events.timetracking.dto.TimeTracked;
import be.doji.productivity.trambu.zulma.exception.MessageSendException;
import be.doji.productivity.trambu.zulma.rest.JsonFormatter;
import be.doji.productivity.trambu.zulma.steps.substeps.RestSteps;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;

public class TimetrackingSteps {


  @Steps
  private RestSteps restSteps;


  public static final String CREATE_BASE_URL = "http://localhost:8069/create";
  public static final String TRACKING_BASE_URL = "http://localhost:8069/timespent";
  private static final String START_BASE_URL = "http://localhost:8069/startTracking";
  private static final String STOP_BASE_URL = "http://localhost:8069/stopTracking";

  public static final String OCCUPATION_REFERENCE = "occupationReference";
  public static final String OCCUPATION_TITLE = "occupationTitle";
  public static final String EMPTY_CONTENT = "";

  @When("registering an occupation with title={string}")
  @Given("an occupation {string} exists")
  public void registeringAnOccupationWithTitle(String occupationTitle) {
    restSteps.sendMessage(
        creationEndpoint(occupationTitle, createAndStoreReference()),
        EMPTY_CONTENT,
        POST
    );
  }

  @NotNull
  private UUID createAndStoreReference() {
    UUID reference = UUID.randomUUID();
    Serenity.setSessionVariable(OCCUPATION_REFERENCE).to(reference.toString());
    return reference;
  }

  @NotNull
  private String creationEndpoint(String title, UUID reference) {
    Serenity.setSessionVariable(OCCUPATION_TITLE).to(title);
    return CREATE_BASE_URL + "?" + "title=" + title + "&reference=" + reference.toString();
  }

  @Then("I can retreive the tracked time for this action")
  public void canRetreiveTheTrackedTime() {
    String response = restSteps.sendMessage(
        trackingEndpoint(Serenity.sessionVariableCalled(OCCUPATION_REFERENCE)),
        EMPTY_CONTENT,
        GET
    );

    Assertions.assertThat(response)
        .contains(getTitleFromStrack());
    Assertions.assertThat(response)
        .contains(getReferenceFromStack());
  }


  @NotNull
  private String trackingEndpoint(String reference) {
    return TRACKING_BASE_URL + "?reference=" + reference;
  }

  @When("starting the occupation at {int}\\/{int}\\/{int} {int}:{int}:{int}")
  public void startingTheOccupationAt(int day, int month, int year, int hour, int minute,
      int second) {
    try {
      TrackingStarted event = startEvent(day, month, year, hour, minute, second);
      restSteps.sendMessage(
          startEndpoint(),
          JsonFormatter.objectMapper().writeValueAsString(event),
          POST
      );
    } catch (JsonProcessingException e) {
      Serenity.recordReportData().withTitle("JSON creation failed:")
          .andContents(e.getMessage() + " - " + Arrays.toString(e.getStackTrace()));

      throw new MessageSendException("SON creation failed", e);
    }
  }

  @NotNull
  private TrackingStarted startEvent(int day, int month, int year, int hour, int minute,
      int second) {
    TrackingStarted event = new TrackingStarted();
    event.setReference(UUID.fromString(getReferenceFromStack()));
    event.setTimestamp(LocalDateTime.now());
    event.setTimeStarted(LocalDateTime.of(year, month, day, hour, minute, second));
    return event;
  }

  @When("stopping the occupation at {int}\\/{int}\\/{int} {int}:{int}:{int}")
  public void stoppingTheOccupationAt(int day, int month, int year, int hour, int minute,
      int second) {
    try {
      TrackingStopped event = stopEvent(day, month, year, hour, minute, second);
      restSteps.sendMessage(
          stopEndpoint(),
          JsonFormatter.objectMapper().writeValueAsString(event),
          POST
      );
    } catch (JsonProcessingException e) {
      Serenity.recordReportData().withTitle("JSON creation failed:")
          .andContents(e.getMessage() + " - " + Arrays.toString(e.getStackTrace()));

      throw new MessageSendException("JSON creation failed", e);
    }
  }

  @NotNull
  private String stopEndpoint() {
    return STOP_BASE_URL;
  }

  @NotNull
  private TrackingStopped stopEvent(int day, int month, int year, int hour, int minute,
      int second) {
    TrackingStopped event = new TrackingStopped();
    event.setReference(UUID.fromString(getReferenceFromStack()));
    event.setTimestamp(LocalDateTime.now());
    event.setTimeStopped(LocalDateTime.of(year, month, day, hour, minute, second));
    return event;
  }

  @NotNull
  private String startEndpoint() {
    return START_BASE_URL;
  }

  private String getReferenceFromStack() {
    return (String) Serenity.sessionVariableCalled(OCCUPATION_REFERENCE);
  }

  private String getTitleFromStrack() {
    return (String) Serenity.sessionVariableCalled(OCCUPATION_TITLE);
  }

  @Then("I have spent {double} hours on the occupation")
  public void iHaveSpentHoursOnTheOccupation(double timeSpent) {
    try {
      String response = restSteps.sendMessage(
          trackingEndpoint(Serenity.sessionVariableCalled(OCCUPATION_REFERENCE)),
          EMPTY_CONTENT,
          GET
      );

      TimeTracked timeTracked = JsonFormatter.objectMapper().readValue(response, TimeTracked.class);

      Assertions.assertThat(timeTracked.getTimeEntries()).hasSize(1);
      Assertions.assertThat(timeTracked.getTimeSpentInHours()).isEqualTo(timeSpent);
    } catch(JsonProcessingException e) {
      Serenity.recordReportData().withTitle("JSON read failed:")
          .andContents(e.getMessage() + " - " + Arrays.toString(e.getStackTrace()));

      throw new MessageSendException("JSON read failed", e);
    }
  }
}
