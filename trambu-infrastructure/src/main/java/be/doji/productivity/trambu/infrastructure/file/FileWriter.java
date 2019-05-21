/**
 * GNU Affero General Public License Version 3
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package be.doji.productivity.trambu.infrastructure.file;

import be.doji.productivity.trambu.infrastructure.converter.ActivityConverter;
import be.doji.productivity.trambu.infrastructure.converter.ActivityDataConverter;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileWriter {

  private final ActivityDatabaseRepository activityRepository;

  public FileWriter(@Autowired ActivityDatabaseRepository repository) {
    this.activityRepository = repository;
  }

  public void writeActivtiesToFile(File file) throws IOException {
    writeActivtiesToFile(file.toPath());
  }

  public void writeActivtiesToFile(Path path) throws IOException {
    List<String> activities = activityRepository.findAll().
        stream()
        .map(ActivityDataConverter::parse)
        .map(ActivityConverter::write)
        .collect(Collectors.toList());
    Files.write(path, activities);
  }
}
