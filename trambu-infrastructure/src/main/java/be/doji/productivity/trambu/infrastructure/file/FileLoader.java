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
package be.doji.productivity.trambu.infrastructure.file;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.infrastructure.converter.ActivityConverter;
import be.doji.productivity.trambu.infrastructure.converter.ActivityDataConverter;
import be.doji.productivity.trambu.infrastructure.converter.LogConverter;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.LogPointData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileLoader {

  private static final String ERROR_LOADING_FILE = "Error while loading timelog file";
  private static final Logger LOG = LoggerFactory.getLogger(FileLoader.class);

  private final ActivityDatabaseRepository activityDatabaseRepository;
  private final LogConverter logConverter;
  private final ActivityDataConverter dataConverter;
  private final ActivityConverter activityConverter;


  public FileLoader(@Autowired ActivityDatabaseRepository activityDatabaseRepository,
      @Autowired LogConverter logConverter,
      @Autowired ActivityDataConverter converter,
      @Autowired ActivityConverter activityConverter) {
    this.activityDatabaseRepository = activityDatabaseRepository;
    this.logConverter = logConverter;
    this.dataConverter = converter;
    this.activityConverter = activityConverter;
  }

  public void loadTodoFileActivities(File file) throws IOException {
    loadTodoFileActivities(file.toPath());
  }

  private void loadTodoFileActivities(Path path) throws IOException {
    throwErrorIfFileDoesNotExist(path.toFile(), ERROR_LOADING_FILE);

    activityDatabaseRepository.deleteAll();
    List<String> todoFileLines = Files.readAllLines(path);
    for (String line : todoFileLines) {
      Activity parsedActivity = activityConverter.parse(line);

      Path commentsFile = path
          .resolveSibling(parsedActivity.getReferenceKey() + "_comments.txt");
      if (commentsFile.toFile().exists()) {
        parsedActivity.setComments(
            Files.readAllLines(commentsFile).stream().map(s -> s + System.lineSeparator())
                .collect(Collectors.joining()));
      }
      ActivityData convertedActivityData = dataConverter.parse(parsedActivity);
      activityDatabaseRepository.save(convertedActivityData);
    }
  }


  /**
   * @param file the file containing timelog data
   * @throws IllegalArgumentException when the file is not found
   * @throws IOException when a problem occurs reading the file
   */
  public void loadTimeLogFile(File file) throws IOException {
    throwErrorIfFileDoesNotExist(file, ERROR_LOADING_FILE);

    for (String line : Files.readAllLines(file.toPath())) {
      LogPointData pointData = logConverter.parse(line);
      pointData.getActivity()
          .ifPresent(activity -> updateTimePoint(pointData, activity.getReferenceKey()));
    }
    LOG.info("Timelog data loaded.");
  }

  private void updateTimePoint(LogPointData pointData, String referenceKey) {
    Optional<ActivityData> byReferenceKey = activityDatabaseRepository
        .findByReferenceKey(referenceKey);
    if (byReferenceKey.isPresent()) {
      byReferenceKey.get().addTimelog(pointData);
      activityDatabaseRepository.save(byReferenceKey.get());
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
