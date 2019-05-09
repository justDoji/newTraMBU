/**
 * MIT License
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package be.doji.productivity.trambu.domain.activity;

import be.doji.productivity.trambu.domain.time.TimePoint;
import be.doji.productivity.trambu.domain.time.TimeSlot;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 * An activity is something you do at a certain time. Activities have a link to times spent on it
 *
 * An be.doji.productivity.Activity has a configurable set of properties. These can be configured.
 */
public class Activity {

  private String title;
  private TimePoint plannedStart;
  private TimePoint plannedEnd;
  private Importance importance;
  private TimePoint deadline;
  private boolean completed;

  void setTitle(String activityName) {
    this.title = activityName;
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

  public String getTitle() {
    return this.title;
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

  public boolean isCompleted() {
    return completed;
  }

  void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public boolean isDeadlineExceeded() {
    return TimePoint.isBefore(TimePoint.now(), this.deadline);
  }

  public static class ActivityBuilder {

    private String activityTitle;
    private TimePoint plannedStart;
    private TimePoint plannedEnd;
    private Importance importance = Importance.NORMAL;
    private TimePoint deadline;
    private boolean completed;

    public ActivityBuilder title(String activityName) {
      this.activityTitle = activityName;
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
      result.setTitle(this.activityTitle);
      result.setPlannedStart(this.plannedStart);
      result.setPlannedEnd(this.plannedEnd);
      result.setImportance(this.importance);
      result.setDeadline(this.deadline);
      result.setCompleted(this.completed);

      return result;
    }

    private void throwExceptionIfInvalidParameters() {
      if (StringUtils.isBlank(this.activityTitle)) {
        throw new IllegalStateException("The activity title can not be empty");
      }
      if (TimePoint.isBefore(this.plannedEnd, this.plannedStart)) {
        throw new IllegalStateException("The activity end date must be after the start date");
      }
    }

    public ActivityBuilder deadline(TimePoint deadline) {
      this.deadline = deadline;
      return this;
    }

    public ActivityBuilder completed(boolean completed) {
      this.completed = completed;
      return this;
    }
  }
}
