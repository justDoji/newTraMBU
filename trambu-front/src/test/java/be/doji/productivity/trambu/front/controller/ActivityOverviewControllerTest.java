package be.doji.productivity.trambu.front.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URISyntaxException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityOverviewControllerTest {

  @Autowired ActivityOverviewController controller;

  @Test
  public void init_loadsActivities() throws URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("controller/todo_read.txt").toURI());
    controller.setTodoFile(file);

    controller.init();

    assertThat(controller.getActivities()).hasSize(3);
  }

}