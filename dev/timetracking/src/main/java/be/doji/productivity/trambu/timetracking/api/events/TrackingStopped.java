package be.doji.productivity.trambu.timetracking.api.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class TrackingStopped implements Event {

  private final UUID reference;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private final LocalDateTime timeStopped;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private final LocalDateTime timestamp = LocalDateTime.now();

  public TrackingStopped(UUID reference, LocalDateTime timeStopped) {
    this.reference = reference;
    this.timeStopped = timeStopped;
  }

  @Override
  public LocalDateTime timestamp() {
    return timestamp;
  }
}
