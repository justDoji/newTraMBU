/**
 * TraMBU - an open time management tool
 *
 *     Copyright (C) 2019  Stijn Dejongh
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     For further information on usage, or licensing, contact the author
 *     through his github profile: https://github.com/justDoji
 */
package be.doji.productivity.trambu.timetracking.infra;

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
