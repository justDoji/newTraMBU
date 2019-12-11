package be.doji.productivity.trambu.timetracking.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:application.properties"})
@ImportAutoConfiguration({InfrastructureAutoConfiguration.class})
public class TimetrackingApplication {

  private static final Logger LOG = LoggerFactory.getLogger(TimetrackingApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(TimetrackingApplication.class);
  }

}
