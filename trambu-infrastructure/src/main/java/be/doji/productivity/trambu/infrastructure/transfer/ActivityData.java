/**
 * MIT License
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package be.doji.productivity.trambu.infrastructure.transfer;

import be.doji.productivity.trambu.domain.activity.Activity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

  @OneToMany(targetEntity = ActivityTagData.class, mappedBy = "activity", cascade = {
      CascadeType.ALL})
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<ActivityTagData> tags = new ArrayList<>();

  public void setTags(List<ActivityTagData> tagsToSet) {
    for (ActivityTagData tag : tagsToSet) {
      tag.setActivity(this);
      this.tags.add(tag);
    }
  }

  @OneToMany(targetEntity = ActivityProjectData.class, mappedBy = "activity", cascade = {
      CascadeType.ALL})
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<ActivityProjectData> projects = new ArrayList<>();

  public void setProjects(List<ActivityProjectData> projectsToSet) {
    for (ActivityProjectData project : projectsToSet) {
      project.setActivity(this);
      this.projects.add(project);
    }
  }

  public Long getId() {
    return id;
  }

  public Activity toDomainObject() {
    return Activity.builder()
        .title(this.title)
        .completed(this.completed)
        .deadline(this.deadline)
        .tags(tags.stream().map(ActivityTagData::getValue).collect(Collectors.toList()))
        .projects(projects.stream().map(ActivityProjectData::getValue).collect(Collectors.toList()))
        .build();
  }
}
