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
package be.doji.productivity.trambu.timetracking.domain;

import static org.mockito.Mockito.when;

import be.doji.productivity.trambu.kernel.time.PointInTime;
import be.doji.productivity.trambu.timetracking.domain.time.TimeService;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.TimeZone;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.quality.Strictness;

public class TimeServiceRule extends JUnitRule {

  private TimeService timeService;
  private Clock mockClock;

  private LocalDateTime timeToSet = LocalDateTime.now();

  public TimeServiceRule() {
    super(Plugins.getMockitoLogger(), Strictness.WARN);
    mockClock = Mockito.mock(Clock.class);
    timeService = Mockito.mock(TimeService.class);
  }


  public void time(LocalDateTime timeToSet) {
    this.timeToSet = timeToSet;
    setClock(timeToSet);
  }

  public TimeService service() {
    return timeService;
  }

  @Override
  public Statement apply(Statement statement, FrameworkMethod frameworkMethod, Object o) {
    return super.apply(decorate(statement), frameworkMethod, o);
  }

  public Statement decorate(Statement statement) {
    return new Statement() {
      public void evaluate() throws Throwable {
        setClock(timeToSet);
        try {
          statement.evaluate();
        } catch (
            Throwable throwable) {
          throw throwable;
        }
      }
    };
  }

  private void setClock(LocalDateTime timeToSet) {
    Instant instantToSet = timeToSet.toInstant(ZoneOffset.of("+01:00"));
    PointInTime pointInTime = new PointInTime(timeToSet);

    when(mockClock.instant()).thenReturn(instantToSet);
    when(mockClock.getZone()).thenReturn(ZoneId.of(TimeZone.getDefault().getID()));
    when(timeService.getSharedClock()).thenReturn(mockClock);
    when(timeService.now()).thenReturn(pointInTime);
  }
}
