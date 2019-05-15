package be.doji.productivity.trambu.front.converter;

import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.front.elements.ActivityModel;
import java.util.Arrays;
import org.junit.Test;

public class ActivityModelConverterTest {

  @Test
  public void parse_defaultFields() {
    Activity activity = Activity.builder()
        .title("name")
        .deadline("18/12/1989 12:00:00")
        .build();

    ActivityModel activityModel = ActivityModelConverter.parse(activity);
    assertThat(activityModel).isNotNull();
    assertThat(activityModel.getTitle()).isEqualTo("name");
    assertThat(activityModel.getDeadline()).isEqualTo("18/12/1989 12:00:00:000");
  }

  @Test
  public void parse_completed() {
    Activity activity = Activity.builder()
        .title("name")
        .completed(true)
        .build();

    ActivityModel activityModel = ActivityModelConverter.parse(activity);
    assertThat(activityModel).isNotNull();
    assertThat(activityModel.isCompleted()).isTrue();
  }

  @Test
  public void parse_tags() {
    Activity activity = Activity.builder()
        .title("name")
        .tags(Arrays.asList("tagOne", "tagTwo"))
        .build();

    ActivityModel activityModel = ActivityModelConverter.parse(activity);
    assertThat(activityModel).isNotNull();
    assertThat(activityModel.getTags()).hasSize(2);
    assertThat(activityModel.getTags().get(0)).isEqualTo("tagOne");
    assertThat(activityModel.getTags().get(1)).isEqualTo("tagTwo");
  }

  @Test
  public void parse_projects() {
    Activity activity = Activity.builder()
        .title("name")
        .projects(Arrays.asList("projectOne", "projectTwo"))
        .build();

    ActivityModel activityModel = ActivityModelConverter.parse(activity);
    assertThat(activityModel).isNotNull();
    assertThat(activityModel.getProjects()).hasSize(2);
    assertThat(activityModel.getProjects().get(0)).isEqualTo("projectOne");
    assertThat(activityModel.getProjects().get(1)).isEqualTo("projectTwo");
  }

  @Test
  public void toDomain() {
  }
}