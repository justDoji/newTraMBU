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
package be.doji.productivity.trambu.infrastructure.repository;

import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityDatabaseRepositoryTest {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  ActivityDatabaseRepository repository;

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

  @After
  public void cleanUp() {
    repository.deleteAll();
  }


}