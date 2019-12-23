package be.doji.productivity.trambu.zulma.steps;

import static be.doji.productivity.trambu.zulma.messages.HttpActions.GET;
import static be.doji.productivity.trambu.zulma.messages.HttpActions.POST;

import be.doji.productivity.trambu.events.timetracking.TrackingStarted;
import be.doji.productivity.trambu.zulma.steps.substeps.RestSteps;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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
        .contains((String) Serenity.sessionVariableCalled(OCCUPATION_TITLE));
    Assertions.assertThat(response)
        .contains((String) Serenity.sessionVariableCalled(OCCUPATION_REFERENCE));
  }

  @NotNull
  private String trackingEndpoint(String reference) {
    return TRACKING_BASE_URL + "?reference=" + reference;
  }


  @When("starting the occupation at {int} {string} {int}:{int}:{int}")
 public void startingTheOccupationtDecember(int day, String month, int hour, int minute, int second) {

  }

  @NotNull
  private String startEndpoint() {
    return START_BASE_URL;
  }
}
