package be.doji.productivity.trambu.listeners;

import be.doji.productivity.trambu.TestGlobals;
import java.util.Map;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;
import org.junit.Assert;

public class HttpCaptorListener implements StepListener {

  public static final String LISTEN_SERVER = "LISTEN_SERVER";

  @Override
  public void testSuiteStarted(Class<?> storyClass) {

  }

  @Override
  public void testSuiteStarted(Story story) {
    try {
      TestGlobals.startCaptureServer();
    } catch (Exception e) {
      Assert.fail(
          "Failed to start capture server: " + e.getClass().toString() + " - " + e.getMessage());
    }

  }

  @Override
  public void testSuiteFinished() {
    TestGlobals.stopCaptureServer();

  }

  @Override
  public void testStarted(String description) {

  }

  @Override
  public void testStarted(String description, String id) {

  }

  @Override
  public void testFinished(TestOutcome result) {

  }

  @Override
  public void testRetried() {

  }

  @Override
  public void stepStarted(ExecutedStepDescription description) {

  }

  @Override
  public void skippedStepStarted(ExecutedStepDescription description) {

  }

  @Override
  public void stepFailed(StepFailure failure) {

  }

  @Override
  public void lastStepFailed(StepFailure failure) {

  }

  @Override
  public void stepIgnored() {

  }

  @Override
  public void stepPending() {

  }

  @Override
  public void stepPending(String message) {

  }

  @Override
  public void stepFinished() {

  }

  @Override
  public void testFailed(TestOutcome testOutcome, Throwable cause) {

  }

  @Override
  public void testIgnored() {

  }

  @Override
  public void testSkipped() {

  }

  @Override
  public void testPending() {

  }

  @Override
  public void testIsManual() {

  }

  @Override
  public void notifyScreenChange() {

  }

  @Override
  public void useExamplesFrom(DataTable table) {

  }

  @Override
  public void addNewExamplesFrom(DataTable table) {

  }

  @Override
  public void exampleStarted(Map<String, String> data) {

  }

  @Override
  public void exampleFinished() {

  }

  @Override
  public void assumptionViolated(String message) {

  }

  @Override
  public void testRunFinished() {

  }
}
