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
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The purpose of this class is to allow spring to componentscan automatically. It is a hassle to
 * get rid of this, and changing it will make the code a lot more verbose.
 */

@SpringBootConfiguration()
@ComponentScan(basePackages = "be.doji.productivity.trambu.infrastructure")
@EnableJpaRepositories(basePackages = "be.doji.productivity.trambu.infrastructure.repository")
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

  @Value("${db.driver}")
  private String driver;

  @Value("${db.password}")
  private String password;

  @Value("${db.url}")
  private String url;

  @Value("${db.username}")
  private String username;

  @Value("${hibernate.dialect}")
  private String dialect;

  @Value("${hibernate.show_sql}")
  private String showSql;

  @Value("${hibernate.hbm2ddl.auto}")
  private String hbm2DdlAuto;

  @Value("${entitymanager.packagesToScan}")
  private String packagesToScan;

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(driver);
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    return dataSource;
  }

  @Bean
  public LocalSessionFactoryBean sessionFactory() {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource());
    sessionFactory.setPackagesToScan(packagesToScan);
    Properties hibernateProperties = new Properties();
    hibernateProperties.put("hibernate.dialect", dialect);
    hibernateProperties.put("hibernate.show_sql", showSql);
    hibernateProperties.put("hibernate.hbm2ddl.auto", hbm2DdlAuto);
    sessionFactory.setHibernateProperties(hibernateProperties);

    return sessionFactory;
  }

  @Bean
  public HibernateTransactionManager transactionManager() {
    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(sessionFactory().getObject());
    return transactionManager;
  }


}
