package be.doji.productivity.trambu.front.controller.state;

import static be.doji.productivity.trambu.front.TrambuWebApplication.PATH_CONFIGURATION_DIRECTORY;

import be.doji.productivity.trambu.front.controller.exception.InvalidReferenceException;
import be.doji.productivity.trambu.front.converter.ActivityModelConverter;
import be.doji.productivity.trambu.front.transfer.ActivityModel;
import be.doji.productivity.trambu.infrastructure.file.FileLoader;
import be.doji.productivity.trambu.infrastructure.file.FileWriter;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Holds the Activities in memory
 */
@Service
public class ActivityModelContainer {

  private static final Logger LOG = LoggerFactory.getLogger(ActivityModelContainer.class);

  private final FileWriter writer;
  private final FileLoader loader;
  private final ActivityModelConverter modelConverter;
  private final ActivityDatabaseRepository repository;

  private File todoFile;
  private File timeFile;

  private List<ActivityModel> activities = new ArrayList<>();

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  public ActivityModelContainer(
      @Autowired FileWriter writer,
      @Autowired FileLoader loader,
      @Autowired ActivityModelConverter modelConverter,
      @Autowired ActivityDatabaseRepository repository) {
    this.writer = writer;
    this.loader = loader;
    this.modelConverter = modelConverter;
    this.repository = repository;
    LOG.debug("ActivityModelContainer loaded");
  }

  @PostConstruct
  public void init() {
    this.todoFile = PATH_CONFIGURATION_DIRECTORY.resolve("TODO.txt").toFile();
    this.timeFile = PATH_CONFIGURATION_DIRECTORY.resolve("TIMELOG.txt").toFile();
    this.loadActivities();
  }

  public void loadActivities() {
    try {
      loadActivitiesFromFile();
      loadTimelogsFromFile();
      updateActivities();
    } catch (IOException e) {
      LOG.error("Error while saving activities to file: {}", e.getMessage());
    }
  }

  private void loadActivitiesFromFile() throws IOException {
    if (todoFile != null && todoFile.exists()) {
      loader.loadTodoFileActivities(todoFile);
    } else {
      LOG.error("No todo file found!");
    }
  }

  private void loadTimelogsFromFile() throws IOException {
    if (timeFile != null && timeFile.exists()) {
      loader.loadTimeLogFile(timeFile);
    } else {
      LOG.error("No timelog file found!");
    }
  }

  private void updateActivities() {
    this.activities = repository.findAll().stream()
        .map(modelConverter::parse)
        .collect(Collectors.toList());
  }

  public List<ActivityModel> getActivities() {
    return this.activities;
  }

  public void saveActivities() {
    for (ActivityModel activityModel : getActivities()) {
      repository.findByReferenceKey(activityModel.getReferenceKey())
          .ifPresent(repository::delete);
      ActivityData savedData = repository.save(modelConverter.toDatabase(activityModel));
      activityModel.setDataBaseId(savedData.getId());
    }

    writeToFile();
    LOG.info("Saving complete");
  }

  void writeToFile() {
    try {
      writeActivities();
      writeTimelogs();
    } catch (IOException e) {
      LOG.error("Error while saving activities to file: {}", e.getMessage());
    }
  }

  private void writeActivities() throws IOException {
    if (todoFile != null && todoFile.exists()) {
      writer.writeActivtiesToFile(todoFile);
    } else {
      LOG.error("No output file found!");
    }
  }

  private void writeTimelogs() throws IOException {
    if (timeFile != null && timeFile.exists()) {
      writer.writeTimeLogsToFile(timeFile);
    } else {
      LOG.info("No output file found!");
    }
  }

  /* Activity Control Methods*/

  public void createActivity() {
    ActivityModel newActivity = new ActivityModel();
    this.activities.add(newActivity);
  }

  public void deleteActivity(String referenceKey) throws InvalidReferenceException {
    if (StringUtils.isBlank(referenceKey) || findModelInList(referenceKey) == null) {
      throw new InvalidReferenceException(MessageFormat
          .format("Invalid Reference key {0}: key was empty, or activity is not known",
              referenceKey));
    }

    Optional<ActivityData> databaseModel = repository.findByReferenceKey(referenceKey);
    databaseModel.ifPresent(repository::delete);
    this.activities.remove(findModelInList(referenceKey));
    saveActivities();
    LOG.info("Activity deleted: {}" + referenceKey);
  }

  public ActivityModel getActivity(String referenceKey) {
    return findModelInList(referenceKey);
  }

  private ActivityModel findModelInList(String reference) {
    //TODO: refactor to HashMap for performance reasons
    return this.activities.stream().filter(m -> m.getReferenceKey().equals(reference))
        .collect(Collectors.toList()).get(0);
  }

  void clearActivities() {
    this.activities.clear();
  }


  /* Field Accessors*/
  public File getTodoFile() {
    return todoFile;
  }

  public void setTodoFile(File todoFile) {
    this.todoFile = todoFile;
  }

  public File getTimeFile() {
    return timeFile;
  }

  public void setTimeFile(File timeFile) {
    this.timeFile = timeFile;
  }

  public void reset() {
    this.clearActivities();
    repository.deleteAll();
    this.loadActivities();
  }


}
