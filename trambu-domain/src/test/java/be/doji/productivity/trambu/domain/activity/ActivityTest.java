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
package be.doji.productivity.trambu.domain.activity;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.doji.productivity.trambu.domain.time.TimePoint;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

public class ActivityTest {

  @Test
  public void activityBuilderBasic() {
    Activity activity = Activity.builder()
        .name("Start design practise")
        .plannedStartAt(TimePoint.fromString("01/05/2019"))
        .plannedEndAt(TimePoint.fromString("31/05/2019"))
        .build();

    Assert.assertNotNull("Activity after creation was null", activity);
    Assert.assertTrue("Created activity has wrong start date",
        activity.getAssignedTimeSlot().startsOn(TimePoint.fromString("01/05/2019")));
    Assert.assertTrue("Created activity has wrong start date",
        activity.getAssignedTimeSlot().endsOn(TimePoint.fromString("31/05/2019")));
    Assert.assertEquals("Created activity has wrong title", "Start design practise",
        activity.getName());
    assertThat(activity.isCompleted()).isFalse();
  }

  @Test
  public void builder_noParameters() {
    assertThatThrownBy(() -> Activity.builder().build())
        .hasMessage("The activity name can not be empty");
  }

  @Test
  public void builder_noName() {
    assertThatThrownBy(
        () -> Activity.builder().plannedStartAt(TimePoint.fromString("01/05/2019")).build())
        .hasMessage("The activity name can not be empty");
  }

  @Test
  public void builder_endBeforeStart() {
    assertThatThrownBy(
        () -> Activity.builder()
            .name("Invalid start/end combination")
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
        .name("Start design practise")
        .build();
    assertThat(activity.getImportance()).isNotNull();
  }

  /**
   * As a user, I want to be able to know the importance of my activities
   */
  @Test
  public void builder_has_customPriority() {
    Activity activity = Activity.builder()
        .name("Start design practise")
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
        .name("Start design practise")
        .importance(Importance.NORMAL)
        .build();
    assertThat(activity.isDeadlineExceeded()).isFalse();
  }

  @Test
  public void builder_completed() {
    Activity activity = Activity.builder()
        .name("Start design practise")
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
        .name("Start design practise")
        .importance(Importance.NORMAL)
        .deadline(TimePoint.fromString("01/01/1865"))
        .build();
    assertThat(activity.isDeadlineExceeded()).isFalse();
  }

}