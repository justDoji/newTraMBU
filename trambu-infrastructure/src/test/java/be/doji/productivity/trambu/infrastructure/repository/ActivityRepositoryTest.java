package be.doji.productivity.trambu.infrastructure.repository;


import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.activity.Importance;
import be.doji.productivity.trambu.domain.time.TimePoint;
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
  private ActivityRepository repository;

  @Test
  public void initialization_emptyActivityRepository() {
    List<Activity> activitiesInRepo = repository.getAll();

    assertThat(activitiesInRepo).isEmpty();
  }

  @Test
  public void addActivity() {
    Activity activityToSave = Activity.builder()
        .name("Implement infrastructure layer")
        .plannedStartAt(TimePoint.fromString("05/05/2019 10:00:00"))
        .plannedEndAt(TimePoint.fromString("12/05/2019 18:00:00"))
        .importance(Importance.NORMAL)
        .build();
    assertThat(repository.getAll()).isEmpty();

    repository.save(activityToSave);

    assertThat(repository.getAll()).hasSize(1);
  }

  @After
  public void cleanUp() {
    repository.clear();
    assertThat(repository.getAll()).isEmpty();
  }


}