package be.doji.productivity.trambu.zulma.messages;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

public final class HttpMessageUtil {

  private HttpMessageUtil() {
    throw new UnsupportedOperationException("Utility classes should not be instantiated");
  }

  public static byte[] toBytes(String fullMessage) throws IOException {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();

    byte[] buffer = fullMessage.getBytes();
    bout.write(buffer);
    return bout.toByteArray();
  }

  public static HttpURLConnection createHttpConnection(String endpoint) throws IOException {
    URL url = new URL(endpoint);
    URLConnection connection = url.openConnection();
    return (HttpURLConnection) connection;
  }

  public static void sendHttpRequest(
      HttpURLConnection httpConn, Map<String, String> requestProperties,
      String requestMethod, byte[] bytesToSend)
      throws IOException {
    for (Entry<String, String> property : requestProperties.entrySet()) {
      httpConn.setRequestProperty(property.getKey(), property.getValue());
    }
    httpConn.setRequestMethod(requestMethod);
    httpConn.setDoOutput(true);
    httpConn.setDoInput(true);

    if (bytesToSend != null) {
      OutputStream out = httpConn.getOutputStream();
      out.write(bytesToSend);
      out.close();
    }

  }

  public static String readHttpResponse(HttpURLConnection httpConn) throws IOException {

    InputStreamReader isr =
        new InputStreamReader(httpConn.getInputStream());
    BufferedReader in = new BufferedReader(isr);

    StringBuilder outputString = new StringBuilder();
    String responseString = "";
    while ((responseString = in.readLine()) != null) {
      outputString.append(responseString);
    }
    return outputString.toString();
  }

}
