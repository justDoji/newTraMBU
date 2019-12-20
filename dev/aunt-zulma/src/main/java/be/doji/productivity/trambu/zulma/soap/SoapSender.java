package be.doji.productivity.trambu.zulma.soap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class SoapSender {

  public String sendRequest(String endpoint, String soapAction, String fullMessage) throws IOException {
      URL url = new URL(endpoint);
      URLConnection connection = url.openConnection();
      HttpURLConnection httpConn = (HttpURLConnection) connection;

    sendHttpRequest(httpConn, toBytes(fullMessage), soapAction);
    InputStreamReader isr =
        new InputStreamReader(httpConn.getInputStream());
    BufferedReader in = new BufferedReader(isr);

    return readHttpResponse(in);
  }

  private String readHttpResponse(BufferedReader in) throws IOException {
    String outputString = "";
    String responseString = "";
    //Write the SOAP message response to a String.
    while ((responseString = in.readLine()) != null) {
      outputString = outputString + responseString;
    }
    return outputString;
  }

  private void sendHttpRequest(HttpURLConnection httpConn, byte[] b, String SOAPAction)
      throws IOException {
    // Set the appropriate HTTP parameters.
    httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
    httpConn.setRequestProperty("Content-Type", "text/be.doji.tools.xml; charset=utf-8");
    httpConn.setRequestProperty("SOAPAction", SOAPAction);
    httpConn.setRequestMethod("POST");
    httpConn.setDoOutput(true);
    httpConn.setDoInput(true);
    OutputStream out = httpConn.getOutputStream();

    out.write(b);
    out.close();
  }

  private byte[] toBytes(String fullMessage) throws IOException {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();

    byte[] buffer = fullMessage.getBytes();
    bout.write(buffer);
    return bout.toByteArray();
  }

}
