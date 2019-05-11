/**
 * MIT License
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package be.doji.productivity.trambu.infrastructure.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import org.junit.Test;

public class ActivityParserTest {

  private static final String ACTIVITY_DATA_LINE = "(A) 2017-10-21:14:13.000 [TaskTitle] +[Overarching Project] @[Tag] @[Tag with multiple words] due:2017-12-21:16:15:00.000 uuid:283b6271-b513-4e89-b757-10e98c9078ea";
  private static final String COMPLETED_ACTIVITY = "X (B) [Buy thunderbird plugin license]";
  private static final String COMPLETED_ACTIVITY_LOWERCASE = "x (B) [Buy thunderbird plugin license]";


  @Test
  public void mapStringToActivity_givesOutput() {
    ActivityData parsedActivity = ActivityParser.parse(ACTIVITY_DATA_LINE);

    assertThat(parsedActivity).isNotNull();
  }

  @Test
  public void mapStringToActivity_emptyString() {
    assertThatThrownBy(() -> ActivityParser.parse(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Failure during parsing: empty String or null value not allowed");
  }

  @Test
  public void mapStringToActivity_null() {
    assertThatThrownBy(() -> ActivityParser.parse(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Failure during parsing: empty String or null value not allowed");
  }

  // Data Parsing test

  @Test
  public void parse_ActivityCompleted_upperCaseIndicator() {
    ActivityData parsedData = ActivityParser.parse(COMPLETED_ACTIVITY);
    assertThat(parsedData.isCompleted()).isTrue();
  }

  @Test
  public void parse_ActivityCompleted_lowerCaseIndicator() {
    ActivityData parsedData = ActivityParser.parse(COMPLETED_ACTIVITY_LOWERCASE);
    assertThat(parsedData.isCompleted()).isTrue();
  }

  @Test
  public void parse_activityTitle_singleWordTitle() {
    ActivityData parsedActivity = ActivityParser.parse(ACTIVITY_DATA_LINE);
    assertThat(parsedActivity.getTitle()).isEqualTo("TaskTitle");
  }

  @Test
  public void parse_activityTitle_multipleWordTitle() {
    ActivityData parsedActivity = ActivityParser.parse(COMPLETED_ACTIVITY);
    assertThat(parsedActivity.getTitle()).isEqualTo("Buy thunderbird plugin license");
  }

  @Test
  public void parse_activityDeadline() {
    ActivityData parsedActivity = ActivityParser.parse(ACTIVITY_DATA_LINE);
    assertThat(parsedActivity.getDeadline()).isEqualTo("2017-12-21:16:15:00.000");
  }

  @Test
  public void parse_activityTags() {
    ActivityData parsedActivity = ActivityParser.parse(ACTIVITY_DATA_LINE);
    assertThat(parsedActivity.getTags()).isNotEmpty();
    assertThat(parsedActivity.getTags()).hasSize(2);
    assertThat(parsedActivity.getTags().get(0).getValue()).isEqualTo("Tag");
    assertThat(parsedActivity.getTags().get(1).getValue()).isEqualTo("Tag with multiple words");
  }

  @Test
  public void parse_activityTags_noTags() {
    ActivityData parsedActivity = ActivityParser.parse(COMPLETED_ACTIVITY);

    assertThat(parsedActivity.getTags()).isEmpty();
  }

  @Test
  public void parse_activityProject() {
    ActivityData parsedActivity = ActivityParser.parse(ACTIVITY_DATA_LINE);
    assertThat(parsedActivity.getProjects()).isNotEmpty();
    assertThat(parsedActivity.getProjects()).hasSize(1);
    assertThat(parsedActivity.getProjects().get(0).getValue()).isEqualTo("Overarching Project");
  }

  @Test
  public void parse_activityProject_noProject() {
    ActivityData parsedActivity = ActivityParser.parse(COMPLETED_ACTIVITY);
    assertThat(parsedActivity.getProjects()).isEmpty();
  }
}