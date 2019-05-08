/**
 * MIT License
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package be.doji.productivity.trambu.infrastructure;

import be.doji.productivity.trambu.infrastructure.repository.ActivityRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The purpose of this class is to allow spring to componentscan automatically. It is a hassle to
 * get rid of this, and changing it will make the code a lot more verbose.
 */

@SpringBootConfiguration()
@Import({PersistenceConfiguration.class})
@ComponentScan(basePackages = "be.doji.productivity.trambu.infrastructure")
@EnableTransactionManagement
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
