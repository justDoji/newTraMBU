package be.doji.productivity.trambu.events.planning.in;

import be.doji.productivity.trambu.events.Event;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class ProjectCreated implements Event {
  
  private UUID projectReference;
  private String title;

  public ProjectCreated(UUID projectReference, String title) {
    this.projectReference = projectReference;
    this.title = title;
  }

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime timestamp = LocalDateTime.now();

  @Override
  public LocalDateTime timestamp() {
    return null;
  }
}
