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
package be.doji.productivity.trambu.infrastructure.file;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.infrastructure.converter.ActivityConverter;
import be.doji.productivity.trambu.infrastructure.converter.ActivityDataConverter;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
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


  public FileLoader(@Autowired ActivityDatabaseRepository activityDatabaseRepository) {
    this.activityDatabaseRepository = activityDatabaseRepository;
  }

  public void loadTodoFileActivities(String todoFileLocation) throws IOException {
    Path path = Paths.get(todoFileLocation);
    loadTodoFileActivities(path);
  }

  public void loadTodoFileActivities(File file) throws IOException {
    loadTodoFileActivities(file.toPath());
  }

  private void loadTodoFileActivities(Path path) throws IOException {
    activityDatabaseRepository.deleteAll();
    List<String> todoFileLines = Files.readAllLines(path);
    for (String line : todoFileLines) {
      Activity parsedActivity = ActivityConverter.parse(line);
      ActivityData convertedActivityData = ActivityDataConverter.parse(parsedActivity);
      activityDatabaseRepository.save(convertedActivityData);
    }
  }


  public void loadTimeLogFile(File file) throws IllegalArgumentException {
    if (!file.exists()) {
      String errorMessage = ERROR_LOADING_FILE + ": " + file.getName();
      LOG.error(errorMessage);
      throw new IllegalArgumentException(errorMessage);
    }



  }
}
