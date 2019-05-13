package be.doji.productivity.trambu.infrastructure.converter;

import static org.junit.Assert.*;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ActivityDataConverterTest {

  @Test
  public void parse_titleIsNotEmpty() {
    Activity activity = Activity.builder()
        .title("name")
        .build();

    ActivityData activityData = ActivityDataConverter.parse(activity);
    Assertions.assertThat(activityData).isNotNull();
    Assertions.assertThat(activityData.getTitle()).isEqualTo("name");
  }

  @Test
  public void parse_deadlineIsNotEmpty() {
    Activity activity = Activity.builder()
        .title("name")
        .deadline("18/12/1989 12:00:00")
        .build();

    ActivityData activityData = ActivityDataConverter.parse(activity);
    Assertions.assertThat(activityData).isNotNull();
    Assertions.assertThat(activityData.getDeadline()).isEqualTo("1989-12-18:12:00:00.000");
  }

  @Test
  public void parse_tagsAreNotEmpty() {
    Activity activity = Activity.builder()
        .title("name")
        .tags(Arrays.asList("tagOne", "TagTwo"))
        .build();

    ActivityData activityData = ActivityDataConverter.parse(activity);
    Assertions.assertThat(activityData).isNotNull();
    Assertions.assertThat(activityData.getTags()).hasSize(2);
    Assertions.assertThat(activityData.getTags().get(0).getValue()).isEqualTo("tagOne");
    Assertions.assertThat(activityData.getTags().get(1).getValue()).isEqualTo("TagTwo");
  }

  @Test
  public void parse_projectsAreNotEmpty() {
    Activity activity = Activity.builder()
        .title("name")
        .projects(Arrays.asList("ProjectOne", "ProjectTwo"))
        .build();

    ActivityData activityData = ActivityDataConverter.parse(activity);
    Assertions.assertThat(activityData).isNotNull();
    Assertions.assertThat(activityData.getProjects()).hasSize(2);
    Assertions.assertThat(activityData.getProjects().get(0).getValue()).isEqualTo("ProjectOne");
    Assertions.assertThat(activityData.getProjects().get(1).getValue()).isEqualTo("ProjectTwo");
  }

}