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
package be.doji.productivity.trambu.front;

import be.doji.productivity.trambu.infrastructure.TrambuInfrastructureAutoConfiguration;
import be.doji.productivity.trambu.infrastructure.file.FileLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:application.properties"})
@ImportAutoConfiguration({TrambuInfrastructureAutoConfiguration.class})
public class TrambuWebApplication {


  public static final Path PATH_CONFIGURATION_DIRECTORY = Paths
      .get(System.getProperty("user.home"), ".trambu");

  public static void main(String[] args) {
    SpringApplication.run(TrambuWebApplication.class, args);
  }

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") @Autowired
  private FileLoader fileLoader;

  @PostConstruct
  public void loadFileData() throws IOException {
    if (!PATH_CONFIGURATION_DIRECTORY.toFile().exists()) {
      Files.createDirectories(PATH_CONFIGURATION_DIRECTORY);
    }

    Path todoLocation = PATH_CONFIGURATION_DIRECTORY.resolve("TODO.txt");
    if (!todoLocation.toFile().exists()) {
      Files.createFile(todoLocation);
    }

    fileLoader.loadTodoFileActivities(todoLocation.toFile());
  }

}
