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
package be.doji.productivity.trambu.front.controller.page;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.getInstance;
import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.front.controller.state.ActivityModelContainer;
import be.doji.productivity.trambu.front.converter.TimeLogConverter;
import be.doji.productivity.trambu.front.transfer.ActivityModel;
import be.doji.productivity.trambu.front.transfer.TimeLogModel;
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

  private static final String LINE_ONE = "x (A) [Go to store for food?] @[food] due:2018-12-07:00:00:00.000 warningPeriod:PT24H";
  private static final String LINE_TWO = "(C) [Show the application to people] +[TraMBU] @[showoff] warningPeriod:PT24H loc:[Home]";
  private static final String LINE_THREE = "(A) 2017-10-21:14:13.000 [Hello World!] +[Overarching Project] @[Tag] @[Tag with multiple words] due:2017-12-21:16:15:00.000 uuid:283b6271-b513-4e89-b757-10e98c9078ea";
  private SimpleDateFormat dateFormat;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired private ActivityModelContainer container;

  private ActivityOverviewController controller;

  @Before
  public void setUp() throws IOException, URISyntaxException {
    this.dateFormat = new SimpleDateFormat(TimeLogConverter.LOG_DATE_PATTERN);
    this.controller = new ActivityOverviewController(container);
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
  public void toggleEditable_savesActivities() throws URISyntaxException {
    reset();
    assertThat(container.getActivities()).isEmpty();

    ActivityModel toToggle = container.getActivity(container.createActivity());
    assertThat(container.getActivities()).hasSize(1);

    // Replicate UI interactions
    controller.toggleEditable(toToggle);
    toToggle.setTitle("A new title");
    controller.toggleEditable(toToggle);

    assertThat(container.getActivities()).hasSize(1);
    assertThat(container.getActivity(toToggle.getReferenceKey()).getTitle()).isEqualTo("A new title");
  }

  @Test
  public void toggleComplete_savesActivities() throws URISyntaxException {
    reset();
    assertThat(container.getActivities()).isEmpty();

    controller.createActivity();
    assertThat(container.getActivities()).hasSize(1);

    // Replicate UI interactions
    ActivityModel toToggle = container.getActivities().get(0);
    assertThat(toToggle.isCompleted()).isFalse();

    controller.toggleCompleted(toToggle);

    assertThat(container.getActivities()).hasSize(1);
    assertThat(container.getActivities().get(0).isCompleted()).isTrue();
  }

  @Test
  public void completeTags_containsAllExistingTags() throws URISyntaxException {
    reset();

    ActivityModel activityOne = container.getActivity(controller.createActivity());
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    ActivityModel activityTwo = container.getActivity(controller.createActivity());
    activityTwo.setTags(Arrays.asList("Cat", "Dog"));

    assertThat(controller.completeTags("C")).hasSize(3);
    assertThat(controller.completeTags("D")).hasSize(2);
    assertThat(controller.completeTags("T")).hasSize(3);
  }

  @Test
  public void completeProjects_containsAllExistingTags() throws URISyntaxException {
    reset();

    ActivityModel activityOne = container.getActivity(controller.createActivity());
    activityOne.setProjects(Arrays.asList("Cone", "Two"));

    ActivityModel activityTwo = container.getActivity(controller.createActivity());
    activityTwo.setProjects(Arrays.asList("Cat", "Dog"));

    assertThat(controller.completeProjects("C")).hasSize(3);
    assertThat(controller.completeProjects("D")).hasSize(2);
    assertThat(controller.completeProjects("T")).hasSize(3);
  }


  @Test
  public void filter_onTag() throws URISyntaxException {
    reset();

    String refOne = controller.createActivity();
    ActivityModel activityOne = container.getActivity(refOne);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    String refTwo = controller.createActivity();
    ActivityModel activityTwo = container.getActivity(refTwo);
    activityTwo.setTags(Arrays.asList("Cat", "Dog"));

    controller.resetFilter();
    assertThat(controller.getFilteredActivities()).hasSize(2);

    controller.addTagFilter("Two");
    assertThat(controller.getFilteredActivities()).hasSize(1);
  }

  @Test
  public void filter_noFilter() throws URISyntaxException {
    reset();

    String refOne = controller.createActivity();
    ActivityModel activityOne = container.getActivity(refOne);
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    String refTwo = controller.createActivity();
    ActivityModel activityTwo = container.getActivity(refTwo);
    activityTwo.setTags(Arrays.asList("Cat", "Dog"));

    controller.resetFilter();

    assertThat(controller.getFilteredActivities()).hasSize(2);
  }

  @Test
  public void filter_onTag_multipleFilters() throws URISyntaxException {
    reset();

    ActivityModel activityOne = container.getActivity(controller.createActivity());
    activityOne.setTags(Arrays.asList("Cone", "Two", "Dog"));

    ActivityModel activityTwo = container.getActivity(controller.createActivity());
    activityTwo.setTags(Arrays.asList("Cat", "Dog"));

    controller.addTagFilter("Dog");
    assertThat(controller.getFilteredActivities()).hasSize(2);
    controller.addTagFilter("Two");
    assertThat(controller.getFilteredActivities()).hasSize(1);

  }


  @Test
  public void filter_onProject_multipleFilters() throws URISyntaxException {
    reset();

    ActivityModel activityOne = container.getActivity(controller.createActivity());
    activityOne.setProjects(Arrays.asList("Cone", "Two", "Dog"));

    ActivityModel activityTwo = container.getActivity(controller.createActivity());
    activityTwo.setProjects(Arrays.asList("Cat", "Dog"));

    controller.addProjectFilter("Dog");
    assertThat(controller.getFilteredActivities()).hasSize(2);
    controller.addProjectFilter("Two");
    assertThat(controller.getFilteredActivities()).hasSize(1);
  }

  @Test
  public void getPossibleProjectFilters_withExistingFilter_onlyReturnsInactiveFilters() throws URISyntaxException {
    reset();

    ActivityModel activityOne = container.getActivity(controller.createActivity());
    activityOne.setProjects(Arrays.asList("Cone", "Two", "Dog", "Cat"));

    assertThat(controller.getFilterOptionsForProjects()).hasSize(4);

    controller.addProjectFilter("Two");

    assertThat(controller.getFilterOptionsForProjects()).hasSize(3);
    assertThat(controller.getFilterOptionsForProjects()).contains("Cone");
    assertThat(controller.getFilterOptionsForProjects()).contains("Dog");
    assertThat(controller.getFilterOptionsForProjects()).contains("Cat");
  }

  @Test
  public void getPossibleTagFilters_withExistingFilter_onlyReturnsInactiveFilters() throws URISyntaxException {
    reset();

    ActivityModel activityOne = container.getActivity(controller.createActivity());
    activityOne.setTags(Arrays.asList("Cone", "Two", "Dog", "Cat"));

    assertThat(controller.getFilterOptionsForTags()).hasSize(4);

    controller.addTagFilter("Two");

    assertThat(controller.getFilterOptionsForTags()).hasSize(3);
    assertThat(controller.getFilterOptionsForTags()).contains("Cone");
    assertThat(controller.getFilterOptionsForTags()).contains("Dog");
    assertThat(controller.getFilterOptionsForTags()).contains("Cat");
  }


  @Test
  public void getAllExistingProjects_multipleActivities() throws URISyntaxException {
    reset();

    ActivityModel activityOne = container.getActivity(controller.createActivity());
    activityOne.setProjects(Arrays.asList("Cone", "Two"));

    ActivityModel activityTwo = container.getActivity(controller.createActivity());
    activityTwo.setProjects(Arrays.asList("Cat", "Dog"));

    assertThat(controller.getFilterOptionsForProjects()).hasSize(4);
  }

  @Test
  public void getAllExistingProjects_multipleActivities_haveOverlap() throws URISyntaxException {
    reset();

    ActivityModel activityOne = container.getActivity(controller.createActivity());
    activityOne.setProjects(Arrays.asList("Cone", "Two"));

    ActivityModel activityTwo = container.getActivity(controller.createActivity());
    activityTwo.setProjects(Arrays.asList("Cat", "Two"));

    assertThat(controller.getFilterOptionsForProjects()).hasSize(3);
  }

  @Test
  public void getAllExistingTags_multipleActivities() throws URISyntaxException {
    reset();

    ActivityModel activityOne = container.getActivity(controller.createActivity());
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    ActivityModel activityTwo = container.getActivity(controller.createActivity());
    activityTwo.setTags(Arrays.asList("Cat", "Dog"));

    assertThat(container.getActivities()).hasSize(2);
    assertThat(controller.getFilteredActivities()).hasSize(2);
    assertThat(controller.getFilterOptionsForTags()).hasSize(4);
  }

  @Test
  public void getAllExistingTags_multipleActivities_haveOverlap() throws URISyntaxException {
    reset();

    ActivityModel activityOne = container.getActivity(controller.createActivity());
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    ActivityModel activityTwo = container.getActivity(controller.createActivity());
    activityTwo.setTags(Arrays.asList("Cat", "Two"));

    assertThat(controller.getFilterOptionsForTags()).hasSize(3);
  }

  @Test
  public void delete_removesActivity() throws URISyntaxException {
    reset();

    ActivityModel activityOne = container.getActivity(controller.createActivity());
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    ActivityModel activityTwo = container.getActivity(controller.createActivity());
    activityTwo.setTags(Arrays.asList("Cat", "Two"));

    assertThat(container.getActivities()).hasSize(2);

    controller.deleteActivity(activityTwo);

    assertThat(container.getActivities()).hasSize(1);
  }

  @Test
  public void hoursSpentTotal_returnsCorrectHours()
      throws ParseException, URISyntaxException {
    reset();
    ClassLoader classLoader = getClass().getClassLoader();
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    container.setTimeFile(timefile);

    ActivityModel activityOne = container.getActivity(controller.createActivity());
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
      throws ParseException, URISyntaxException {
    reset();
    ClassLoader classLoader = getClass().getClassLoader();
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    container.setTimeFile(timefile);

    ActivityModel activityOne = container.getActivity(controller.createActivity());
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
      throws URISyntaxException {
    reset();
    ClassLoader classLoader = getClass().getClassLoader();
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    container.setTimeFile(timefile);

    ActivityModel activityOne = container.getActivity(controller.createActivity());
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
  public void toggleTimeLog() throws URISyntaxException {
    reset();

    ClassLoader classLoader = getClass().getClassLoader();
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    container.setTimeFile(timefile);

    ActivityModel activityOne = container.getActivity(controller.createActivity());
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    assertThat(activityOne.getTimelogs()).isEmpty();

    controller.toggleTimelog(activityOne);
    assertThat(activityOne.getTimelogs()).hasSize(1);
    assertThat(activityOne.getTimeRunning()).isTrue();
  }

  @Test
  public void autotrack_startsTimeWhenActivated() throws URISyntaxException {
    reset();

    ClassLoader classLoader = getClass().getClassLoader();
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());
    container.setTimeFile(timefile);

    ActivityModel activityOne = container.getActivity(controller.createActivity());
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
  public void autotrack_doesntTimeWhenDeactivated() throws URISyntaxException {
    reset();

    ActivityModel activityOne = container.getActivity(container.createActivity());
    activityOne.setTags(Arrays.asList("Cone", "Two"));

    assertThat(activityOne.getTimelogs()).isEmpty();
    assertThat(controller.isAutotracking()).isFalse();
    assertThat(activityOne.getTimeRunning()).isFalse();

    controller.toggleExpanded(activityOne);
    assertThat(activityOne.getTimeRunning()).isFalse();
  }

  private void reset() throws URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    File todofile = new File(classLoader.getResource("controller/todo_write.txt").toURI());
    File timefile = new File(classLoader.getResource("controller/timelog_write.txt").toURI());

    container.setTodoFile(todofile);
    container.setTimeFile(timefile);
    container.reset();

    assertThat(container.getActivities()).isEmpty();

    controller.resetFilter();
    assertThat(controller.getFilterchain().getFilters()).isEmpty();
  }


}