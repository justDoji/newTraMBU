package be.doji.productivity.trambu.zulma.soap;

import static be.doji.productivity.trambu.zulma.messages.HttpMessageUtil.createHttpConnection;
import static be.doji.productivity.trambu.zulma.messages.HttpMessageUtil.readHttpResponse;
import static be.doji.productivity.trambu.zulma.messages.HttpMessageUtil.sendHttpRequest;
import static be.doji.productivity.trambu.zulma.messages.HttpMessageUtil.toBytes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class SoapSender {

  public String sendRequest(String endpoint, String soapAction, String fullMessage)
      throws IOException {
    HttpURLConnection httpConn = createHttpConnection(endpoint);

    sendHttpRequest(
        httpConn,
        createSoapHeaders(toBytes(fullMessage), soapAction),
        "POST",
        toBytes(fullMessage)
    );

    return readHttpResponse(httpConn);
  }

  @NotNull
  private Map<String, String> createSoapHeaders(byte[] bytesToWrite, String SOAPAction) {
    Map<String, String> connectionProperties = new HashMap<>();
    connectionProperties.put("Content-Length", String.valueOf(bytesToWrite.length));
    connectionProperties.put("Content-Type", "text/xml; charset=utf-8");
    connectionProperties.put("SOAPAction", SOAPAction);
    return connectionProperties;
  }


}
