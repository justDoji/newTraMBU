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
package be.doji.productivity.trambu.timetracking.infra.time;

import be.doji.productivity.trambu.timetracking.domain.time.TimeService;
import java.time.Clock;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {

  private final Clock sharedClock;

  public TimeServiceImpl() {
    sharedClock = Clock.systemDefaultZone();
  }

  public TimeServiceImpl(Clock sharedClock) {
    this.sharedClock = sharedClock;
  }

  @Override
  public Clock getSharedClock() {
    return sharedClock;
  }

}