package be.doji.productivity.trambu.events.planning;

import be.doji.productivity.trambu.events.Event;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class ActionCreated implements Event {

  private UUID reference;
  private String actionName;

  public ActionCreated(UUID reference, String actionName) {
    this.reference = reference;
    this.actionName = actionName;
  }

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime timestamp = LocalDateTime.now();

  @Override
  public LocalDateTime timestamp() {
    return timestamp;
  }

}
