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

import be.doji.productivity.trambu.infrastructure.converter.ActivityConverter;
import be.doji.productivity.trambu.infrastructure.converter.ActivityDataConverter;
import be.doji.productivity.trambu.infrastructure.converter.LogConverter;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileWriter {

  private static final String ERROR_OPENING_FILE = "Error while opening output file";
  private final ActivityDatabaseRepository activityRepository;
  private final ActivityDataConverter dataConverter;
  private final LogConverter logConverter;

  private static final Logger LOG = LoggerFactory.getLogger(FileWriter.class);

  public FileWriter(@Autowired ActivityDatabaseRepository repository,
      @Autowired ActivityDataConverter dataConverter,
      @Autowired LogConverter logConverter) {
    this.activityRepository = repository;
    this.dataConverter = dataConverter;
    this.logConverter = logConverter;
  }

  public void writeActivtiesToFile(File file) throws IOException {
    writeActivtiesToFile(file.toPath());
  }

  public void writeActivtiesToFile(Path path) throws IOException {
    List<String> activities = activityRepository.findAll().
        stream()
        .map(dataConverter::parse)
        .map(ActivityConverter::write)
        .collect(Collectors.toList());
    Files.write(path, activities, StandardOpenOption.TRUNCATE_EXISTING);
  }

  public void writeTimeLogsToFile(File file) throws IOException {
    writeTimeLogsToFile(file.toPath());
  }

  public void writeTimeLogsToFile(Path path) throws IOException {
    throwErrorIfFileDoesNotExist(path, ERROR_OPENING_FILE);
    List<String> fileLines = activityRepository.findAll().stream()
        .map(logConverter::write)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
    Files.write(path, fileLines, StandardOpenOption.TRUNCATE_EXISTING);
  }

  private void throwErrorIfFileDoesNotExist(Path path, String baseErrorMessage) {
    if (path == null || !path.toFile().exists()) {
      String errorMessage = baseErrorMessage + ": " + (path != null ? path.toString() : "null");
      LOG.error(errorMessage);
      throw new IllegalArgumentException(errorMessage);
    }
  }
}
