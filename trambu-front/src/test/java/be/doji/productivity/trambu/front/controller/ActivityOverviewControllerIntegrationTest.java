/**
 * GNU Affero General Public License Version 3
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package be.doji.productivity.trambu.front.controller;

import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.infrastructure.file.FileLoader;
import be.doji.productivity.trambu.infrastructure.file.FileWriter;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityOverviewControllerIntegrationTest {

  ActivityOverviewController controller;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired ActivityDatabaseRepository repository;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired FileWriter writer;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired FileLoader loader;

  @Before
  public void setUp() throws IOException, URISyntaxException {
    this.controller = new ActivityOverviewController(writer, loader, repository);
    cleanUp();
  }


  @Test
  public void init_loadsActivities() throws URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("controller/todo_read.txt").toURI());
    controller.setTodoFile(file);
    controller.clearActivities();
    repository.deleteAll();
    assertThat(controller.getActivities()).hasSize(0);

    controller.loadActivities();

    assertThat(controller.getActivities()).hasSize(3);
  }

  @Test
  public void createActivities_createsActivity() throws URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("controller/todo_write.txt").toURI());
    controller.setTodoFile(file);
    controller.clearActivities();
    Assertions.assertThat(controller.getActivities()).isEmpty();

    controller.createActivity();

    Assertions.assertThat(controller.getActivities()).hasSize(1);
  }

  @Test
  public void saveActivities_writeToRepository() throws URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("controller/todo_write.txt").toURI());
    controller.setTodoFile(file);
    controller.clearActivities();
    repository.deleteAll();
    Assertions.assertThat(controller.getActivities()).isEmpty();
    Assertions.assertThat(repository.findAll()).isEmpty();

    controller.createActivity();
    Assertions.assertThat(controller.getActivities()).hasSize(1);

    controller.saveActivities();
    Assertions.assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  public void saveActivities_writeToFile() throws URISyntaxException, IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("controller/todo_write.txt").toURI());
    controller.clearActivities();
    repository.deleteAll();
    controller.setTodoFile(file);
    Assertions.assertThat(controller.getActivities()).isEmpty();
    Assertions.assertThat(Files.readAllLines(file.toPath())).isEmpty();

    controller.createActivity();
    Assertions.assertThat(controller.getActivities()).hasSize(1);

    controller.saveActivities();
    Assertions.assertThat(Files.readAllLines(file.toPath())).hasSize(1);
  }


  private static final String LINE_ONE ="x (A) [Go to store for food?] @[food] due:2018-12-07:00:00:00.000 warningPeriod:PT24H";
  private static final String LINE_TWO ="(C) [Show the application to people] +[TraMBU] @[showoff] warningPeriod:PT24H loc:[Home]";
  private static final String LINE_THREE = "(A) 2017-10-21:14:13.000 [Hello World!] +[Overarching Project] @[Tag] @[Tag with multiple words] due:2017-12-21:16:15:00.000 uuid:283b6271-b513-4e89-b757-10e98c9078ea";

  @After
  public void cleanUp() throws IOException, URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("controller/todo_write.txt").toURI());
    Files.write(file.toPath(), new ArrayList<>());

    file = new File(classLoader.getResource("controller/todo_read.txt").toURI());
    Files.write(file.toPath(), Arrays.asList(LINE_ONE, LINE_TWO, LINE_THREE));
  }


}