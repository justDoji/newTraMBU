package be.doji.productivity.trambu.listeners;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;
import org.springframework.util.StringUtils;

public class MarkdownListener implements StepListener {

  public static final String NEWLINE = "(\\r\\n|\\r|\\n)";
  private File outputFile;

  @Override
  public void testSuiteStarted(Class<?> storyClass) {

  }

  @Override
  public void testSuiteStarted(Story story) {
    try {
      Files.createDirectories(new File("target/manual").toPath());
      outputFile = new File("target/manual/" + story.getName() + "-testcases.md");
      if(Files.notExists(outputFile.toPath())) {
        Files.createFile(outputFile.toPath());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void testSuiteFinished() {

  }

  @Override
  public void testStarted(String description) {
    testStarted(description);

  }

  @Override
  public void testStarted(String description, String id) {
    try {
      Files.write(outputFile.toPath(),
          Arrays.asList(
              "# " + description + " " + id,
              System.lineSeparator(),
              "executed by: ",
              "on date: ",
              System.lineSeparator(),
              "## Test Steps:",
              System.lineSeparator()
          ),
          StandardOpenOption.APPEND);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void testFinished(TestOutcome result) {

    try {
      Files.write(outputFile.toPath(), Arrays.asList(System.lineSeparator() ,"***", " END OF TESTCASE ", "***"),
          StandardOpenOption.APPEND);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void testRetried() {

  }

  @Override
  public void stepStarted(ExecutedStepDescription description) {
    try {
      if (StringUtils.startsWithIgnoreCase(description.getTitle(), "then") || StringUtils.startsWithIgnoreCase(description.getTitle(), "and")) {
        formatAssert(description);
      } else {
        formatAction(description);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void formatAssert(ExecutedStepDescription description) {
    try {
      List<String> lines = new ArrayList<>();

      lines.add(System.lineSeparator());

      lines.add("***");
      lines.add("**VERIFY**");

      lines.add(" EXPECTED");
      lines.add(" > " + description.getTitle().toLowerCase()
          .replaceFirst("then", "")
          .replaceFirst("and", "")
          .replaceAll(NEWLINE, System.lineSeparator() + " > "));
      lines.add(System.lineSeparator());
      lines.add(" ACTUAL");
      lines.add("> ");

      lines.add(System.lineSeparator());
      lines.add("- [ ] OK");
      lines.add("- [ ] NOK");
      lines.add("***");
      lines.add(System.lineSeparator());

      Files.write(outputFile.toPath(), lines,
          StandardOpenOption.APPEND);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void formatAction(ExecutedStepDescription description) throws IOException {
    Files.write(outputFile.toPath(), Collections.singletonList("* " + description.getTitle()),
        StandardOpenOption.APPEND);
  }

  @Override
  public void skippedStepStarted(ExecutedStepDescription description) {
    stepStarted(description);
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
