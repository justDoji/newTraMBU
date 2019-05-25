/**
 * TraMBU - an open time management tool
 *
 *     Copyright (C) 2019  Stijn Dejongh
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     For further information on usage, or licensing, contact the author
 *     through his github profile: https://github.com/justDoji
 */
package be.doji.productivity.trambu.front.converter;

import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.time.TimePoint;
import be.doji.productivity.trambu.front.transfer.ActivityModel;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityModelConverterTest {

  @Autowired private  ActivityModelConverter converter;

  @Test
  public void parse_defaultFields() {
    Activity activity = Activity.builder()
        .title("name")
        .deadline("18/12/1989 12:00:00")
        .build();

    ActivityModel activityModel = converter.parse(activity);
    assertThat(activityModel).isNotNull();
    assertThat(activityModel.getTitle()).isEqualTo("name");
    Date myBirthday = new GregorianCalendar(1989, Calendar.DECEMBER, 18, 12, 0, 0).getTime();
    assertThat(activityModel.getDeadline()).isEqualTo(myBirthday);
  }

  @Test
  public void parse_completed() {
    Activity activity = Activity.builder()
        .title("name")
        .completed(true)
        .build();

    ActivityModel activityModel = converter.parse(activity);
    assertThat(activityModel).isNotNull();
    assertThat(activityModel.isCompleted()).isTrue();
  }

  @Test
  public void parse_tags() {
    Activity activity = Activity.builder()
        .title("name")
        .tags(Arrays.asList("tagOne", "tagTwo"))
        .build();

    ActivityModel activityModel = converter.parse(activity);
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

    ActivityModel activityModel = converter.parse(activity);
    assertThat(activityModel).isNotNull();
    assertThat(activityModel.getProjects()).hasSize(2);
    assertThat(activityModel.getProjects().get(0)).isEqualTo("projectOne");
    assertThat(activityModel.getProjects().get(1)).isEqualTo("projectTwo");
  }

  @Test
  public void toDomain_convertsTitle() {
    ActivityModel activityModel = new ActivityModel();
    activityModel.setTitle("Some kind of title");

    Activity activity = converter.toDomain(activityModel);
    Assertions.assertThat(activity).isNotNull();
    Assertions.assertThat(activity.getTitle()).isEqualTo("Some kind of title");
    Assertions.assertThat(activity.isCompleted()).isFalse();
  }

  @Test
  public void toDomain_convertsCompleted() {
    ActivityModel activityModel = new ActivityModel();
    activityModel.setTitle("Some kind of title");
    activityModel.setCompleted(true);

    Activity activity = converter.toDomain(activityModel);
    Assertions.assertThat(activity).isNotNull();
    Assertions.assertThat(activity.isCompleted()).isTrue();
  }

  @Test
  public void toDomain_convertsDeadline() {
    ActivityModel activityModel = new ActivityModel();
    activityModel.setTitle("Some kind of title");
    Date myBirthday = new GregorianCalendar(1989, Calendar.DECEMBER, 18, 12, 0, 0).getTime();
    activityModel.setDeadline(myBirthday);

    Activity activity = converter.toDomain(activityModel);
    Assertions.assertThat(activity.getDeadline()).isPresent();
    Assertions.assertThat(TimePoint
        .isSameDate(activity.getDeadline().get(), TimePoint.fromString("18/12/1989 12:00:00:000")))
        .isTrue();
  }

  @Test
  public void toDomain_convertsTags() {
    ActivityModel activityModel = new ActivityModel();
    activityModel.setTitle("Some kind of title");
    activityModel.setTags(Arrays.asList("TagOne", "TagTwo"));

    Activity activity = converter.toDomain(activityModel);
    Assertions.assertThat(activity.getTags()).isNotNull();
    Assertions.assertThat(activity.getTags()).isNotEmpty();
    Assertions.assertThat(activity.getTags().get(0)).isEqualTo("TagOne");
    Assertions.assertThat(activity.getTags().get(1)).isEqualTo("TagTwo");
  }

  @Test
  public void toDomain_convertsProjects() {
    ActivityModel activityModel = new ActivityModel();
    activityModel.setTitle("Some kind of title");
    activityModel.setProjects(Arrays.asList("ProjectOne", "ProjectTwo"));

    Activity activity = converter.toDomain(activityModel);
    Assertions.assertThat(activity.getProjects()).isNotNull();
    Assertions.assertThat(activity.getProjects()).isNotEmpty();
    Assertions.assertThat(activity.getProjects().get(0)).isEqualTo("ProjectOne");
    Assertions.assertThat(activity.getProjects().get(1)).isEqualTo("ProjectTwo");
  }
}