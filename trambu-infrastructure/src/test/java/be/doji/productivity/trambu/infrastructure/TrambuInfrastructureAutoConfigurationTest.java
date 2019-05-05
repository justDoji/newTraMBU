package be.doji.productivity.trambu.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import be.doji.productivity.trambu.infrastructure.repository.ActivityRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrambuInfrastructureAutoConfigurationTest {


  @Autowired
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  private ActivityRepository autowiredService;

  @Test
  public void autowiringWorks() {
    assertThat(autowiredService).isNotNull();
  }

}
