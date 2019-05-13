package be.doji.productivity.trambu.infrastructure.file;

import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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

  @After
  public void cleanUp() {
    activityDatabaseRepository.deleteAll();
  }

}