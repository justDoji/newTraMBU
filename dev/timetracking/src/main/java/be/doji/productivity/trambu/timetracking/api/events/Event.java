package be.doji.productivity.trambu.timetracking.api.events;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface Event extends Serializable {

  LocalDateTime timestamp();
}
