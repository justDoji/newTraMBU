package be.doji.productivity.trambu.timetracking.infra.access;

import be.doji.productivity.trambu.timetracking.infra.exception.OccupationUnknown;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OccupationExceptionHandler {

  @ExceptionHandler(OccupationUnknown.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public void handleOccupationUnknown() {
  }

}
