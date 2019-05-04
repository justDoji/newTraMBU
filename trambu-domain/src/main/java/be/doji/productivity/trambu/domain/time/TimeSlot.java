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
