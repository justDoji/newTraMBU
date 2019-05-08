package be.doji.productivity.trambu.infrastructure.repository;

import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityDatabaseRepositoryTest {

  @Autowired
  ActivityDatabaseRepository repository;
  @Autowired
  DataSource dataSource;

  @Before
  public void setUp() {
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    ClassPathResource createActivityDataTable = new ClassPathResource(
        "repository/DML_create_activity.sql");
    populator.addScript(createActivityDataTable);
    DatabasePopulatorUtils.execute(populator, dataSource);
  }

  @Test
  public void repository_CanBeAutowired() {
    Assertions.assertThat(repository).isNotNull();
    Assertions.assertThat(repository.findAll()).hasSize(0);
  }

  @Test
  public void repository_saveEntity() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Save me");
    activityData.setCompleted(true);

    ActivityData saved = repository.save(activityData);

    Assertions.assertThat(repository.findById(saved.getId())).isPresent();

    ActivityData savedActivity = repository.findById(saved.getId()).get();
    Assertions.assertThat(savedActivity.getTitle()).isEqualTo("Save me");
    Assertions.assertThat(savedActivity.isCompleted()).isTrue();
  }


}