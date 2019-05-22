/**
 * GNU Affero General Public License Version 3
 *
 * Copyright (c) 2019 Stijn Dejongh
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
 * If not, see <http://www.gnu.org/licenses/>.
 */
package be.doji.productivity.trambu.front.controller;

import static be.doji.productivity.trambu.front.TrambuWebApplication.PATH_CONFIGURATION_DIRECTORY;
import static be.doji.productivity.trambu.front.converter.ActivityModelConverter.toDatabase;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

@SessionScope
@Named
public class ActivityOverviewController {

  private File todoFile;

  private final FileWriter writer;

  private final FileLoader loader;

  private final ActivityDatabaseRepository repository;

  private List<ActivityModel> model = new ArrayList<>();
  private FilterChain<ActivityModel> filterchain = new FilterChain();

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Inject
  ActivityOverviewController(@Autowired FileWriter writer, @Autowired FileLoader loader,
      @Autowired ActivityDatabaseRepository repository) {
    this.writer = writer;
    this.loader = loader;
    this.repository = repository;
  }

  @PostConstruct
  public void init() {
    this.todoFile = PATH_CONFIGURATION_DIRECTORY.resolve("TODO.txt").toFile();
    if (this.model == null || this.model.isEmpty()) {
      loadActivities();
    }
  }

  void loadActivities() {
    try {
      if (todoFile != null && todoFile.exists()) {
        loader.loadTodoFileActivities(todoFile);
      } else {
        showMessage("No todo file found!");
      }
      this.model = repository.findAll().stream()
          .map(ActivityModelConverter::parse)
          .collect(Collectors.toList());
    } catch (IOException e) {
      showMessage("Error while saving activities to file");
    }
  }

  public List<ActivityModel> getActivities() {
    return this.model;
  }

  public List<ActivityModel> getFilteredActivities() {
    return filterchain.getFilteredData(this.model);
  }

  public void toggleEditable(ActivityModel model) {
    ActivityModel toToggle = findModelInList(model.getFrontId());
    boolean editable = toToggle.isEditable();

    if (model.isEditable()) {
      saveActivities();
    }

    toToggle.setEditable(!editable);
  }

  public void toggleExpanded(ActivityModel model) {
    ActivityModel toToggle = findModelInList(model.getFrontId());
    toToggle.setExpanded(!toToggle.isExpanded());
  }

  public void toggleCompleted(ActivityModel model) {
    ActivityModel toToggle = findModelInList(model.getFrontId());
    toToggle.setCompleted(!toToggle.isCompleted());
    saveActivities();
  }

  void saveActivities() {
    for (ActivityModel activityModel : getActivities()) {
      ActivityData savedData = repository.save(toDatabase(activityModel));
      activityModel.setDataBaseId(savedData.getId());
    }

    writeToFile();
    showMessage("Activities saved");
  }

  void writeToFile() {
    try {
      if (todoFile != null && todoFile.exists()) {
        writer.writeActivtiesToFile(todoFile);
      } else {
        showMessage("No output file found!");
      }
    } catch (IOException e) {
      showMessage("Error while saving activities to file");
    }
  }

  public void createActivity() {
    ActivityModel newActivity = new ActivityModel();
    this.model.add(newActivity);
  }

  public void deleteActivity(ActivityModel toDelete) {
    if (toDelete != null) {
      ActivityModel modelInList = findModelInList(toDelete.getFrontId());
      ActivityData databaseModel = toDatabase(modelInList);
      repository.delete(databaseModel);
      this.model.remove(modelInList);
      saveActivities();
      showMessage("Activity deleted");
    }
  }

  private ActivityModel findModelInList(String frontId) {
    return this.model.stream().filter(m -> m.getFrontId().equals(frontId))
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
    Optional<List<String>> reduce = this.getActivities().stream().map(getter)
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

  public void setTodoFile(File todoFile) {
    this.todoFile = todoFile;
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


}
