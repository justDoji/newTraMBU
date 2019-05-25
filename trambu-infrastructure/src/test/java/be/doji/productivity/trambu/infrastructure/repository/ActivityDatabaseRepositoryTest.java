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
package be.doji.productivity.trambu.infrastructure.repository;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatCode;

import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityTagData;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityDatabaseRepositoryTest {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  ActivityDatabaseRepository repository;

  @Test
  public void repository_CanBeAutowired() {
    Assertions.assertThat(repository).isNotNull();
    Assertions.assertThat(repository.findAll()).hasSize(0);
  }

  @Test
  public void repository_saveEntity() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Save me");
    activityData.setCompleted(true);

    ActivityData saved = repository.save(activityData);

    Assertions.assertThat(repository.findById(saved.getId())).isPresent();

    ActivityData savedActivity = repository.findById(saved.getId()).get();
    Assertions.assertThat(savedActivity.getTitle()).isEqualTo("Save me");
    Assertions.assertThat(savedActivity.isCompleted()).isTrue();
  }

  @Test
  public void addActivity_withTags() {
    ActivityData activityData = new ActivityData();

    List<ActivityTagData> tagList = new ArrayList<>();
    tagList.add(new ActivityTagData("TestTag", activityData));
    tagList.add(new ActivityTagData("TestTagTwo", activityData));

    activityData.setTitle("Save me");
    activityData.setCompleted(true);
    activityData.setTags(tagList);

    assertThat(repository.findAll()).isEmpty();

    repository.save(activityData);

    assertThat(repository.findAll()).hasSize(1);
    assertThat(repository.findAll().get(0).getTitle()).isEqualTo("Save me");
    assertThat(repository.findAll().get(0).getTags()).isNotEmpty();
    assertThat(repository.findAll().get(0).getTags()).hasSize(2);
  }

  @Test
  public void addActivity_withTimeLog() {
    ActivityData data = new ActivityData();
    data.setTitle("Activity with timelog");
    List<LogPointData> timeLogs = new ArrayList<>();
    LogPointData logPoint = new LogPointData("24-05-2019 20:00:00", "24-05-2019 21:14:00");
    timeLogs.add(logPoint);
    data.setTimelogs(timeLogs);

    assertThat(repository.findAll()).hasSize(0);

    repository.save(data);

    assertThat(repository.findAll()).hasSize(1);
    ActivityData savedData = repository.findAll().get(0);
    assertThat(savedData.getTitle()).isEqualTo("Activity with timelog");
    assertThat(savedData.getTimelogs()).isNotEmpty();
    assertThat(savedData.getTimelogs()).hasSize(1);
    assertThat(savedData.getTimelogs().get(0)).isNotNull();
    assertThat(savedData.getTimelogs().get(0).getStart()).isEqualTo("24-05-2019 20:00:00");
    assertThat(savedData.getTimelogs().get(0).getEnd()).isEqualTo("24-05-2019 21:14:00");
  }

  @After
  public void cleanUp() {
    repository.deleteAll();
  }


}