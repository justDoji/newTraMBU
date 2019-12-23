package be.doji.productivity.trambu.zulma.rest;

import static be.doji.productivity.trambu.zulma.messages.HttpMessageUtil.createHttpConnection;
import static be.doji.productivity.trambu.zulma.messages.HttpMessageUtil.readHttpResponse;
import static be.doji.productivity.trambu.zulma.messages.HttpMessageUtil.sendHttpRequest;
import static be.doji.productivity.trambu.zulma.messages.HttpMessageUtil.toBytes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public class RestSender {

  public String sendRequest(String endpoint, String fullMessage, String messageAction)
      throws IOException {
    HttpURLConnection httpConn = createHttpConnection(endpoint);

    byte[] message = StringUtils.isBlank(fullMessage) ? null : toBytes(fullMessage);
    sendHttpRequest(
        httpConn,
        createRestHeader(message),
        messageAction,
        message
    );

    return readHttpResponse(httpConn);
  }

  @NonNull
  private Map<String, String> createRestHeader(byte[] bytesToWrite) {
    Map<String, String> connectionProperties = new HashMap<>();
    connectionProperties.put("Content-Type", "application/json; charset=utf-8");
    connectionProperties.put("Transfer-Encoding", "chunked");
    if(bytesToWrite != null ) {
      connectionProperties.put("Content-Length", String.valueOf(bytesToWrite.length));
    }
    return connectionProperties;
  }
}
