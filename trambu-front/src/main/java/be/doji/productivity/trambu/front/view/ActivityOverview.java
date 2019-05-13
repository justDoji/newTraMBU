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
package be.doji.productivity.trambu.front.view;

import be.doji.productivity.trambu.front.elements.ActivityModel;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityProjectData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityTagData;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

@SessionScope
@Named
public class ActivityOverview {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired ActivityDatabaseRepository repository;

  private List<ActivityModel> model;

  @PostConstruct
  public void init() {
    if (this.model == null) {
      repository.save(createMockData());

      this.model = repository.findAll().stream()
          .map(db -> new ActivityModel(db.toDomainObject(), db.getId()))
          .collect(Collectors.toList());
    }
  }

  public ActivityData createMockData() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Build a User Interface in JSF");
    activityData.setCompleted(true);

    ArrayList<ActivityTagData> tags = new ArrayList<>();
    tags.add(new ActivityTagData("tag 1"));
    tags.add(new ActivityTagData("tag 2"));
    tags.add(new ActivityTagData("tag 3"));
    activityData.setTags(tags);

    ArrayList<ActivityProjectData> projects = new ArrayList<>();
    projects.add(new ActivityProjectData("project 1"));
    projects.add(new ActivityProjectData("project 2"));
    projects.add(new ActivityProjectData("project 3"));
    activityData.setProjects(projects);

    return activityData;
  }

  public List<ActivityModel> getActivities() {
    return this.model;
  }
}
