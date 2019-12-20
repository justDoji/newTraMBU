package be.doji.productivity.trambu.zulma.soap;

import java.time.LocalDateTime;

public class Capture {

  private String bodyContent;
  private String calledEndpoint;
  private String requestMethod;
  private LocalDateTime captureTime = LocalDateTime.now();

  public Capture() {
  }

  public String getBodyContent() {
    return bodyContent;
  }

  void setBodyContent(String bodyContent) {
    this.bodyContent = bodyContent;
  }

  void setCalledEndpoint(String calledEndpoint) {
    this.calledEndpoint = calledEndpoint;
  }

  public String getCalledEndpoint() {
    return calledEndpoint;
  }

  void setRequestMethod(String requestMethod) {
    this.requestMethod = requestMethod;
  }

  public String getRequestMethod() {
    return requestMethod;
  }

  public LocalDateTime getTimestamp() {
    return captureTime;
  }
}
