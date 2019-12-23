package be.doji.productivity.trambu.planning.domain;

import be.doji.productivity.trambu.kernel.time.PointInTime;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Comment {

  private PointInTime timeStamp = PointInTime.fromDateTime(LocalDateTime.now());
  private String text;

  public Comment(String text) {
    this.text = text;
  }

}
