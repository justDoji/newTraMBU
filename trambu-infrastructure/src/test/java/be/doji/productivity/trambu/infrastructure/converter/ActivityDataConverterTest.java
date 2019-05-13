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
package be.doji.productivity.trambu.infrastructure.converter;

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