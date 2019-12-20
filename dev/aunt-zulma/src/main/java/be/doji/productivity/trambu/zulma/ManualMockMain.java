package be.doji.productivity.trambu.zulma;

import be.doji.productivity.trambu.zulma.soap.SoapListener;

/**
 * Class used to manually start a Capture HTTP listener for debugging and exploratory purposes.
 */
public class ManualMockMain {

  public static void main(String[] args) {
    try {

      SoapListener.startServer(8080);

      while (true) {
        //Loop forever
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
