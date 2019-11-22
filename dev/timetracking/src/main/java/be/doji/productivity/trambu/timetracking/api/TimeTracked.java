package be.doji.productivity.trambu.timetracking.api;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeTracked {

  public TimeTracked() {
  }

  public TimeTracked(String reference, String title, double timeSpentInHours,
      List<Pair<LocalDateTime, LocalDateTime>> timeEntries) {
    this.reference = reference;
    this.title = title;
    this.timeSpentInHours = timeSpentInHours;
    this.timeEntries = timeEntries;
  }

  public String reference;
  public String title;
  public double timeSpentInHours;
  public List<Pair<LocalDateTime, LocalDateTime>> timeEntries;

}
