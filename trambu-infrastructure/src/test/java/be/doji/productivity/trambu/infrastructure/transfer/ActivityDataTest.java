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
package be.doji.productivity.trambu.infrastructure.transfer;


import be.doji.productivity.trambu.domain.time.TimePoint;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ActivityDataTest {

  @Test
  public void toDomainObject_containsTitle() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Some title");

    Assertions.assertThat(activityData.toDomainObject()).isNotNull();
    Assertions.assertThat(activityData.toDomainObject().getTitle()).isEqualTo("Some title");
  }

  @Test
  public void toDomainObject_containsCompleted() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Some title");
    activityData.setCompleted(true);

    Assertions.assertThat(activityData.toDomainObject()).isNotNull();
    Assertions.assertThat(activityData.toDomainObject().isCompleted()).isTrue();
  }

  @Test
  public void toDomainObject_containsDeadline() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Implement deadlines in core");
    activityData.setDeadline("09/05/2019");

    Assertions.assertThat(activityData.toDomainObject()).isNotNull();
    Assertions.assertThat(TimePoint.isSameDate(activityData.toDomainObject().getDeadline().get(),
        TimePoint.fromString("09/05/2019"))).isTrue();

  }
}