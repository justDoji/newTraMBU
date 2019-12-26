package be.doji.productivity.trambu.planning.infra;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootConfiguration()
@ComponentScan(basePackages = "be.doji.productivity.trambu.planning")
@EnableTransactionManagement
public class InfrastructureAutoConfiguration {

  private static final Log log = LogFactory.getLog(InfrastructureAutoConfiguration.class);

  InfrastructureAutoConfiguration() {
    log.info("Configuration created");
  }

}
