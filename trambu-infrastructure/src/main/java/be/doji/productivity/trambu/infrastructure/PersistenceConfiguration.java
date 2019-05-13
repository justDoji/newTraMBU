/**
 * MIT License
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package be.doji.productivity.trambu.infrastructure;

import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource(value = {"classpath:database.properties"})
@EnableJpaRepositories
@EnableTransactionManagement
public class PersistenceConfiguration {


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

  @Value("${hibernate.jdbc.batch_size}")
  private String jdbcBatchSize;
  @Value("${hibernate.jdbc.fetch_size}")
  private String jdbcFetchSize;
  @Value("${hibernate.max_fetch_depth}")
  private String maxFetchDepth;

  private static final String PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH = "hibernate.max_fetch_depth";
  private static final String PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE = "hibernate.jdbc.fetch_size";
  private static final String PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
  private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";

  @Bean
  public JpaTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setDataSource(dataSource());
    return transactionManager;
  }

  @Bean
  @ConditionalOnMissingBean(DataSource.class)
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(driver);
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    return dataSource;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactory.setJpaVendorAdapter(vendorAdaptor());
    entityManagerFactory.setDataSource(dataSource());
    entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
    entityManagerFactory.setPackagesToScan(packagesToScan);
    entityManagerFactory.setJpaProperties(jpaHibernateProperties());

    return entityManagerFactory;
  }

  @PostConstruct
  public void initializeDatabase() {
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

    populator.addScript(getActivityDML());
    populator.addScript(getTagDML());
    populator.addScript(getProjectDML());
    populator.addScript(getLogPointDML());

    DatabasePopulatorUtils.execute(populator, dataSource());
  }

  private ClassPathResource getActivityDML() {
    return new ClassPathResource("repository/DML_create_activity.sql");
  }

  private ClassPathResource getTagDML() {
    return new ClassPathResource("repository/DML_create_tag.sql");
  }

  private ClassPathResource getProjectDML() {
    return new ClassPathResource("repository/DML_create_project.sql");
  }

  private ClassPathResource getLogPointDML() {
    return new ClassPathResource("repository/DML_create_logpoint.sql");
  }

  private HibernateJpaVendorAdapter vendorAdaptor() {
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setShowSql(true);
    return vendorAdapter;
  }

  private Properties jpaHibernateProperties() {

    Properties properties = new Properties();

    properties.put(PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH, maxFetchDepth);
    properties.put(PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE, jdbcFetchSize);
    properties.put(PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE, jdbcBatchSize);
    properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, showSql);

    return properties;
  }

}
