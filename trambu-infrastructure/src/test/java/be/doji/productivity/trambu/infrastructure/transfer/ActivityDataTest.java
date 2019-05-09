package be.doji.productivity.trambu.infrastructure.transfer;


import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityDataTest {

  @Test
  public void toDomainObject_containsTitle() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Some title");

    Assertions.assertThat(activityData.toDomainObject()).isNotNull();
    Assertions.assertThat(activityData.toDomainObject().getTitle()).isEqualTo("Some title");
  }

  @Test
  public void toDomainObject_containsCompleted() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Some title");
    activityData.setCompleted(true);

    Assertions.assertThat(activityData.toDomainObject()).isNotNull();
    Assertions.assertThat(activityData.toDomainObject().isCompleted()).isTrue();
  }
}