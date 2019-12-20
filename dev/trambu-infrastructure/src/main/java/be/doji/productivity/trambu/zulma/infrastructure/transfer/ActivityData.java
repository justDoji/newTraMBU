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
package be.doji.productivity.trambu.zulma.infrastructure.transfer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

@Data
@Entity
@Table(name = "ACTIVITY")
public class ActivityData {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_seq")
  @SequenceGenerator(name = "activity_seq", sequenceName = "SEQ_ACTIVITY")
  private Long id;

  @Type(type = "org.hibernate.type.NumericBooleanType")
  @Column(name = "COMPLETED", nullable = false)
  private boolean completed;

  @Column(name = "TITLE", nullable = false)
  private String title;

  @Column(name = "DEADLINE")
  private String deadline;

  @Column(name = "REFERENCE_KEY")
  private String referenceKey;

  @OneToMany(targetEntity = ActivityTagData.class, mappedBy = "activity", cascade = {
      CascadeType.ALL}, orphanRemoval = true)
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<ActivityTagData> tags = new ArrayList<>();

  @OneToMany(targetEntity = LogPointData.class, mappedBy = "activity", cascade = {
      CascadeType.ALL}, orphanRemoval = true)
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<LogPointData> timelogs = new ArrayList<>();

  @Column(name = "COMMENTS")
  private String comments;

  public ActivityData() {
    this.referenceKey = UUID.randomUUID().toString();
  }

  /**
   * Extra setter for double binding
   */
  public void setTags(List<ActivityTagData> tagsToSet) {
    this.tags = new ArrayList<>();
    for (ActivityTagData tag : tagsToSet) {
      tag.setActivity(this);
      this.tags.add(tag);
    }
  }

  /**
   * Extra setter for double binding
   */
  public void setTimelogs(
      List<LogPointData> timelogs) {
    this.timelogs = new ArrayList<>();
    for (LogPointData logPoint : timelogs) {
      logPoint.setActivity(this);
      this.timelogs.add(logPoint);
    }
  }

  @OneToMany(targetEntity = ActivityProjectData.class, mappedBy = "activity", cascade = {
      CascadeType.ALL}, orphanRemoval = true)
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<ActivityProjectData> projects = new ArrayList<>();

  public void setProjects(List<ActivityProjectData> projectsToSet) {
    this.projects = new ArrayList<>();
    for (ActivityProjectData project : projectsToSet) {
      project.setActivity(this);
      this.projects.add(project);
    }
  }

  public Long getId() {
    return id;
  }

  public void addTimelog(LogPointData pointData) {
    if (pointData != null) {
      pointData.setActivity(this);
      this.timelogs.add(pointData);
    }
  }

}
