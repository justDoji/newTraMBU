package be.doji.productivity.trambu.zulma.features.steps;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;

public class GeneralSteps {

  private static final String HEADER_LOGO_CLASS = "logo-image";
  private WebDriver driver;

  @When("opening TRAMBU")
  public void openTrambu() {
    System.setProperty("webdriver.gecko.driver", "C:\\\\geckodriver.exe");
    try {
      driver = new FirefoxDriver();
      driver.get("http://localhost:8080/index.xhtml");
    } catch (WebDriverException e) {
      Assertions.fail("Error during test: {}", e.getMessage());
    }
  }

  @Then("TRAMBU logo is visible")
  public void assertTrambuIsRunning() {
    try {
      assertThat(driver.findElement(By.className(HEADER_LOGO_CLASS)).isDisplayed()).isTrue();
    } catch (WebDriverException e) {
      Assertions.fail("Error during test: {}", e.getMessage());
    }
  }

  @AfterScenario
  public void close() {
    driver.close();
  }

}
