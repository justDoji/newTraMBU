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
