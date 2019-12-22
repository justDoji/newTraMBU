package be.doji.productivity.trambu.zulma.steps.substeps;

import be.doji.productivity.trambu.zulma.exception.MessageSendException;
import be.doji.productivity.trambu.zulma.rest.RestSender;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import net.serenitybdd.core.Serenity;

public class RestSteps {

  public void sendMessage(String endpoint, String content, String action) {
    try {
      RestSender restSender = new RestSender();
      String response = restSender.sendRequest(encode(endpoint), "", action);

      Serenity.recordReportData().withTitle("Message sent to endpoint:").andContents(endpoint);
      Serenity.recordReportData().withTitle("Message content:").andContents(content);
      Serenity.recordReportData().withTitle("Service answered with: ").andContents(response);

    } catch (IOException | URISyntaxException e) {
      Serenity.recordReportData().withTitle("Message send failed:")
          .andContents(e.getMessage() + " - " + Arrays.toString(e.getStackTrace()));

      throw new MessageSendException("Message send failed", e);
    }
  }


  public String encode(String unformattedURL)
      throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
    String decodedURL = URLDecoder.decode(unformattedURL, "UTF-8");
    URL url = new URL(decodedURL);
    URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(),
        url.getPath(), url.getQuery(), url.getRef());
    return uri.toASCIIString();
  }
}
