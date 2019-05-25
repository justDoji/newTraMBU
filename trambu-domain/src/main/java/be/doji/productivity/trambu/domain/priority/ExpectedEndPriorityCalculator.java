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
package be.doji.productivity.trambu.domain.priority;

import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.time.TimePoint;
import java.time.temporal.ChronoUnit;

public class ExpectedEndPriorityCalculator implements PriorityCalculator {

  private static final int OFFSET_IN_DAYS = 2;
  private static final int RANGE_MAX = 150;
  private static final double INCREMENT = -((double)RANGE_MAX / Priority.values().length);


  @Override
  public Priority calculatePriority(Activity activity) {
    double priorityCalculator = RANGE_MAX;
    TimePoint addedOffset = TimePoint.now();

    while (activity.getAssignedTimeSlot().contains(addedOffset) && priorityCalculator > 0) {
      addedOffset = addedOffset.add(OFFSET_IN_DAYS, ChronoUnit.DAYS);
      priorityCalculator += INCREMENT;
    }

    priorityCalculator = Math.abs(priorityCalculator);
    priorityCalculator = Math.max(priorityCalculator, 0);
    int index = (int) (priorityCalculator / INCREMENT);

    return index < Priority.values().length ? Priority.values()[index]
        : Priority.values()[Priority.values().length - 1];
  }
}
