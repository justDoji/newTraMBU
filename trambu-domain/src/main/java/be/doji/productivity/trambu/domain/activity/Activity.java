/**
 * GNU Affero General Public License Version 3
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package be.doji.productivity.trambu.domain.activity;

import be.doji.productivity.trambu.domain.time.TimePoint;
import be.doji.productivity.trambu.domain.time.TimeSlot;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
  @Getter @Setter private List<String> projects = new ArrayList<>();
  @Getter @Setter private List<String> tags = new ArrayList<>();
  private UUID referenceKey;

  public Activity() {
    /* Empty default constructor for use in reflection by Converter classes */
    this.projects = new ArrayList<>();
    this.tags = new ArrayList<>();
    this.referenceKey = UUID.randomUUID();
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

  public String getReferenceKey() {
    return referenceKey.toString();
  }

  public void setReferenceKey(String referenceKey) {
    if (referenceKey != null) {
      this.referenceKey = UUID.fromString(referenceKey);
    }
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
    private String referenceKey;

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
      result.setReferenceKey(this.referenceKey);

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

    public ActivityBuilder referenceKey(String referenceKey) {
      this.referenceKey = referenceKey;
      return this;
    }
  }
}
