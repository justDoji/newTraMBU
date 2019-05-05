package be.doji.productivity.trambu.infrastructure;

import be.doji.productivity.trambu.infrastructure.repository.ActivityRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * The purpose of this class is to allow spring to componentscan automatically. It is a hassle to
 * get rid of this, and changing it will make the code a lot more verbose.
 */

@SpringBootConfiguration()
@ComponentScan(basePackages = "be.doji.productivity.trambu.infrastructure.repository")
public class TrambuInfrastructureAutoConfiguration {

  private static final Log log = LogFactory.getLog(TrambuInfrastructureAutoConfiguration.class);

  public TrambuInfrastructureAutoConfiguration() {
    log.info("Created");
  }

  /**
   * Configures AuthTokenService if missing
   */
  @Bean
  @ConditionalOnMissingBean(ActivityRepository.class)
  public ActivityRepository activityRepository() {

    log.info("Configuring AuthTokenService");
    return new ActivityRepository();
  }

}
