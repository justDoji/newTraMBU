package be.doji.productivity.trambu.infrastructure.transfer;


import be.doji.productivity.trambu.domain.time.TimePoint;
import org.assertj.core.api.Assertions;
import org.junit.Test;

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

  @Test
  public void toDomainObject_containsDeadline() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Implement deadlines in core");
    activityData.setDeadline("09/05/2019");

    Assertions.assertThat(activityData.toDomainObject()).isNotNull();
    Assertions.assertThat(TimePoint.isSameDate(activityData.toDomainObject().getDeadline().get(),
        TimePoint.fromString("09/05/2019"))).isTrue();

  }
}