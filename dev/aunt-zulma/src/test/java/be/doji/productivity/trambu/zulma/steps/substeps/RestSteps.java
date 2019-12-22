package be.doji.productivity.trambu.zulma.steps.substeps;

import static be.doji.productivity.trambu.zulma.messages.HttpActions.POST;

import be.doji.productivity.trambu.zulma.rest.RestSender;
import java.io.IOException;
import java.util.Arrays;
import net.serenitybdd.core.Serenity;

public class RestSteps {

  public void sendMessage(String endpoint, String content, String post) {
    try {
      RestSender restSender = new RestSender();
      String response = restSender.sendRequest(endpoint, "", POST);

      Serenity.recordReportData().withTitle("Message sent to endpoint:").andContents(endpoint);
      Serenity.recordReportData().withTitle("Message content:").andContents(content);
      Serenity.recordReportData().withTitle("Service answered with: ").andContents(response);
      
    } catch (IOException e) {
      Serenity.recordReportData().withTitle("Message send failed:")
          .andContents(e.getMessage() + " - " + Arrays.toString(e.getStackTrace()));
    }
  }
}
