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

import be.doji.productivity.trambu.front.calculator.TimeSpentCalculator;
import be.doji.productivity.trambu.front.controller.exception.InvalidReferenceException;
import be.doji.productivity.trambu.front.controller.state.ActivityModelContainer;
import be.doji.productivity.trambu.front.filter.FilterChain;
import be.doji.productivity.trambu.front.transfer.ActivityModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
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
  public static final String FILTER_TYPE_PROJECT = "Project";
  public static final String FILTER_TYPE_TAG = "Tag";
  private final ActivityModelContainer activityContainer;
  private final FilterChain<ActivityModel> filterchain = new FilterChain<>();

  private boolean autotracking;

  @SuppressWarnings({"CdiInjectionPointsInspection"})
  @Inject
  ActivityOverviewController(@Autowired ActivityModelContainer activityContainer) {
    this.activityContainer = activityContainer;
  }

  public void toggleEditable(String activityKey) {
    ActivityModel toToggle = activityContainer.getActivity(activityKey);
    boolean editable = toToggle.isEditable();

    if (toToggle.isEditable()) {
      activityContainer.saveActivities();
    }

    toToggle.setEditable(!editable);
  }

  public void toggleExpanded(String activityKey) {
    ActivityModel toToggle = activityContainer.getActivity(activityKey);
    toToggle.setExpanded(!toToggle.isExpanded());

    if (isAutotracking()) {
      LOG.debug("Autotrack for activity: {}", toToggle.getTitle());
      toggleTimelog(toToggle);
    }
  }

  public void toggleCompleted(String activityKey) {
    ActivityModel toToggle = activityContainer.getActivity(activityKey);
    toToggle.setCompleted(!toToggle.isCompleted());
    activityContainer.saveActivities();
  }

  public String createActivity() {

    String activityKey = activityContainer.createActivity();
    toggleEditable(activityKey);
    return activityKey;
  }

  public void deleteActivity(String activityKey) {
    try {
      activityContainer.deleteActivity(activityKey);
    } catch (InvalidReferenceException e) {
      String message = "An error occured while deleting an activity";
      LOG.error(message);
      showMessage(message);
    }
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

  public void toggleTimelog(ActivityModel model) {
    ActivityModel toUpdate = activityContainer.getActivity(model.getReferenceKey());
    toUpdate.toggleTimeLog();
    activityContainer.saveActivities();
  }

  public boolean isAutotracking() {
    return autotracking;
  }

  public void setAutotracking(boolean autotracking) {
    LOG.debug("autotrack setter: {}", autotracking);
    this.autotracking = autotracking;
  }

  public void toggleAutotrack() {
    LOG.debug("Toggle autotrack");
    this.autotracking = !this.autotracking;
  }

  public String hoursSpentTotal(String referenceKey) {
    return TimeSpentCalculator.hoursSpentTotal(activityContainer.getActivity(referenceKey));
  }

  public String hoursSpentToday(String referenceKey) {
    return TimeSpentCalculator.hoursSpentToday(activityContainer.getActivity(referenceKey));
  }

  public List<ActivityModel> getFilteredActivities() {
    return filterchain.getFilteredData(activityContainer.getActivities());
  }

  public void addTagFilter(String tagToInclude) {
    this.filterchain
        .addPositiveFiler(tagToInclude, ActivityModel::getTags,
            tags -> tags.contains(tagToInclude), FILTER_TYPE_TAG);
  }

  public void addProjectFilter(String projectToInclude) {
    this.filterchain
        .addPositiveFiler(projectToInclude, ActivityModel::getProjects,
            projects -> projects.contains(projectToInclude), FILTER_TYPE_PROJECT);
  }

  public FilterChain<ActivityModel> getFilterchain() {
    return filterchain;
  }

  public void resetFilter() {
    this.filterchain.reset();
  }

  public List<String> getFilterOptionsForProjects() {
    return getOpenFilterOptionsForType(ActivityModel::getProjects,
        FILTER_TYPE_PROJECT);
  }

  public List<String> getFilterOptionsForTags() {
    return getOpenFilterOptionsForType(ActivityModel::getTags,
        FILTER_TYPE_TAG);
  }

  private List<String> getOpenFilterOptionsForType(Function<ActivityModel, List<String>> getter,
      String type) {
    List<String> collect = getValuesFor(this.getFilteredActivities(), getter)
        .orElse(new ArrayList<>()).stream()
        .filter(o -> !filterchain.containsFilter(o, type))
        .collect(
            Collectors.toList());
    collect.sort(String.CASE_INSENSITIVE_ORDER);
    return collect;
  }

  public List<String> completeTags(String query) {
    return getCompletionOptions(query, ActivityModel::getTags);
  }

  public List<String> completeProjects(String query) {
    return getCompletionOptions(query, ActivityModel::getProjects);
  }

  private List<String> getCompletionOptions(String query,
      Function<ActivityModel, List<String>> getter) {
    Optional<List<String>> reducedValues = getValuesFor(activityContainer.getActivities(), getter);
    List<String> options = reducedValues.orElse(new ArrayList<>());

    Set<String> returnOptions = new HashSet<>();
    returnOptions.add(query);
    for (String option : options) {
      if (option.toLowerCase().contains(query.toLowerCase())) {
        returnOptions.add(option);
      }
    }
    return new ArrayList<>(returnOptions);
  }

  private Optional<List<String>> getValuesFor(List<ActivityModel> toCheck,
      Function<ActivityModel, List<String>> getter) {
    Optional<List<String>> reduce = toCheck.stream().map(getter)
        .reduce(this::reduceStrings);
    reduce.ifPresent(strings -> strings.sort(String.CASE_INSENSITIVE_ORDER));
    return reduce;
  }
}
