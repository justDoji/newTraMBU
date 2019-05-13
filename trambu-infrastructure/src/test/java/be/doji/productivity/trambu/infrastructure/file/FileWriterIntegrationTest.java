package be.doji.productivity.trambu.infrastructure.file;

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
    Assertions.assertThat(activityDatabaseRepository.findAll()).isEmpty();
    activityDatabaseRepository.save(createData());
    Assertions.assertThat(activityDatabaseRepository.findAll()).isNotEmpty();
    Assertions.assertThat(activityDatabaseRepository.findAll()).hasSize(1);

    fileWriter.writeActivtiesToFile(file);

    List<String> strings = Files.readAllLines(file.toPath());
    Assertions.assertThat(strings).isNotEmpty();
    Assertions.assertThat(strings).hasSize(1);
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