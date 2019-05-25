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
package be.doji.productivity.trambu.infrastructure.transfer;


import be.doji.productivity.trambu.domain.time.TimePoint;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ActivityDataTest {

  @Test
  public void toDomainObject_containsTitle() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Some title");

    Assertions.assertThat(activityData.toDomainObject()).isNotNull();
    Assertions.assertThat(activityData.toDomainObject().getTitle()).isEqualTo("Some title");
  }

  @Test
  public void toDomainObject_containsCompleted() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Some title");
    activityData.setCompleted(true);

    Assertions.assertThat(activityData.toDomainObject()).isNotNull();
    Assertions.assertThat(activityData.toDomainObject().isCompleted()).isTrue();
  }

  @Test
  public void toDomainObject_containsDeadline() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Implement deadlines in core");
    activityData.setDeadline("09/05/2019");

    Assertions.assertThat(activityData.toDomainObject()).isNotNull();
    Assertions.assertThat(activityData.toDomainObject().getDeadline()).isPresent();
    Assertions.assertThat(TimePoint.isSameDate(activityData.toDomainObject().getDeadline().get(),
        TimePoint.fromString("09/05/2019"))).isTrue();
  }

  @Test
  public void toDomainObject_containsTags() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Implement deadlines in core");
    List<ActivityTagData> tags = new ArrayList<>();
    tags.add(new ActivityTagData("Test"));
    activityData.setTags(tags);

    Assertions.assertThat(activityData.toDomainObject()).isNotNull();
    Assertions.assertThat(activityData.toDomainObject().getTags()).isNotEmpty();
    Assertions.assertThat(activityData.toDomainObject().getTags()).hasSize(1);
    Assertions.assertThat(activityData.toDomainObject().getTags().get(0)).isEqualTo("Test");
  }

  @Test
  public void toDomainObject_containsProjects() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Implement deadlines in core");
    List<ActivityProjectData> projects = new ArrayList<>();
    projects.add(new ActivityProjectData("Test"));
    activityData.setProjects(projects);

    Assertions.assertThat(activityData.toDomainObject()).isNotNull();
    Assertions.assertThat(activityData.toDomainObject().getProjects()).isNotEmpty();
    Assertions.assertThat(activityData.toDomainObject().getProjects()).hasSize(1);
    Assertions.assertThat(activityData.toDomainObject().getProjects().get(0)).isEqualTo("Test");
  }

}