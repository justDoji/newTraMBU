/**
 * GNU Affero General Public License Version 3
 *
 * Copyright (c) 2019 Stijn Dejongh
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
 * If not, see <http://www.gnu.org/licenses/>.
 */
package be.doji.productivity.trambu.infrastructure.file;

import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileLoaderIntegrationTest {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired private ActivityDatabaseRepository activityDatabaseRepository;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired private FileLoader fileLoader;

  @Test
  public void loadTodoFileContents_FileWithActivities() throws IOException, URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("reader/todo_test.txt").toURI());
    Assertions.assertThat(activityDatabaseRepository.findAll()).isEmpty();

    fileLoader.loadTodoFileActivities(file);

    Assertions.assertThat(activityDatabaseRepository.findAll()).isNotEmpty();
    Assertions.assertThat(activityDatabaseRepository.findAll()).hasSize(3);
  }

  @Test
  public void loadTimeLogFileContents_FileWithTimeLogs() throws URISyntaxException {
    // Prep file
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("reader/timelog_test.txt").toURI());
    Assertions.assertThat(activityDatabaseRepository.findAll()).isEmpty();

    //Add activity with ID 123 to activity repository
    ActivityData rootActivity = new ActivityData();
    rootActivity.setId(123L);
    rootActivity.setTitle("Implement timelogs");
    activityDatabaseRepository.save(rootActivity);
    Assertions.assertThat(activityDatabaseRepository.findAll()).hasSize(1);

    fileLoader.loadTimeLogFile(file);

    // Assert expected outcome
    Assertions.assertThat(activityDatabaseRepository.findAll()).hasSize(1);
    List<LogPointData> expectedTimeLogs = activityDatabaseRepository.findAll().get(0).getTimelogs();
    Assertions.assertThat(expectedTimeLogs).isNotNull();
    Assertions.assertThat(expectedTimeLogs).isNotEmpty();
    Assertions.assertThat(expectedTimeLogs).hasSize(3);

  }

  @After
  public void cleanUp() {
    activityDatabaseRepository.deleteAll();
  }

}