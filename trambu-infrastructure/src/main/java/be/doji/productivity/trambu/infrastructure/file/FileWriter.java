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
