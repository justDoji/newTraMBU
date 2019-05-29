package be.doji.productivity.trambu.front.controller.exception;

public class InvalidReferenceException extends Exception {

  public InvalidReferenceException() {
  }

  public InvalidReferenceException(String message) {
    super(message);
  }

  public InvalidReferenceException(String message, Throwable cause) {
    super(message, cause);
  }
}
