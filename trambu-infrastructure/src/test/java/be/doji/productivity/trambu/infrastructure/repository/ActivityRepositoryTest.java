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