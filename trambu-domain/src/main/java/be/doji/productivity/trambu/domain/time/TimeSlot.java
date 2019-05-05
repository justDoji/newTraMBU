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
package be.doji.productivity.trambu.domain.time;

public class TimeSlot {

  private final TimePoint start;
  private final TimePoint end;

  public TimeSlot(TimePoint plannedStart, TimePoint plannedEnd) {
    this.start = plannedStart;
    this.end = plannedEnd;
  }

  public TimePoint getStart() {
    return this.start;
  }

  public TimePoint getEnd() {
    return this.end;
  }

  public boolean startsOn(TimePoint reference) {
    return TimePoint.isSameDate(this.start, reference);
  }

  public boolean endsOn(TimePoint reference) {
    return TimePoint.isSameDate(this.end, reference);
  }

  public static TimeSlot between(TimePoint from, TimePoint to) {
    return TimePoint.isBefore(from, to) ? new TimeSlot(from, to) : new TimeSlot(to, from);
  }

  public boolean contains(TimePoint toCheck) {
    return TimePoint.isBeforeOrEqual(toCheck, end) && TimePoint.isBeforeOrEqual(start, toCheck);
  }
}
