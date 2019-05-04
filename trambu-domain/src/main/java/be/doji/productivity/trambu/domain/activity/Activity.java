package be.doji.productivity.trambu.domain.activity;

import be.doji.productivity.trambu.domain.time.TimePoint;
import be.doji.productivity.trambu.domain.time.TimeSlot;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 * An activity is something you do at a certain time. Activities have a link to times spent on it
 *
 * An be.doji.productivity.Activity has a configurable set of properties. These can be
 * configured.
 */
public class Activity {

  private String name;
  private TimePoint plannedStart;
  private TimePoint plannedEnd;
  private Importance importance;
  private TimePoint deadline;

  void setName(String activityName) {
    this.name = activityName;
  }

  void setPlannedStart(TimePoint plannedStart) {
    this.plannedStart = plannedStart;
  }

  void setPlannedEnd(TimePoint plannedEnd) {
    this.plannedEnd = plannedEnd;
  }

  void setImportance(Importance importance) {
    this.importance = importance;
  }

  void setDeadline(TimePoint deadline) {
    this.deadline = deadline;
  }

  private Activity() {
  }

  public static ActivityBuilder builder() {
    return new ActivityBuilder();
  }

  public TimeSlot getAssignedTimeSlot() {
    return new TimeSlot(plannedStart, plannedEnd);
  }

  public String getName() {
    return this.name;
  }

  Importance getImportance() {
    return this.importance;
  }

  public Optional<TimePoint> getDeadline() {
    return this.deadline == null ? Optional.empty() : Optional.of(this.deadline);
  }

  public Optional<TimePoint> getPlannedEnd() {
    return this.plannedEnd == null ? Optional.empty() : Optional.of(this.plannedEnd);
  }

  public Optional<TimePoint> getPlannedStart() {
    return this.plannedStart == null ? Optional.empty() : Optional.of(this.plannedStart);
  }


  public boolean isDeadlineExceeded() {
    return TimePoint.isBefore(TimePoint.now(), this.deadline);
  }


  public static class ActivityBuilder {

    private String activityName;
    private TimePoint plannedStart;
    private TimePoint plannedEnd;
    private Importance importance = Importance.NORMAL;
    private TimePoint deadline;

    public ActivityBuilder name(String activityName) {
      this.activityName = activityName;
      return this;
    }

    public ActivityBuilder plannedStartAt(TimePoint startDate) {
      this.plannedStart = startDate;
      return this;
    }

    public ActivityBuilder plannedEndAt(TimePoint plannedEnd) {
      this.plannedEnd = plannedEnd;
      return this;
    }

    public ActivityBuilder importance(Importance prio) {
      this.importance = prio;
      return this;
    }

    public Activity build() {
      throwExceptionIfInvalidParameters();

      Activity result = new Activity();
      result.setName(this.activityName);
      result.setPlannedStart(this.plannedStart);
      result.setPlannedEnd(this.plannedEnd);
      result.setImportance(this.importance);
      result.setDeadline(this.deadline);

      return result;
    }

    private void throwExceptionIfInvalidParameters() {
      if (StringUtils.isBlank(this.activityName)) {
        throw new IllegalStateException("The activity name can not be empty");
      }
      if (TimePoint.isBefore(this.plannedEnd, this.plannedStart)) {
        throw new IllegalStateException("The activity end date must be after the start date");
      }
    }

    public ActivityBuilder deadline(TimePoint deadline) {
      this.deadline = deadline;
      return this;
    }
  }
}
