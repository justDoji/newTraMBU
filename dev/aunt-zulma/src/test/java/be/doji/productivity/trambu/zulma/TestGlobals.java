package be.doji.productivity.trambu.zulma;

import be.doji.productivity.trambu.zulma.soap.SoapListener;
import java.util.Optional;
import org.seleniumhq.jetty9.server.Server;

public final class TestGlobals {

  private static Server captureServer;

  public static Optional<Server> getCaptureServer() {
    return Optional.ofNullable(captureServer);
  }

  public static void setCaptureServer(Server captureServer) {
    TestGlobals.captureServer = captureServer;
  }

  public static void startCaptureServer() throws Exception {
    if (getCaptureServer().isPresent() && getCaptureServer().get().isRunning()) {
      getCaptureServer().get().stop();
    }

    setCaptureServer(SoapListener.startServer());

    System.out.println("Started listen server");
  }


  public static void stopCaptureServer() {
    try {
      if (getCaptureServer().isPresent()) {
        getCaptureServer().get().stop();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
