package be.doji.productivity.trambu.timetracking.infra.exception;

public class OccupationUnknown extends RuntimeException {

  public OccupationUnknown() {
  }

  public OccupationUnknown(String message) {
    super(message);
  }

  public OccupationUnknown(String message, Throwable cause) {
    super(message, cause);
  }
}
