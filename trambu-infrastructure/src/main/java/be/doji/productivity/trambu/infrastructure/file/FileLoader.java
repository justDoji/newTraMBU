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
package be.doji.productivity.trambu.infrastructure.file;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.infrastructure.converter.ActivityConverter;
import be.doji.productivity.trambu.infrastructure.converter.ActivityDataConverter;
import be.doji.productivity.trambu.infrastructure.converter.LogParser;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileLoader {

  public static final String ERROR_LOADING_FILE = "Error while loading timelog file";
  private final Logger LOG = LoggerFactory.getLogger(FileLoader.class);
  private final ActivityDatabaseRepository activityDatabaseRepository;
  private final LogParser logParser;


  public FileLoader(@Autowired ActivityDatabaseRepository activityDatabaseRepository,
      @Autowired LogParser logParser) {
    this.activityDatabaseRepository = activityDatabaseRepository;
    this.logParser = logParser;
  }

  public void loadTodoFileActivities(String todoFileLocation) throws IOException {
    Path path = Paths.get(todoFileLocation);
    loadTodoFileActivities(path);
  }

  public void loadTodoFileActivities(File file) throws IOException {
    loadTodoFileActivities(file.toPath());
  }

  private void loadTodoFileActivities(Path path) throws IOException {
    throwErrorIfFileDoesNotExist(path.toFile(), ERROR_LOADING_FILE);

    activityDatabaseRepository.deleteAll();
    List<String> todoFileLines = Files.readAllLines(path);
    for (String line : todoFileLines) {
      Activity parsedActivity = ActivityConverter.parse(line);
      ActivityData convertedActivityData = ActivityDataConverter.parse(parsedActivity);
      activityDatabaseRepository.save(convertedActivityData);
    }
  }


  /**
   * @param file the file containing timelog data
   * @throws IllegalArgumentException when the file is not found
   * @throws IOException when a problem occurs reading the file
   */
  public void loadTimeLogFile(File file) throws IllegalArgumentException, IOException {
    throwErrorIfFileDoesNotExist(file, ERROR_LOADING_FILE);

    for (String line : Files.readAllLines(file.toPath())) {
      LogPointData pointData = logParser.parse(line);
      pointData.getActivity().ifPresent(activity -> {
        activity.addTimelog(pointData);
        activityDatabaseRepository.save(activity);
      });
    }
  }

  private void throwErrorIfFileDoesNotExist(File file, String baseErrorMessage) {
    if (!file.exists()) {
      String errorMessage = baseErrorMessage + ": " + file.getName();
      LOG.error(errorMessage);
      throw new IllegalArgumentException(errorMessage);
    }
  }
}
