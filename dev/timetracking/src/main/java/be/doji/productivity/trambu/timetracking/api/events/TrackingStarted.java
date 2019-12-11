package be.doji.productivity.trambu.timetracking.api.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class TrackingStarted implements Event {

  private final UUID reference;
  
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private final LocalDateTime timeStarted;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private final LocalDateTime timestamp = LocalDateTime.now();

  public TrackingStarted(UUID reference, LocalDateTime timeStarted) {
    this.reference = reference;
    this.timeStarted = timeStarted;
  }

  @Override
  public LocalDateTime timestamp() {
    return timestamp;
  }
}
