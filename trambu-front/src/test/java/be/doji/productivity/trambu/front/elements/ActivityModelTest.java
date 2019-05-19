package be.doji.productivity.trambu.front.elements;

import static org.junit.Assert.*;

import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ActivityModelTest {

  /*
  * Test for bug report: https://github.com/justDoji/newTraMBU/issues/1#issue-445760387
   */
  @Test
  public void setProjects() {
    ActivityModel model = new ActivityModel();
    Assertions.assertThat(model.getProjects()).isEmpty();

    model.setProjects(Arrays.asList("One", "Two"));
    Assertions.assertThat(model.getProjects()).hasSize(2);

    //Bug occured when setting the projects twice
    model.setProjects(Arrays.asList("One", "Two"));
    Assertions.assertThat(model.getProjects()).hasSize(2);
  }

  /*
   * Test for bug report: https://github.com/justDoji/newTraMBU/issues/1#issue-445760387
   */
  @Test
  public void setTags() {
    ActivityModel model = new ActivityModel();
    Assertions.assertThat(model.getTags()).isEmpty();

    model.setTags(Arrays.asList("One", "Two"));
    Assertions.assertThat(model.getTags()).hasSize(2);

    //Bug occured when setting the tags twice
    model.setTags(Arrays.asList("One", "Two"));
    Assertions.assertThat(model.getTags()).hasSize(2);
  }
}