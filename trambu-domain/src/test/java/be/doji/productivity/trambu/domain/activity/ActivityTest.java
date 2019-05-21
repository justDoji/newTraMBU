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
package be.doji.productivity.trambu.domain.activity;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import be.doji.productivity.trambu.domain.activity.Activity.ActivityBuilder;
import be.doji.productivity.trambu.domain.time.TimePoint;
import org.junit.Assert;
import org.junit.Test;

public class ActivityTest {

  @Test
  public void activityBuilderBasic() {
    Activity activity = Activity.builder()
        .title("Start design practise")
        .plannedStartAt(TimePoint.fromString("01/05/2019"))
        .plannedEndAt(TimePoint.fromString("31/05/2019"))
        .build();

    Assert.assertNotNull("Activity after creation was null", activity);
    Assert.assertTrue("Created activity has wrong start date",
        activity.getAssignedTimeSlot().startsOn(TimePoint.fromString("01/05/2019")));
    Assert.assertTrue("Created activity has wrong start date",
        activity.getAssignedTimeSlot().endsOn(TimePoint.fromString("31/05/2019")));
    Assert.assertEquals("Created activity has wrong title", "Start design practise",
        activity.getTitle());
    assertThat(activity.isCompleted()).isFalse();
  }

  @Test
  public void builder_noParameters() {
    assertThatThrownBy(() -> Activity.builder().build())
        .hasMessage("The activity title can not be empty");
  }

  @Test
  public void builder_noName() {
    assertThatThrownBy(
        () -> Activity.builder().plannedStartAt(TimePoint.fromString("01/05/2019")).build())
        .hasMessage("The activity title can not be empty");
  }

  @Test
  public void builder_endBeforeStart() {
    assertThatThrownBy(
        () -> Activity.builder()
            .title("Invalid start/end combination")
            .plannedStartAt(TimePoint.fromString("01/05/2019"))
            .plannedEndAt(TimePoint.fromString("01/01/2018"))
            .build())
        .hasMessage("The activity end date must be after the start date");
  }

  /**
   * As a user, I want to be able to know the importance of my activities
   */
  @Test
  public void builder_has_defaultPriority() {
    Activity activity = Activity.builder()
        .title("Start design practise")
        .build();
    assertThat(activity.getImportance()).isNotNull();
  }

  /**
   * As a user, I want to be able to know the importance of my activities
   */
  @Test
  public void builder_has_customPriority() {
    Activity activity = Activity.builder()
        .title("Start design practise")
        .importance(Importance.NORMAL)
        .build();
    assertThat(activity.getImportance()).isNotNull();
    assertThat(activity.getImportance()).isEqualTo(Importance.NORMAL);
  }

  /**
   * The instance in time at which the activity has to end.
   * When this is exceeded, we expect bad stuff to happen
   */
  @Test
  public void isDeadlineExceeded_default() {
    Activity activity = Activity.builder()
        .title("Start design practise")
        .importance(Importance.NORMAL)
        .build();
    assertThat(activity.isDeadlineExceeded()).isFalse();
  }

  @Test
  public void builder_completed() {
    Activity activity = Activity.builder()
        .title("Start design practise")
        .completed(true)
        .build();
    assertThat(activity.isCompleted()).isTrue();
  }

  /**
   * The instance in time at which the activity has to end.
   * When this is exceeded, we expect bad stuff to happen
   */
  @Test
  public void isDeadlineExceeded_withDeadlineIn1900() {
    Activity activity = Activity.builder()
        .title("Start design practise")
        .importance(Importance.NORMAL)
        .deadline(TimePoint.fromString("01/01/1865"))
        .build();
    assertThat(activity.isDeadlineExceeded()).isFalse();
  }

  @Test
  public void builder_deadlineFromString_empy() {
    Activity activity = Activity.builder()
        .title("Start design practise")
        .importance(Importance.NORMAL)
        .deadline("")
        .build();
    assertThat(activity.getDeadline()).isNotPresent();
  }

  @Test
  public void builder_deadlineFromString_null() {
    Activity activity = Activity.builder()
        .title("Start design practise")
        .importance(Importance.NORMAL)
        .deadline("")
        .build();
    assertThat(activity.getDeadline()).isNotPresent();
  }

  @Test
  public void builder_deadlineFromString_isConverted() {
    Activity activity = Activity.builder()
        .title("Start design practise")
        .importance(Importance.NORMAL)
        .deadline("01/01/1865")
        .build();
    assertThat(activity.getDeadline()).isPresent();
    //noinspection OptionalGetWithoutIsPresent
    assertThat(TimePoint.isSameDate(activity.getDeadline().get(),TimePoint.fromString("01/01/1865"))).isTrue();
  }

  @Test
  public void builder_deadlineFromStringThrowsException_notADate() {
    ActivityBuilder builder = Activity.builder()
        .title("Start design practise")
        .importance(Importance.NORMAL);

    Throwable thrown = catchThrowable(() -> builder.deadline("tralalalalala"));
    assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                      .hasMessageContaining("No matching parsers");
  }

}