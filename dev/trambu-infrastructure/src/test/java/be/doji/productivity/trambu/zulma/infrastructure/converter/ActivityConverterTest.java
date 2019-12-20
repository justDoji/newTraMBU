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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.doji.productivity.trambu.zulma.domain.activity.Activity;
import be.doji.productivity.trambu.zulma.domain.time.TimePoint;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityConverterTest {

  private static final String ACTIVITY_DATA_LINE = "(A) 2017-10-21:14:13.000 [TaskTitle] +[Overarching Project] @[Tag] @[Tag with multiple words] due:2017-12-21:16:15:00.000 uuid:[283b6271-b513-4e89-b757-10e98c9078ea]";
  private static final String COMPLETED_ACTIVITY = "X (B) [Buy thunderbird plugin license]";
  private static final String COMPLETED_ACTIVITY_LOWERCASE = "x (B) [Buy thunderbird plugin license]";

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired private ActivityConverter activityConverter;

  @Test
  public void mapStringToActivity_givesOutput() {
    Activity parsedActivity = activityConverter.parse(ACTIVITY_DATA_LINE);

    assertThat(parsedActivity).isNotNull();
  }

  @Test
  public void mapStringToActivity_emptyString() {
    assertThatThrownBy(() -> activityConverter.parse(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Failure during parsing: empty String or null value not allowed");
  }

  @Test
  public void mapStringToActivity_null() {
    assertThatThrownBy(() -> activityConverter.parse(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Failure during parsing: empty String or null value not allowed");
  }

  // Data Parsing test

  @Test
  public void parse_ActivityCompleted_upperCaseIndicator() {
    Activity parsedData = activityConverter.parse(COMPLETED_ACTIVITY);
    assertThat(parsedData.isCompleted()).isTrue();
  }

  @Test
  public void parse_ActivityCompleted_lowerCaseIndicator() {
    Activity parsedData = activityConverter.parse(COMPLETED_ACTIVITY_LOWERCASE);
    assertThat(parsedData.isCompleted()).isTrue();
  }

  @Test
  public void parse_activityTitle_singleWordTitle() {
    Activity parsedActivity = activityConverter.parse(ACTIVITY_DATA_LINE);
    assertThat(parsedActivity.getTitle()).isEqualTo("TaskTitle");
  }

  @Test
  public void parse_activityTitle_multipleWordTitle() {
    Activity parsedActivity = activityConverter.parse(COMPLETED_ACTIVITY);
    assertThat(parsedActivity.getTitle()).isEqualTo("Buy thunderbird plugin license");
  }

  @Test
  public void parse_activityDeadline() {
    Activity parsedActivity = activityConverter.parse(ACTIVITY_DATA_LINE);
    assertThat(parsedActivity.getDeadline()).isPresent();
    assertThat(TimePoint.isSameDate(TimePoint.fromString("2017-12-21:16:15:00.000"),
        parsedActivity.getDeadline().get())).isTrue();
  }

  @Test
  public void parse_activityTags() {
    Activity parsedActivity = activityConverter.parse(ACTIVITY_DATA_LINE);
    assertThat(parsedActivity.getTags()).isNotEmpty();
    assertThat(parsedActivity.getTags()).hasSize(2);
    assertThat(parsedActivity.getTags().get(0)).isEqualTo("Tag");
    assertThat(parsedActivity.getTags().get(1)).isEqualTo("Tag with multiple words");
  }

  @Test
  public void parse_activityTags_noTags() {
    Activity parsedActivity = activityConverter.parse(COMPLETED_ACTIVITY);

    assertThat(parsedActivity.getTags()).isEmpty();
  }

  @Test
  public void parse_activityProject() {
    Activity parsedActivity = activityConverter.parse(ACTIVITY_DATA_LINE);
    assertThat(parsedActivity.getProjects()).isNotEmpty();
    assertThat(parsedActivity.getProjects()).hasSize(1);
    assertThat(parsedActivity.getProjects().get(0)).isEqualTo("Overarching Project");
  }

  @Test
  public void parse_activityProject_noProject() {
    Activity parsedActivity = activityConverter.parse(COMPLETED_ACTIVITY);
    assertThat(parsedActivity.getProjects()).isEmpty();
  }

  @Test
  public void parse_activityContainsUUID() {
    Activity parsed = activityConverter.parse(ACTIVITY_DATA_LINE);
    assertThat(parsed.getReferenceKey()).isEqualTo("283b6271-b513-4e89-b757-10e98c9078ea");
  }

  @Test
  public void write_activityTitle() {
    Activity activity = Activity.builder()
        .title("name")
        .build();

    String toString = activityConverter.write(activity);
    assertThat(toString).startsWith("[name]");
  }

  @Test
  public void write_completed() {
    Activity activity = Activity.builder()
        .title("name")
        .completed(true)
        .build();

    String toString = activityConverter.write(activity);
    assertThat(toString).startsWith("x [name]");
  }

  @Test
  public void write_activityTitle_multipleWords() {
    Activity activity = Activity.builder()
        .title("name is @ cool g_y")
        .build();

    String toString = activityConverter.write(activity);
    assertThat(toString).contains("[name is @ cool g_y]");
  }

  @Test
  public void write_deadline() {
    Activity activity = Activity.builder()
        .title("name")
        .deadline("2017-10-21:14:13:00.000")
        .build();

    String toString = activityConverter.write(activity);
    assertThat(toString).startsWith("[name] due:2017-10-21:14:13:00.000");
  }

  @Test
  public void write_tags() {
    Activity activity = Activity.builder()
        .title("name")
        .tags(Arrays.asList("tagOne", "tagTwo"))
        .build();

    String toString = activityConverter.write(activity);
    assertThat(toString).startsWith("[name] @[tagOne] @[tagTwo]");
  }

  @Test
  public void write_projects() {
    Activity activity = Activity.builder()
        .title("name")
        .projects(Arrays.asList("projectOne", "projectTwo"))
        .build();

    String toString = activityConverter.write(activity);
    assertThat(toString).startsWith("[name] +[projectOne] +[projectTwo]");
  }
}