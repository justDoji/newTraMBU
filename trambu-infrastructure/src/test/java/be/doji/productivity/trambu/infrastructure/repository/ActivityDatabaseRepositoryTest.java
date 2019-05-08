package be.doji.productivity.trambu.infrastructure.repository;

import static org.junit.Assert.*;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityDatabaseRepositoryTest {

  @Autowired ActivityDatabaseRepository repository;

  @Test
  public void repository_CanBeAutowired() {
    Assertions.assertThat(repository).isNotNull();
    Assertions.assertThat(repository.findAll()).hasSize(0);
  }

  


}