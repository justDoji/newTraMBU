package be.doji.productivity.trambu.infrastructure;

import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
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
  private String hbm2Ddl_Auto;

  @Value("${entitymanager.packagesToScan}")
  private String packagesToScan;

  @Value("${hibernate.max_fetch_depth}")
  private String jdbc_batch_size;
  @Value("${hibernate.fetch.size}")
  private String jdbc_fetch_size;
  @Value("${hibernate.max_fetch_depth}")
  private String max_fetch_depth;

  private static final String PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH = "hibernate.max_fetch_depth";
  private static final String PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE = "hibernate.jdbc.fetch_size";
  private static final String PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
  private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";


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
  public SessionFactory sessionFactory() {
    return new LocalSessionFactoryBuilder(dataSource()).scanPackages(packagesToScan).
        addProperties(getHibernateProperties()).buildSessionFactory();
  }

  private Properties getHibernateProperties() {
    Properties hibernateProperties = new Properties();
    hibernateProperties.put("hibernate.dialect", dialect);
    hibernateProperties.put("hibernate.show_sql", showSql);
    hibernateProperties.put("hibernate.hbm2ddl.auto", hbm2Ddl_Auto);
    return hibernateProperties;
  }

  @Bean
  public HibernateTransactionManager transactionManager() {
    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(sessionFactory());
    return transactionManager;
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

  private HibernateJpaVendorAdapter vendorAdaptor() {
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setShowSql(true);
    return vendorAdapter;
  }

  private Properties jpaHibernateProperties() {

    Properties properties = new Properties();

    properties.put(PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH, max_fetch_depth);
    properties.put(PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE, jdbc_fetch_size);
    properties.put(PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE, jdbc_batch_size);
    properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, showSql);

    return properties;
  }

}
