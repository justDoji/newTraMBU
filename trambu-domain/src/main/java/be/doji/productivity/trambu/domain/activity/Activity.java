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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * An activity is something you do at a certain time. Activities have a link to times spent on it
 *
 * An be.doji.productivity.Activity has a configurable set of properties. These can be configured.
 */
public class Activity {

  @Getter @Setter private String title;
  @Setter private TimePoint plannedStart;
  @Setter private TimePoint plannedEnd;
  @Setter private Importance importance;
  private TimePoint deadline;
  @Getter @Setter private boolean completed;
  @Getter @Setter private List<String> projects;
  @Getter @Setter private List<String> tags;

  public Activity() {
  }

  public static ActivityBuilder builder() {
    return new ActivityBuilder();
  }

  public TimeSlot getAssignedTimeSlot() {
    return new TimeSlot(plannedStart, plannedEnd);
  }

  Importance getImportance() {
    return this.importance;
  }

  public Optional<TimePoint> getDeadline() {
    return this.deadline == null ? Optional.empty() : Optional.of(this.deadline);
  }

  public void setDeadline(String deadline) {
    if (StringUtils.isNotBlank(deadline)) {
      this.deadline = TimePoint.fromString(deadline);
    }
  }

  public void setDeadline(TimePoint deadline) {
    this.deadline = deadline;
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

    private String activityTitle;
    private TimePoint plannedStart;
    private TimePoint plannedEnd;
    private Importance importance = Importance.NORMAL;
    private TimePoint deadline;
    private boolean completed;
    private List<String> projects = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

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
      result.setTags(tags);
      result.setProjects(projects);

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

    public ActivityBuilder deadline(String deadline) {
      if (StringUtils.isNotBlank(deadline)) {
        this.deadline = TimePoint.fromString(deadline);
      }
      return this;
    }

    public ActivityBuilder tags(List<String> tags) {
      this.tags.addAll(tags);
      return this;
    }

    public ActivityBuilder projects(List<String> projects) {
      this.projects.addAll(projects);
      return this;
    }
  }
}
