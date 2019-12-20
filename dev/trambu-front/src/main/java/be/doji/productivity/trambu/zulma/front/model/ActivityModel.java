/**
 * TraMBU - an open time management tool
 *
 * Copyright (C) 2019  Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 * For further information on usage, or licensing, contact the author through his github profile:
 * https://github.com/justDoji
 */
package be.doji.productivity.trambu.zulma.front.model;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.Data;

@Data
public class ActivityModel {

  private static final String BASIC_DATE_TIME_PATTERN = "dd/MM/uuuu HH:mm:ss";
  private static final DateTimeFormatter df = DateTimeFormatter
      .ofPattern(BASIC_DATE_TIME_PATTERN, Locale.FRANCE);

  private Long dataBaseId;

  private String title = "Give me a name!";
  private List<String> projects = new ArrayList<>();
  private List<String> tags = new ArrayList<>();
  private Date deadline;
  private Status status;
  private String referenceKey;
  private List<TimeLogModel> timelogs = new ArrayList<>();
  private String comments;

  private boolean editable;
  private boolean expanded;
  private boolean timelogExpanded;

  public ActivityModel() {
    this.referenceKey = UUID.randomUUID().toString();
    this.status = Status.TODO;
  }

  public boolean isEditable() {
    return editable;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  public boolean isExpanded() {
    return expanded;
  }

  public void setExpanded(boolean expanded) {
    this.expanded = expanded;
  }

  public void setProjects(List<String> projectsToSet) {
    this.projects = new ArrayList<>();
    if (projectsToSet != null) {
      this.projects.addAll(projectsToSet);
    }
  }

  public void setTags(List<String> tagsToSet) {
    this.tags = new ArrayList<>();
    if (tagsToSet != null) {
      this.tags.addAll(tagsToSet);
    }
  }

  public void setTimeLogs(List<TimeLogModel> timelogs) {
    this.timelogs = new ArrayList<>();
    this.timelogs.addAll(timelogs);
  }

  public void addTimeLog(TimeLogModel timelog) {
    this.timelogs.add(timelog);
  }

  public boolean getTimelogExpanded() {
    return timelogExpanded;
  }

  public void setTimelogExpanded(boolean timelogExpanded) {
    this.timelogExpanded = timelogExpanded;
  }

  public void toggleTimeLog() {
    if (getTimeRunning() && getLastTimelogEntry() != null) {
      getLastTimelogEntry().setEnd(new Date());
    } else {
      TimeLogModel timeLog = new TimeLogModel();
      timeLog.setStart(new Date());
      this.timelogs.add(timeLog);
    }
  }

  public boolean getTimeRunning() {
    TimeLogModel lastTimelogEntry = getLastTimelogEntry();
    if (lastTimelogEntry == null) {
      return false;
    }
    return lastTimelogEntry.getEnd() == null;
  }

  private TimeLogModel getLastTimelogEntry() {
    if (timelogs.isEmpty()) {
      return null;
    }
    return this.timelogs.get(timelogs.size() - 1);
  }

  public boolean isCompleted() {
    return this.status == Status.DONE;
  }

  public void setCompleted(boolean isCompleted) {
    this.status = isCompleted ? Status.DONE : Status.TODO;
  }

  public void toggleTimelogExpanded() {
    this.timelogExpanded = !this.timelogExpanded;
  }

  public List<String> getWrappedStatus() {
    return Collections.singletonList(this.status.toString());
  }

  public enum Status {

    TODO("TODO"),
    BUSY("BUSY"),
    DONE("DONE");

    private final String stringValue;

    Status(String stringValue) {
      this.stringValue = stringValue;
    }

    public String toString() {
      return this.stringValue;
    }
  }

}
