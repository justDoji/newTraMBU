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
package be.doji.productivity.trambu.zulma.infrastructure.file;

import be.doji.productivity.trambu.zulma.domain.activity.Activity;
import be.doji.productivity.trambu.zulma.infrastructure.converter.ActivityConverter;
import be.doji.productivity.trambu.zulma.infrastructure.converter.ActivityDataConverter;
import be.doji.productivity.trambu.zulma.infrastructure.converter.LogConverter;
import be.doji.productivity.trambu.zulma.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.zulma.infrastructure.transfer.ActivityData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
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
  private final ActivityConverter activityConverter;

  private static final Logger LOG = LoggerFactory.getLogger(FileWriter.class);

  public FileWriter(@Autowired ActivityDatabaseRepository repository,
      @Autowired ActivityDataConverter dataConverter,
      @Autowired LogConverter logConverter,
      @Autowired ActivityConverter activityConverter) {
    this.activityRepository = repository;
    this.dataConverter = dataConverter;
    this.logConverter = logConverter;
    this.activityConverter = activityConverter;
  }

  public void writeActivtiesToFile(File file) throws IOException {
    writeActivtiesToFile(file.toPath());
  }

  public void writeActivtiesToFile(Path path) throws IOException {
    List<String> lines = new ArrayList<>();
    for (ActivityData data : activityRepository.findAll()) {
      Activity parsedData = dataConverter.parse(data);
      lines.add(activityConverter.write(parsedData));
      if (StringUtils.isNotBlank(parsedData.getComments())) {
        Path commentFile = path.resolveSibling(data.getReferenceKey() + "_comments.txt");
        Files.deleteIfExists(commentFile);
        Files.createFile(commentFile);
        Files.write(commentFile, Collections.singletonList(data.getComments()),
            StandardOpenOption.TRUNCATE_EXISTING);
      }
    }
    Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
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
