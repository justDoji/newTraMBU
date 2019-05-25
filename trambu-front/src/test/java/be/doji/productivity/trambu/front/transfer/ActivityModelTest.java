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
package be.doji.productivity.trambu.front.transfer;

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