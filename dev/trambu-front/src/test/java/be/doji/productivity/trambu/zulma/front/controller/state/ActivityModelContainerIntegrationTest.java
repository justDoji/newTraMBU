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
package be.doji.productivity.trambu.zulma.front.controller.state;

import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.zulma.front.converter.ActivityModelConverter;
import be.doji.productivity.trambu.zulma.front.converter.TimeLogConverter;
import be.doji.productivity.trambu.zulma.front.model.ActivityModel;
import be.doji.productivity.trambu.zulma.front.model.TimeLogModel;
import be.doji.productivity.trambu.zulma.infrastructure.file.FileLoader;
import be.doji.productivity.trambu.zulma.infrastructure.file.FileWriter;
import be.doji.productivity.trambu.zulma.infrastructure.repository.ActivityDatabaseRepository;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityModelContainerIntegrationTest {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  ActivityDatabaseRepository repository;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  FileWriter writer;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  FileLoader loader;

  @Autowired private ActivityModelConverter modelConverter;

  private ActivityModelContainer container;

  private static final String LINE_ONE = "x (A) [Go to store for food?] @[food] due:2018-12-07:00:00:00.000 warningPeriod:PT24H";
  private static final String LINE_TWO = "(C) [Show the application to people] +[TraMBU] @[showoff] warningPeriod:PT24H loc:[Home]";
  private static final String LINE_THREE = "(A) 2017-10-21:14:13.000 [Hello World!] +[Overarching Project] @[Tag] @[Tag with multiple words] due:2017-12-21:16:15:00.000 uuid:283b6271-b513-4e89-b757-10e98c9078ea";
  private SimpleDateFormat dateFormat;
  private ListAppender<ILoggingEvent> logAppender;

  @Before
  public void setUp() throws Exception {
    this.dateFormat = new SimpleDateFormat(TimeLogConverter.LOG_DATE_PATTERN);
    this.container = new ActivityModelContainer(writer, loader,
        modelConverter, repository);
    setUpLogger();
    reset();
  }

  private void setUpLogger() {
    // get Logback Logger
    Logger fooLogger = (Logger) LoggerFactory.getLogger(ActivityModelContainer.class);

    // create and start a ListAppender
    this.logAppender = new ListAppender<>();
    logAppender.start();

    // add the appender to the logger
    fooLogger.addAppender(logAppender);
  }

  private void reset() throws URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    File todofile = new File(classLoader.getResource("controller/todo_write.txt").toURI());
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    container.setTodoFile(todofile);
    container.setTimeFile(timefile);

    container.reset();

    assertThat(container.getActivities()).isEmpty();
    Assertions.assertThat(repository.findAll()).isEmpty();
  }

  @After
  public void cleanUp() throws IOException, URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("controller/todo_write.txt").toURI());
    Files.write(file.toPath(), new ArrayList<>());

    File timeFile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    Files.write(timeFile.toPath(), new ArrayList<>());

    file = new File(classLoader.getResource("controller/todo_read.txt").toURI());
    Files.write(file.toPath(), Arrays.asList(LINE_ONE, LINE_TWO, LINE_THREE));
  }

  /* TEST SUITE */

  @Test
  public void init_loadsActivities() throws URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("controller/todo_read.txt").toURI());
    container.setTodoFile(file);
    container.clearActivities();
    repository.deleteAll();
    assertThat(container.getActivities()).hasSize(0);

    container.loadActivities();

    assertThat(container.getActivities()).hasSize(3);
  }

  @Test
  public void createActivities_createsActivity() throws URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("controller/todo_write.txt").toURI());
    container.setTodoFile(file);
    container.clearActivities();
    assertThat(container.getActivities()).isEmpty();

    container.createActivity();

    assertThat(container.getActivities()).hasSize(1);
  }

  @Test
  public void saveActivities_writeToRepository() {
    container.createActivity();
    assertThat(container.getActivities()).hasSize(1);

    container.saveActivities();
    Assertions.assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  public void saveActivities_writeToFile() throws URISyntaxException, IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("controller/todo_write.txt").toURI());
    container.clearActivities();
    repository.deleteAll();
    container.setTodoFile(file);
    assertThat(container.getActivities()).isEmpty();
    assertThat(Files.readAllLines(file.toPath())).isEmpty();

    container.createActivity();
    assertThat(container.getActivities()).hasSize(1);

    container.saveActivities();
    assertThat(Files.readAllLines(file.toPath())).hasSize(1);
  }

  @Test
  public void saveActivities_doesnNotwriteToFile_ifFileDoesNotExist() throws URISyntaxException, IOException {
    container.clearActivities();
    repository.deleteAll();

    Path file = Paths.get("controller/DOEST/NOT/EXIST.txt");
    assertThat(file.toFile().exists()).isFalse();
    container.setTodoFile(file.toFile());
    assertThat(container.getActivities()).isEmpty();

    container.createActivity();
    assertThat(container.getActivities()).hasSize(1);

    container.saveActivities();

    List<ILoggingEvent> logsList = logAppender.list;
    SoftAssertions assertions = new SoftAssertions();
    assertions.assertThat(logsList.get(0)
        .getMessage()).contains("No output file found!");
    assertions.assertThat(logsList.get(0).getLevel()).isEqualTo(Level.ERROR);
    assertions.assertThat(logsList.get(1)
        .getMessage()).contains("Saving complete");
    assertions.assertThat(logsList.get(1).getLevel()).isEqualTo(Level.INFO);
    assertions.assertAll();
  }

  @Test
  public void saveActivities_writeToFile_onlyWritesOnce() throws URISyntaxException, IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("controller/todo_write.txt").toURI());
    container.clearActivities();
    repository.deleteAll();
    container.setTodoFile(file);
    assertThat(container.getActivities()).isEmpty();
    assertThat(Files.readAllLines(file.toPath())).isEmpty();

    container.createActivity();
    container.createActivity();
    assertThat(container.getActivities()).hasSize(2);

    container.saveActivities();
    assertThat(Files.readAllLines(file.toPath())).hasSize(2);

    container.saveActivities();
    assertThat(Files.readAllLines(file.toPath())).hasSize(2);
  }

  @Test
  public void saveActivities_timeLogsAreSaved()
      throws IOException, URISyntaxException, ParseException {
    reset();

    ClassLoader classLoader = getClass().getClassLoader();
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    container.setTimeFile(timefile);

    container.createActivity();
    ActivityModel activityOne = container.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    TimeLogModel timeLog = new TimeLogModel();
    timeLog.setStart(dateFormat.parse("2018-12-05:18:48:33.130"));
    timeLog.setEnd(dateFormat.parse("2018-13-05:18:48:33.130"));
    activityOne.addTimeLog(timeLog);

    container.saveActivities();
    assertThat(Files.readAllLines(timefile.toPath())).isNotEmpty();
    assertThat(Files.readAllLines(timefile.toPath())).hasSize(1);
  }

  @Test
  public void loadActivities_timeLogsAreLoaded() throws URISyntaxException {
    reset();

    ClassLoader classLoader = getClass().getClassLoader();
    File todoFile = new File(classLoader.getResource("controller/todo_with_uuid_read.txt").toURI());
    File timeFile = new File(classLoader.getResource("controller/timelog.txt").toURI());

    container.setTodoFile(todoFile);
    container.setTimeFile(timeFile);

    container.loadActivities();

    assertThat(container.getActivities()).isNotEmpty();
    assertThat(container.getActivities()).hasSize(4);
    ActivityModel shouldContainTimeLogs = container.getActivities().get(1);
    assertThat(shouldContainTimeLogs).isNotNull();
    assertThat(shouldContainTimeLogs.getTitle()).isEqualTo("Show application demo");
    assertThat(shouldContainTimeLogs.getTimelogs()).isNotEmpty();
    assertThat(shouldContainTimeLogs.getTimelogs()).hasSize(3);
  }

}