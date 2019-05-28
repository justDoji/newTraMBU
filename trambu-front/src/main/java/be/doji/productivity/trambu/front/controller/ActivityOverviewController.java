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

import static be.doji.productivity.trambu.front.TrambuWebApplication.PATH_CONFIGURATION_DIRECTORY;

import be.doji.productivity.trambu.front.converter.ActivityModelConverter;
import be.doji.productivity.trambu.front.filter.FilterChain;
import be.doji.productivity.trambu.front.transfer.ActivityModel;
import be.doji.productivity.trambu.infrastructure.file.FileLoader;
import be.doji.productivity.trambu.infrastructure.file.FileWriter;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

@SessionScope
@Named
public class ActivityOverviewController {

  private static final Logger LOG = LoggerFactory.getLogger(ActivityOverviewController.class);

  private File todoFile;
  private File timeFile;

  private final FileWriter writer;
  private final FileLoader loader;
  private final ActivityModelConverter modelConverter;
  private final ActivityDatabaseRepository repository;

  private boolean autotracking;


  private List<ActivityModel> model = new ArrayList<>();
  private FilterChain<ActivityModel> filterchain = new FilterChain();

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Inject
  ActivityOverviewController(@Autowired FileWriter writer,
      @Autowired FileLoader loader,
      @Autowired ActivityDatabaseRepository repository,
      @Autowired ActivityModelConverter modelConverter) {
    this.writer = writer;
    this.loader = loader;
    this.repository = repository;
    this.modelConverter = modelConverter;
  }

  @PostConstruct
  public void init() {
    this.todoFile = PATH_CONFIGURATION_DIRECTORY.resolve("TODO.txt").toFile();
    this.timeFile = PATH_CONFIGURATION_DIRECTORY.resolve("TIMELOG.txt").toFile();
    if (this.model == null || this.model.isEmpty()) {
      loadActivities();
    }
  }

  void loadActivities() {
    try {
      loadActivitiesFromFile();
      loadTimelogsFromFile();
      repopulateModel();
    } catch (IOException e) {
      showMessage("Error while saving activities to file");
    }
  }

  private void loadActivitiesFromFile() throws IOException {
    if (todoFile != null && todoFile.exists()) {
      loader.loadTodoFileActivities(todoFile);
    } else {
      showMessage("No todo file found!");
    }
  }

  private void loadTimelogsFromFile() throws IOException {
    if (timeFile != null && timeFile.exists()) {
      loader.loadTimeLogFile(timeFile);
    } else {
      showMessage("No timelog file found!");
    }
  }

  private void repopulateModel() {
    this.model = repository.findAll().stream()
        .map(modelConverter::parse)
        .collect(Collectors.toList());
  }

  public List<ActivityModel> getActivities() {
    return this.model;
  }

  public List<ActivityModel> getFilteredActivities() {
    return filterchain.getFilteredData(this.model);
  }

  public void toggleEditable(ActivityModel model) {
    ActivityModel toToggle = findModelInList(model.getReferenceKey());
    boolean editable = toToggle.isEditable();

    if (model.isEditable()) {
      saveActivities();
    }

    toToggle.setEditable(!editable);
  }

  public void toggleExpanded(ActivityModel model) {
    ActivityModel toToggle = findModelInList(model.getReferenceKey());
    toToggle.setExpanded(!toToggle.isExpanded());

    if (isAutotracking()) {
      LOG.debug("Autotrack for activity: " + model.getTitle());
      toggleTimelog(model);
    }
  }

  public void toggleCompleted(ActivityModel model) {
    ActivityModel toToggle = findModelInList(model.getReferenceKey());
    toToggle.setCompleted(!toToggle.isCompleted());
    saveActivities();
  }

  void saveActivities() {
    for (ActivityModel activityModel : getActivities()) {
      repository.findByReferenceKey(activityModel.getReferenceKey())
          .ifPresent(repository::delete);
      ActivityData savedData = repository.save(modelConverter.toDatabase(activityModel));
      activityModel.setDataBaseId(savedData.getId());
    }

    writeToFile();
    showMessage("Activities saved");
  }

  void writeToFile() {
    try {
      writeActivities();
      writeTimelogs();
    } catch (IOException e) {
      showMessage("Error while saving activities to file");
    }
  }

  private void writeActivities() throws IOException {
    if (todoFile != null && todoFile.exists()) {
      writer.writeActivtiesToFile(todoFile);
    } else {
      showMessage("No output file found!");
    }
  }

  private void writeTimelogs() throws IOException {
    if (timeFile != null && timeFile.exists()) {
      writer.writeTimeLogsToFile(timeFile);
    } else {
      showMessage("No output file found!");
    }
  }

  public void createActivity() {
    ActivityModel newActivity = new ActivityModel();
    this.model.add(newActivity);
  }

  public void deleteActivity(ActivityModel toDelete) {
    if (toDelete != null) {
      ActivityModel modelInList = findModelInList(toDelete.getReferenceKey());
      ActivityData databaseModel = modelConverter.toDatabase(modelInList);
      repository.delete(databaseModel);
      this.model.remove(modelInList);
      saveActivities();
      showMessage("Activity deleted");
    }
  }

  private ActivityModel findModelInList(String frontId) {
    return this.model.stream().filter(m -> m.getReferenceKey().equals(frontId))
        .collect(Collectors.toList()).get(0);
  }

  public List<String> completeTags(String query) {
    return getOptions(query, ActivityModel::getTags);
  }

  public List<String> completeProjects(String query) {
    return getOptions(query, ActivityModel::getProjects);
  }

  public List<String> getAllExistingTags() {
    return getValuesFor(ActivityModel::getTags).orElse(new ArrayList<>());
  }

  public List<String> getAllExistingProjects() {
    return getValuesFor(ActivityModel::getProjects).orElse(new ArrayList<>());
  }

  private List<String> getOptions(String query, Function<ActivityModel, List<String>> getter) {
    Optional<List<String>> reducedValues = getValuesFor(getter);

    List<String> options = reducedValues.orElse(new ArrayList<>());
    Set<String> returnOptions = new HashSet<>();
    for (String option : options) {
      if (option.toLowerCase().contains(query.toLowerCase())) {
        returnOptions.add(option);
      }
    }
    return new ArrayList<>(returnOptions);
  }

  private Optional<List<String>> getValuesFor(Function<ActivityModel, List<String>> getter) {
    Optional<List<String>> reduce = this.getFilteredActivities().stream().map(getter)
        .reduce(this::reduceStrings);
    reduce.ifPresent(strings -> strings.sort(String.CASE_INSENSITIVE_ORDER));
    return reduce;
  }

  private List<String> reduceStrings(List<String> strings, List<String> strings2) {
    Set<String> result = new HashSet<>();
    result.addAll(strings);
    result.addAll(strings2);
    return new ArrayList<>(result);
  }


  private void showMessage(String message) {
    FacesContext context = FacesContext.getCurrentInstance();

    if (context != null) {
      context.addMessage(null, new FacesMessage("Info", message));
    }
  }

  void clearActivities() {
    this.model.clear();
  }

  public void addTagFilter(String tagToInclude) {
    this.filterchain
        .addPositiveFiler(tagToInclude, ActivityModel::getTags,
            tags -> tags.contains(tagToInclude), "Tag");
  }

  public void addProjectFilter(String projectToInclude) {
    this.filterchain
        .addPositiveFiler(projectToInclude, ActivityModel::getProjects,
            projects -> projects.contains(projectToInclude), "Project");
  }

  public FilterChain<ActivityModel> getFilterchain() {
    return filterchain;
  }

  public void resetFilter() {
    this.filterchain.reset();
  }

  public void setTodoFile(File todoFile) {
    this.todoFile = todoFile;
  }

  public void setTimeFile(File timeFile) {
    this.timeFile = timeFile;
  }

  public void toggleTimelog(ActivityModel model) {
    ActivityModel toUpdate = findModelInList(model.getReferenceKey());
    toUpdate.toggleTimeLog();
    saveActivities();
  }

  public boolean isAutotracking() {
    return autotracking;
  }

  public void setAutotracking(boolean autotracking) {
    LOG.debug("autotrack setter: " + autotracking);
    this.autotracking = autotracking;
  }

  public void toggleAutotrack() {
    LOG.debug("Toggle autotrack");
    this.autotracking = !this.autotracking;
  }
}
