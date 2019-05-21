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
package be.doji.productivity.trambu.front.transfer;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

  private String frontId;
  private String title = "Give me a name!";
  private List<String> projects = new ArrayList<>();
  private List<String> tags = new ArrayList<>();
  private Date deadline;
  private boolean completed;
  private boolean editable;
  private boolean expanded;

  public ActivityModel() {
    this.frontId = UUID.randomUUID().toString();
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
}
