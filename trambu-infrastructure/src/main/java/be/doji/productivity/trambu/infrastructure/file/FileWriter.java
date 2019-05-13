package be.doji.productivity.trambu.infrastructure.file;

import java.io.File;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class FileWriter {

  public void writeActivtiesToFile(File file) {
    writeActivtiesToFile(file.toPath());
  }

  public void writeActivtiesToFile(Path path) {

  }
}
