package be.doji.productivity.trambu.front.elements;

import be.doji.productivity.trambu.domain.activity.Activity;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.Data;

@Data
public class ActivityModel {

  private static final String BASIC_DATE_TIME_PATTERN = "dd/MM/uuuu HH:mm:ss";
  private static final DateTimeFormatter df = DateTimeFormatter
      .ofPattern(BASIC_DATE_TIME_PATTERN, Locale.FRANCE);

  private String id;
  private String title;
  private List<String> projects;
  private List<String> tags;
  private String deadline = "No deadline in sight";
  private boolean completed;

  public ActivityModel(Activity toBuildFrom) {
    this.title = toBuildFrom.getTitle();
    this.projects = toBuildFrom.getProjects();
    this.tags = toBuildFrom.getTags();
    this.completed = toBuildFrom.isCompleted();
    this.id = UUID.randomUUID().toString();

    toBuildFrom.getDeadline()
        .ifPresent(timePoint -> this.deadline = df.format(timePoint.toLocalDateTime()));
  }

  public void toggleCompleted() {
    this.completed = !this.completed;
  }
}
