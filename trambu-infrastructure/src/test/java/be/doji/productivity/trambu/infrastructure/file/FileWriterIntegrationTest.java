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
package be.doji.productivity.trambu.infrastructure.file;

import static org.assertj.core.api.Assertions.*;

import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityTagData;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
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
public class FileWriterIntegrationTest {

  private static final String FILE_PATH = "writer/blank.txt";

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired private ActivityDatabaseRepository activityDatabaseRepository;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired private FileWriter fileWriter;

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
    File file = new File(getClass().getClassLoader().getResource(FILE_PATH).toURI());
    Files.write(file.toPath(), new ArrayList<>());
  }
}