package be.doji.productivity.trambu.zulma.steps;

import static be.doji.productivity.trambu.zulma.messages.HttpActions.POST;

import be.doji.productivity.trambu.zulma.steps.substeps.RestSteps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.UUID;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import org.jetbrains.annotations.NotNull;

public class TimetrackingSteps {

  public static final String EMPTY_CONTENT = "";
  @Steps
  private RestSteps restSteps;


  public static final String CREATE_BASE_URL = "http://localhost:8069/create";
  public static final String OCCUPATION_REFERENCE = "occupationReference";

  @When("registering an occupation with title={string}")
  public void registeringAnOccupationWithTitle(String occupationTitle) {
    restSteps.sendMessage(
        trackingEndpoint(occupationTitle, createAndStoreReference()),
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
  private String trackingEndpoint(String title, UUID reference) {
    return CREATE_BASE_URL + "?" + "title=" + title + "&reference=" + reference.toString();
  }

  @Then("I can retreive the tracked time for this action")
  public void iCanRetreiveTheTrackedTimeForThisAction() {

  }
}
