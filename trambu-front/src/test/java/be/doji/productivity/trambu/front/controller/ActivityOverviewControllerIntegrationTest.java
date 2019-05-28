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
package be.doji.productivity.trambu.front.controller;

import static java.util.Calendar.*;
import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.front.converter.ActivityModelConverter;
import be.doji.productivity.trambu.front.converter.TimeLogConverter;
import be.doji.productivity.trambu.front.transfer.ActivityModel;
import be.doji.productivity.trambu.front.transfer.TimeLogModel;
import be.doji.productivity.trambu.infrastructure.file.FileLoader;
import be.doji.productivity.trambu.infrastructure.file.FileWriter;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import org.junit.After;
import org.junit.Before;
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

  @Autowired
  private ActivityModelConverter modelConverter;

  private static final String LINE_ONE = "x (A) [Go to store for food?] @[food] due:2018-12-07:00:00:00.000 warningPeriod:PT24H";
  private static final String LINE_TWO = "(C) [Show the application to people] +[TraMBU] @[showoff] warningPeriod:PT24H loc:[Home]";
  private static final String LINE_THREE = "(A) 2017-10-21:14:13.000 [Hello World!] +[Overarching Project] @[Tag] @[Tag with multiple words] due:2017-12-21:16:15:00.000 uuid:283b6271-b513-4e89-b757-10e98c9078ea";
  private SimpleDateFormat dateFormat;

  @Before
  public void setUp() throws IOException, URISyntaxException {
    this.controller = new ActivityOverviewController(writer, loader, repository, modelConverter);
    this.dateFormat = new SimpleDateFormat(TimeLogConverter.LOG_DATE_PATTERN);
    cleanUp();
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
    assertThat(controller.getActivities()).isEmpty();

    controller.createActivity();

    assertThat(controller.getActivities()).hasSize(1);
  }

  @Test
  public void saveActivities_writeToRepository() throws URISyntaxException, IOException {
    clearActivityState();
    assertThat(controller.getActivities()).isEmpty();
    assertThat(repository.findAll()).isEmpty();

    controller.createActivity();
    assertThat(controller.getActivities()).hasSize(1);

    controller.saveActivities();
    assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  public void saveActivities_writeToFile() throws URISyntaxException, IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("controller/todo_write.txt").toURI());
    controller.clearActivities();
    repository.deleteAll();
    controller.setTodoFile(file);
    assertThat(controller.getActivities()).isEmpty();
    assertThat(Files.readAllLines(file.toPath())).isEmpty();

    controller.createActivity();
    assertThat(controller.getActivities()).hasSize(1);

    controller.saveActivities();
    assertThat(Files.readAllLines(file.toPath())).hasSize(1);
  }

  @Test
  public void saveActivities_writeToFile_onlyWritesOnce() throws URISyntaxException, IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("controller/todo_write.txt").toURI());
    controller.clearActivities();
    repository.deleteAll();
    controller.setTodoFile(file);
    assertThat(controller.getActivities()).isEmpty();
    assertThat(Files.readAllLines(file.toPath())).isEmpty();

    controller.createActivity();
    controller.createActivity();
    assertThat(controller.getActivities()).hasSize(2);

    controller.saveActivities();
    assertThat(Files.readAllLines(file.toPath())).hasSize(2);

    controller.saveActivities();
    assertThat(Files.readAllLines(file.toPath())).hasSize(2);
  }


  @Test
  public void toggleEditable_savesActivities() throws URISyntaxException, IOException {
    clearActivityState();
    assertThat(controller.getActivities()).isEmpty();
    assertThat(repository.findAll()).isEmpty();

    controller.createActivity();
    assertThat(controller.getActivities()).hasSize(1);

    // Replicate UI interactions
    ActivityModel toToggle = controller.getActivities().get(0);
    controller.toggleEditable(toToggle);
    toToggle.setTitle("A new title");
    controller.toggleEditable(toToggle);

    assertThat(repository.findAll()).hasSize(1);
    assertThat(repository.findAll().get(0).getTitle()).isEqualTo("A new title");
  }

  @Test
  public void toggleComplete_savesActivities() throws URISyntaxException, IOException {
    clearActivityState();
    assertThat(controller.getActivities()).isEmpty();
    assertThat(repository.findAll()).isEmpty();

    controller.createActivity();
    assertThat(controller.getActivities()).hasSize(1);

    // Replicate UI interactions
    ActivityModel toToggle = controller.getActivities().get(0);
    assertThat(toToggle.isCompleted()).isFalse();

    controller.toggleCompleted(toToggle);

    assertThat(repository.findAll()).hasSize(1);
    assertThat(repository.findAll().get(0).isCompleted()).isTrue();
  }

  @Test
  public void completeTags_containsAllExistingTags() throws URISyntaxException, IOException {
    clearActivityState();
    assertThat(controller.getActivities()).isEmpty();
    assertThat(repository.findAll()).isEmpty();

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    controller.createActivity();
    ActivityModel activityTwo = controller.getActivities().get(1);
    activityTwo.setTags(Arrays.asList("Cat", "Dog"));

    assertThat(controller.completeTags("C")).hasSize(2);
    assertThat(controller.completeTags("D")).hasSize(1);
    assertThat(controller.completeTags("T")).hasSize(2);
  }

  @Test
  public void completeProjects_containsAllExistingTags() throws URISyntaxException, IOException {
    clearActivityState();

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setProjects(Arrays.asList("Cone", "Two"));

    controller.createActivity();
    ActivityModel activityTwo = controller.getActivities().get(1);
    activityTwo.setProjects(Arrays.asList("Cat", "Dog"));

    assertThat(controller.completeProjects("C")).hasSize(2);
    assertThat(controller.completeProjects("D")).hasSize(1);
    assertThat(controller.completeProjects("T")).hasSize(2);
  }


  @Test
  public void filter_onTag() throws URISyntaxException, IOException {
    clearActivityState();

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    controller.createActivity();
    ActivityModel activityTwo = controller.getActivities().get(1);
    activityTwo.setTags(Arrays.asList("Cat", "Dog"));

    controller.resetFilter();

    controller.addTagFilter("Two");
    assertThat(controller.getFilteredActivities()).hasSize(1);
  }

  @Test
  public void filter_noFilter() throws URISyntaxException, IOException {
    clearActivityState();

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    controller.createActivity();
    ActivityModel activityTwo = controller.getActivities().get(1);
    activityTwo.setTags(Arrays.asList("Cat", "Dog"));

    controller.resetFilter();

    assertThat(controller.getFilteredActivities()).hasSize(2);
  }

  @Test
  public void filter_onTag_multipleFilters() throws URISyntaxException, IOException {
    clearActivityState();

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two", "Dog"));

    controller.createActivity();
    ActivityModel activityTwo = controller.getActivities().get(1);
    activityTwo.setTags(Arrays.asList("Cat", "Dog"));

    controller.addTagFilter("Dog");
    assertThat(controller.getFilteredActivities()).hasSize(2);
    controller.addTagFilter("Two");
    assertThat(controller.getFilteredActivities()).hasSize(1);

  }


  @Test
  public void filter_onProject_multipleFilters() throws URISyntaxException, IOException {
    clearActivityState();

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setProjects(Arrays.asList("Cone", "Two", "Dog"));

    controller.createActivity();
    ActivityModel activityTwo = controller.getActivities().get(1);
    activityTwo.setProjects(Arrays.asList("Cat", "Dog"));

    controller.addProjectFilter("Dog");
    assertThat(controller.getFilteredActivities()).hasSize(2);
    controller.addProjectFilter("Two");
    assertThat(controller.getFilteredActivities()).hasSize(1);
  }


  @Test
  public void getAllExistingProjects_multipleActivities() throws URISyntaxException, IOException {
    clearActivityState();

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setProjects(Arrays.asList("Cone", "Two"));

    controller.createActivity();
    ActivityModel activityTwo = controller.getActivities().get(1);
    activityTwo.setProjects(Arrays.asList("Cat", "Dog"));

    assertThat(controller.getAllExistingProjects()).hasSize(4);
  }

  @Test
  public void getAllExistingProjects_multipleActivities_haveOverlap()
      throws URISyntaxException, IOException {
    clearActivityState();

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setProjects(Arrays.asList("Cone", "Two"));

    controller.createActivity();
    ActivityModel activityTwo = controller.getActivities().get(1);
    activityTwo.setProjects(Arrays.asList("Cat", "Two"));

    assertThat(controller.getAllExistingProjects()).hasSize(3);
  }

  @Test
  public void getAllExistingTags_multipleActivities() throws URISyntaxException, IOException {
    clearActivityState();

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    controller.createActivity();
    ActivityModel activityTwo = controller.getActivities().get(1);
    activityTwo.setTags(Arrays.asList("Cat", "Dog"));

    assertThat(controller.getAllExistingTags()).hasSize(4);
  }

  @Test
  public void getAllExistingTags_multipleActivities_haveOverlap()
      throws URISyntaxException, IOException {
    clearActivityState();

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    controller.createActivity();
    ActivityModel activityTwo = controller.getActivities().get(1);
    activityTwo.setTags(Arrays.asList("Cat", "Two"));

    assertThat(controller.getAllExistingTags()).hasSize(3);
  }

  @Test
  public void delete_removesActivity() throws IOException, URISyntaxException {
    clearActivityState();

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    controller.createActivity();
    ActivityModel activityTwo = controller.getActivities().get(1);
    activityTwo.setTags(Arrays.asList("Cat", "Two"));

    assertThat(controller.getActivities()).hasSize(2);

    controller.deleteActivity(activityTwo);

    assertThat(controller.getActivities()).hasSize(1);
  }

  @Test
  public void loadActivities_timeLogsAreLoaded() throws URISyntaxException, IOException {
    clearActivityState();
    ClassLoader classLoader = getClass().getClassLoader();
    File todoFile = new File(classLoader.getResource("controller/todo_with_uuid_read.txt").toURI());
    File timeFile = new File(classLoader.getResource("controller/timelog.txt").toURI());

    controller.setTodoFile(todoFile);
    controller.setTimeFile(timeFile);

    controller.loadActivities();

    assertThat(controller.getActivities()).isNotEmpty();
    assertThat(controller.getActivities()).hasSize(4);
    ActivityModel shouldContainTimeLogs = controller.getActivities().get(0);
    assertThat(shouldContainTimeLogs).isNotNull();
    assertThat(shouldContainTimeLogs.getTitle()).isEqualTo("Show application demo");
    assertThat(shouldContainTimeLogs.getTimelogs()).isNotEmpty();
    assertThat(shouldContainTimeLogs.getTimelogs()).hasSize(3);
  }

  @Test
  public void saveActivities_timeLogsAreSaved()
      throws IOException, URISyntaxException, ParseException {
    clearActivityState();
    ClassLoader classLoader = getClass().getClassLoader();
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    controller.setTimeFile(timefile);

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    TimeLogModel timeLog = new TimeLogModel();
    timeLog.setStart(dateFormat.parse("2018-12-05:18:48:33.130"));
    timeLog.setEnd(dateFormat.parse("2018-13-05:18:48:33.130"));
    activityOne.addTimeLog(timeLog);

    controller.saveActivities();
    assertThat(Files.readAllLines(timefile.toPath())).isNotEmpty();
    assertThat(Files.readAllLines(timefile.toPath())).hasSize(1);
  }

  @Test
  public void hoursSpentTotal_returnsCorrectHours()
      throws ParseException, IOException, URISyntaxException {
    clearActivityState();
    ClassLoader classLoader = getClass().getClassLoader();
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    controller.setTimeFile(timefile);

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    TimeLogModel timeLog = new TimeLogModel();
    timeLog.setStart(dateFormat.parse("2018-12-05:10:00:00.00"));
    timeLog.setEnd(dateFormat.parse("2018-12-05:18:45:33.130"));
    activityOne.addTimeLog(timeLog);

    String spentTotal = controller.hoursSpentTotal(activityOne.getReferenceKey());
    assertThat(spentTotal).isNotBlank();
    assertThat(spentTotal).isEqualTo("8.76");
  }

  @Test
  public void hoursSpentToday_returnsZero_ifNoTimeSpentToday()
      throws ParseException, IOException, URISyntaxException {
    clearActivityState();
    ClassLoader classLoader = getClass().getClassLoader();
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    controller.setTimeFile(timefile);

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    TimeLogModel timeLog = new TimeLogModel();
    timeLog.setStart(dateFormat.parse("1995-12-05:10:00:00.00"));
    timeLog.setEnd(dateFormat.parse("1995-12-05:18:45:33.130"));
    activityOne.addTimeLog(timeLog);

    String spentTotal = controller.hoursSpentToday(activityOne.getReferenceKey());
    assertThat(spentTotal).isNotBlank();
    assertThat(spentTotal).isEqualTo("0.00");
  }

  @Test
  public void hoursSpentToday_returnsTwo_ifTwoHoursSpentToday()
      throws IOException, URISyntaxException {
    clearActivityState();
    ClassLoader classLoader = getClass().getClassLoader();
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    controller.setTimeFile(timefile);

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    TimeLogModel timeLog = new TimeLogModel();
    Calendar start = getInstance();
    start.set(HOUR_OF_DAY, start.get(HOUR_OF_DAY) - 2);
    Calendar end = getInstance();

    timeLog.setStart(start.getTime());
    timeLog.setEnd(end.getTime());
    activityOne.addTimeLog(timeLog);

    String spentTotal = controller.hoursSpentToday(activityOne.getReferenceKey());
    assertThat(spentTotal).isNotBlank();
    assertThat(spentTotal).isEqualTo("2.00");
  }

  @Test
  public void toggleTimeLog() throws IOException, URISyntaxException {
    clearActivityState();
    ClassLoader classLoader = getClass().getClassLoader();
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    controller.setTimeFile(timefile);

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    assertThat(activityOne.getTimelogs()).isEmpty();

    controller.toggleTimelog(activityOne);
    assertThat(activityOne.getTimelogs()).hasSize(1);
    assertThat(activityOne.getTimeRunning()).isTrue();
  }

  @Test
  public void autotrack_startsTimeWhenActivated() throws IOException, URISyntaxException {
    clearActivityState();
    ClassLoader classLoader = getClass().getClassLoader();
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    controller.setTimeFile(timefile);

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    assertThat(activityOne.getTimelogs()).isEmpty();
    assertThat(controller.isAutotracking()).isFalse();
    assertThat(activityOne.getTimeRunning()).isFalse();

    controller.toggleAutotrack();
    assertThat(controller.isAutotracking()).isTrue();

    controller.toggleExpanded(activityOne);
    assertThat(activityOne.getTimeRunning()).isTrue();
  }

  @Test
  public void autotrack_doesntTimeWhenDeactivated() throws IOException, URISyntaxException {
    clearActivityState();
    ClassLoader classLoader = getClass().getClassLoader();
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    controller.setTimeFile(timefile);

    controller.createActivity();
    ActivityModel activityOne = controller.getActivities().get(0);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    assertThat(activityOne.getTimelogs()).isEmpty();
    assertThat(controller.isAutotracking()).isFalse();
    assertThat(activityOne.getTimeRunning()).isFalse();

    controller.toggleExpanded(activityOne);
    assertThat(activityOne.getTimeRunning()).isFalse();
  }


  private void clearActivityState() throws URISyntaxException, IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File todofile = new File(classLoader.getResource("controller/todo_write.txt").toURI());
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    controller.setTodoFile(todofile);
    controller.setTimeFile(timefile);
    controller.clearActivities();
    repository.deleteAll();
    assertThat(controller.getActivities()).isEmpty();
    assertThat(repository.findAll()).isEmpty();
  }


}