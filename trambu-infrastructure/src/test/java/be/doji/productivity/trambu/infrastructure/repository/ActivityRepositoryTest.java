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
package be.doji.productivity.trambu.infrastructure.repository;


import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.activity.Importance;
import be.doji.productivity.trambu.domain.time.TimePoint;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityRepositoryTest {


  @Autowired
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  private ActivityRepository repository;

  @Test
  public void initialization_emptyActivityRepository() {
    List<Activity> activitiesInRepo = repository.getAll();

    assertThat(activitiesInRepo).isEmpty();
  }

  @Test
  public void addActivity() {
    Activity activityToSave = Activity.builder()
        .title("Implement infrastructure layer")
        .plannedStartAt(TimePoint.fromString("05/05/2019 10:00:00"))
        .plannedEndAt(TimePoint.fromString("12/05/2019 18:00:00"))
        .importance(Importance.NORMAL)
        .build();
    assertThat(repository.getAll()).isEmpty();

    repository.save(activityToSave);

    assertThat(repository.getAll()).hasSize(1);
  }

  @Test
  public void addActivity_withTags() {
    List<String> tagList = new ArrayList<>();
    tagList.add("TestTag");
    tagList.add("TestTagTwo");
    Activity activityToSave = Activity.builder()
        .title("Implement infrastructure layer")
        .plannedStartAt(TimePoint.fromString("05/05/2019 10:00:00"))
        .plannedEndAt(TimePoint.fromString("12/05/2019 18:00:00"))
        .importance(Importance.NORMAL)
        .tags(tagList)
        .build();
    assertThat(repository.getAll()).isEmpty();

    repository.save(activityToSave);

    assertThat(repository.getAll()).hasSize(1);
    assertThat(repository.getAll().get(0).getTitle()).isEqualTo("Implement infrastructure layer");
    assertThat(repository.getAll().get(0).getTags()).isNotEmpty();
    assertThat(repository.getAll().get(0).getTags()).hasSize(2);
  }

  @After
  public void cleanUp() {
    repository.clear();
    assertThat(repository.getAll()).isEmpty();
  }


}