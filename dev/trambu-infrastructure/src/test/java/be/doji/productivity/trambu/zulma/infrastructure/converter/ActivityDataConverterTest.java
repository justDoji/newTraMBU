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
package be.doji.productivity.trambu.zulma.infrastructure.converter;

import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.zulma.domain.activity.Activity;
import be.doji.productivity.trambu.zulma.infrastructure.transfer.ActivityData;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityDataConverterTest {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired ActivityDataConverter converter;

  @Test
  public void parse_titleIsNotEmpty() {
    Activity activity = Activity.builder()
        .title("name")
        .build();

    ActivityData activityData = converter.parse(activity);
    assertThat(activityData).isNotNull();
    assertThat(activityData.getTitle()).isEqualTo("name");
  }

  @Test
  public void parse_deadlineIsNotEmpty() {
    Activity activity = Activity.builder()
        .title("name")
        .deadline("18/12/1989 12:00:00")
        .build();

    ActivityData activityData = converter.parse(activity);
    assertThat(activityData).isNotNull();
    assertThat(activityData.getDeadline()).isEqualTo("1989-12-18:12:00:00.000");
  }

  @Test
  public void parse_tagsAreNotEmpty() {
    Activity activity = Activity.builder()
        .title("name")
        .tags(Arrays.asList("tagOne", "TagTwo"))
        .build();

    ActivityData activityData = converter.parse(activity);
    assertThat(activityData).isNotNull();
    assertThat(activityData.getTags()).hasSize(2);
    assertThat(activityData.getTags().get(0).getValue()).isEqualTo("tagOne");
    assertThat(activityData.getTags().get(1).getValue()).isEqualTo("TagTwo");
  }

  @Test
  public void parse_projectsAreNotEmpty() {
    Activity activity = Activity.builder()
        .title("name")
        .projects(Arrays.asList("ProjectOne", "ProjectTwo"))
        .build();

    ActivityData activityData = converter.parse(activity);
    assertThat(activityData).isNotNull();
    assertThat(activityData.getProjects()).hasSize(2);
    assertThat(activityData.getProjects().get(0).getValue()).isEqualTo("ProjectOne");
    assertThat(activityData.getProjects().get(1).getValue()).isEqualTo("ProjectTwo");
  }

  @Test
  public void parse_referenceKey() {
    Activity activity = Activity.builder()
        .title("name")
        .referenceKey("283b6271-b513-4e89-b757-10e98c9078ea")
        .build();
    ActivityData activityData = converter.parse(activity);
    assertThat(activityData).isNotNull();
    assertThat(activityData.getReferenceKey()).isNotNull();
    assertThat(activityData.getReferenceKey()).isEqualTo("283b6271-b513-4e89-b757-10e98c9078ea");
  }

  @Test
  public void parse_comments() {
    Activity activity = Activity.builder()
        .title("name")
        .referenceKey("283b6271-b513-4e89-b757-10e98c9078ea")
        .comments("These are comments")
        .build();

    ActivityData parsedData = converter.parse(activity);
    assertThat(parsedData.getComments()).isEqualTo("These are comments");
  }

}