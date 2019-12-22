package be.doji.productivity.trambu.zulma.exception;

public class MessageSendException extends AssertionError {

  public MessageSendException(String message, Throwable cause) {
    super(message, cause);
  }
}
