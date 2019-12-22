package be.doji.productivity.trambu.zulma.soap;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.server.handler.AbstractHandler;

/**
 * https://www.eclipse.org/jetty/documentation/current/embedding-jetty.html
 */
public class SoapListener {


  public static final int DEFAULT_STUB_ENDPOINT = 4321;

  public static Server createServer(int port) {
    Server server = new Server(port);

    server.setHandler(new SoapCaptureHandler());

    return server;
  }

  public static Server startServer(int port) throws Exception {
    Server server = createServer(port);

    server.start();
//    server.join();

    return server;
  }

  public static Server startServer() throws Exception {
    return startServer(DEFAULT_STUB_ENDPOINT);
  }


  public static class SoapCaptureHandler extends AbstractHandler {

    private final String returnMessage;
    private List<Capture> captures = new ArrayList<>();

    SoapCaptureHandler() {
      this("ACK");
    }

    SoapCaptureHandler(String returnMessage) {
      this.returnMessage = returnMessage;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {

      sendAck(response);

      String capturedRequest = baseRequest.toString();
      System.out.println("Captured request: {0}" + capturedRequest);

      captures.add(readCapture(request, baseRequest));

      baseRequest.setHandled(true);
    }

    private void sendAck(HttpServletResponse response) throws IOException {
      response.setContentType("text/html; charset=utf-8");
      response.setStatus(HttpServletResponse.SC_OK);

      PrintWriter out = response.getWriter();
      out.println("<h1>" + returnMessage + "</h1>");
    }

    private Capture readCapture(HttpServletRequest request,
        Request baseRequest) {
      Capture capture = new Capture();

      capture.setBodyContent(readBody(request));
      capture.setCalledEndpoint(baseRequest.getMetaData().getURI().toString());
      capture.setRequestMethod(baseRequest.getMetaData().getMethod());
      return capture;
    }

    private String readBody(HttpServletRequest request) {
      try {
        StringBuilder bodyContent = new StringBuilder("");
        String line = request.getReader().readLine();

        while (line != null) {
          bodyContent.append(line);
          line = request.getReader().readLine();
        }

        return bodyContent.toString();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return "ERROR";
    }

    public List<Capture> getCaptures() {
      return captures;
    }
  }
}
