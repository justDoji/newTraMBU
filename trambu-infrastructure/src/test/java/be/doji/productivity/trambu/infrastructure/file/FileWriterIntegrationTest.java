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
package be.doji.productivity.trambu.infrastructure.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityTagData;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileWriterIntegrationTest {

  private static final String FILE_PATH = "writer/blank.txt";
  private static final String FILE_PATH_LOGS = "writer/blank_logs.txt";

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private ActivityDatabaseRepository activityDatabaseRepository;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private FileWriter fileWriter;

  @Test
  public void writeTodoFileContents_FileWithActivities() throws IOException, URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(FILE_PATH).toURI());
    assertThat(activityDatabaseRepository.findAll()).isEmpty();
    activityDatabaseRepository.save(createData());
    assertThat(activityDatabaseRepository.findAll()).isNotEmpty();
    assertThat(activityDatabaseRepository.findAll()).hasSize(1);

    fileWriter.writeActivtiesToFile(file);

    List<String> strings = Files.readAllLines(file.toPath());
    assertThat(strings).isNotEmpty();
    assertThat(strings).hasSize(1);
  }

  @Test
  public void writeTimeLogFileContents_FileIsWritten() throws IOException, URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(FILE_PATH_LOGS).toURI());

    assertThat(activityDatabaseRepository.findAll()).isEmpty();
    assertThat(Files.readAllLines(file.toPath())).isEmpty();

    LogPointData logPoint = new LogPointData("2018-05-26:13:40:00.000", "2018-05-26:14:30:00.000");
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Activity with logpoint");
    activityData.setCompleted(false);
    activityData.addTimelog(logPoint);

    activityDatabaseRepository.save(activityData);
    assertThat(activityDatabaseRepository.findAll()).hasSize(1);

    fileWriter.writeTimeLogsToFile(file);

    assertThat(Files.readAllLines(file.toPath())).hasSize(1);
  }

  @Test
  public void writeTimeLogFileContents_throwsException_IfFileDoesNotExist() {
    File file = new File("SOME/PATH/THAT/DOES_NOT_EXIST.txt");

    assertThatThrownBy(() -> fileWriter.writeTimeLogsToFile(file))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Error while opening output file")
        .hasMessageContaining("DOES_NOT_EXIST.txt");
  }

  private ActivityData createData() {
    ActivityData activityData = new ActivityData();

    List<ActivityTagData> tagList = new ArrayList<>();
    tagList.add(new ActivityTagData("TestTag", activityData));
    tagList.add(new ActivityTagData("TestTagTwo", activityData));

    activityData.setTitle("Save me");
    activityData.setCompleted(true);
    activityData.setTags(tagList);
    return activityData;
  }

  @After
  public void cleanUp() throws IOException, URISyntaxException {
    activityDatabaseRepository.deleteAll();
    File todoFile = new File(getClass().getClassLoader().getResource(FILE_PATH).toURI());
    Files.write(todoFile.toPath(), new ArrayList<>());

    File logFile = new File(getClass().getClassLoader().getResource(FILE_PATH_LOGS).toURI());
    Files.write(logFile.toPath(), new ArrayList<>());
  }
}