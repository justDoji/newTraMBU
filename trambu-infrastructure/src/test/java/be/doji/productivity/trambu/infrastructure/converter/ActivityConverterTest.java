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
package be.doji.productivity.trambu.infrastructure.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.time.TimePoint;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ActivityConverterTest {

  private static final String ACTIVITY_DATA_LINE = "(A) 2017-10-21:14:13.000 [TaskTitle] +[Overarching Project] @[Tag] @[Tag with multiple words] due:2017-12-21:16:15:00.000 uuid:283b6271-b513-4e89-b757-10e98c9078ea";
  private static final String COMPLETED_ACTIVITY = "X (B) [Buy thunderbird plugin license]";
  private static final String COMPLETED_ACTIVITY_LOWERCASE = "x (B) [Buy thunderbird plugin license]";

  @Test
  public void mapStringToActivity_givesOutput() {
    Activity parsedActivity = ActivityConverter.parse(ACTIVITY_DATA_LINE);

    assertThat(parsedActivity).isNotNull();
  }

  @Test
  public void mapStringToActivity_emptyString() {
    assertThatThrownBy(() -> ActivityConverter.parse(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Failure during parsing: empty String or null value not allowed");
  }

  @Test
  public void mapStringToActivity_null() {
    assertThatThrownBy(() -> ActivityConverter.parse(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Failure during parsing: empty String or null value not allowed");
  }

  // Data Parsing test

  @Test
  public void parse_ActivityCompleted_upperCaseIndicator() {
    Activity parsedData = ActivityConverter.parse(COMPLETED_ACTIVITY);
    assertThat(parsedData.isCompleted()).isTrue();
  }

  @Test
  public void parse_ActivityCompleted_lowerCaseIndicator() {
    Activity parsedData = ActivityConverter.parse(COMPLETED_ACTIVITY_LOWERCASE);
    assertThat(parsedData.isCompleted()).isTrue();
  }

  @Test
  public void parse_activityTitle_singleWordTitle() {
    Activity parsedActivity = ActivityConverter.parse(ACTIVITY_DATA_LINE);
    assertThat(parsedActivity.getTitle()).isEqualTo("TaskTitle");
  }

  @Test
  public void parse_activityTitle_multipleWordTitle() {
    Activity parsedActivity = ActivityConverter.parse(COMPLETED_ACTIVITY);
    assertThat(parsedActivity.getTitle()).isEqualTo("Buy thunderbird plugin license");
  }

  @Test
  public void parse_activityDeadline() {
    Activity parsedActivity = ActivityConverter.parse(ACTIVITY_DATA_LINE);
    assertThat(parsedActivity.getDeadline()).isPresent();
    assertThat(TimePoint.isSameDate(TimePoint.fromString("2017-12-21:16:15:00.000"),
        parsedActivity.getDeadline().get())).isTrue();
  }

  @Test
  public void parse_activityTags() {
    Activity parsedActivity = ActivityConverter.parse(ACTIVITY_DATA_LINE);
    assertThat(parsedActivity.getTags()).isNotEmpty();
    assertThat(parsedActivity.getTags()).hasSize(2);
    assertThat(parsedActivity.getTags().get(0)).isEqualTo("Tag");
    assertThat(parsedActivity.getTags().get(1)).isEqualTo("Tag with multiple words");
  }

  @Test
  public void parse_activityTags_noTags() {
    Activity parsedActivity = ActivityConverter.parse(COMPLETED_ACTIVITY);

    assertThat(parsedActivity.getTags()).isEmpty();
  }

  @Test
  public void parse_activityProject() {
    Activity parsedActivity = ActivityConverter.parse(ACTIVITY_DATA_LINE);
    assertThat(parsedActivity.getProjects()).isNotEmpty();
    assertThat(parsedActivity.getProjects()).hasSize(1);
    assertThat(parsedActivity.getProjects().get(0)).isEqualTo("Overarching Project");
  }

  @Test
  public void parse_activityProject_noProject() {
    Activity parsedActivity = ActivityConverter.parse(COMPLETED_ACTIVITY);
    assertThat(parsedActivity.getProjects()).isEmpty();
  }

  @Test
  public void write_activityTitle() {
    Activity activity = Activity.builder()
        .title("name")
        .build();

    String toString = ActivityConverter.write(activity);
    Assertions.assertThat(toString).startsWith("[name]");
  }

  @Test
  public void write_completed() {
    Activity activity = Activity.builder()
        .title("name")
        .completed(true)
        .build();

    String toString = ActivityConverter.write(activity);
    Assertions.assertThat(toString).startsWith("x [name]");
  }

  @Test
  public void write_activityTitle_multipleWords() {
    Activity activity = Activity.builder()
        .title("name is @ cool g_y")
        .build();

    String toString = ActivityConverter.write(activity);
    Assertions.assertThat(toString).contains("[name is @ cool g_y]");
  }

  @Test
  public void write_deadline() {
    Activity activity = Activity.builder()
        .title("name")
        .deadline("2017-10-21:14:13:00.000")
        .build();

    String toString = ActivityConverter.write(activity);
    Assertions.assertThat(toString).startsWith("[name] due:2017-10-21:14:13:00.000");
  }

  @Test
  public void write_tags() {
    Activity activity = Activity.builder()
        .title("name")
        .tags(Arrays.asList("tagOne", "tagTwo"))
        .build();

    String toString = ActivityConverter.write(activity);
    Assertions.assertThat(toString).startsWith("[name] @[tagOne] @[tagTwo]");
  }

  @Test
  public void write_projects() {
    Activity activity = Activity.builder()
        .title("name")
        .projects(Arrays.asList("projectOne", "projectTwo"))
        .build();

    String toString = ActivityConverter.write(activity);
    Assertions.assertThat(toString).startsWith("[name] +[projectOne] +[projectTwo]");
  }
}