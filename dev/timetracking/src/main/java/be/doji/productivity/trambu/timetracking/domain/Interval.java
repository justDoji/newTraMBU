package be.doji.productivity.trambu.timetracking.domain;

import be.doji.productivity.trambu.kernel.annotations.AggregateRoot;
import java.util.UUID;

@AggregateRoot
public class Interval {

  private UUID identifier = UUID.randomUUID();
  private Occupation occupation;
  private PointInTime start;
  private PointInTime end;

  private Interval() {}

  public static Builder builder() {
    return new Builder();
  }

  public UUID getIdentifier() {
    return identifier;
  }

  private Interval setIdentifier(UUID identifier) {
    this.identifier = identifier;
    return this;
  }

  private Interval setOccupation(
      Occupation occupation) {
    this.occupation = occupation;
    return this;
  }

  private Interval setStart(PointInTime start) {
    this.start = start;
    return this;
  }

  private Interval setEnd(PointInTime end) {
    this.end = end;
    return this;
  }

  public String getOccupationName() {
    return this.occupation.getName();
  }

  public static class Builder {

    private UUID rootIdentifier;
    private String occupationName;
    private PointInTime start = PointInTime.now();
    private PointInTime end;

    public Builder() {
    }

    public Builder rootIdentifier(UUID rootIdentifier) {
      this.rootIdentifier = rootIdentifier;
      return this;
    }

    public Builder occupationName(String occupationName) {
      this.occupationName = occupationName;
      return this;
    }

    public Interval build() {
      Interval interval = new Interval();
      if (rootIdentifier != null) {
        interval.setIdentifier(rootIdentifier);
      }

      Occupation occupation = new Occupation(interval.getIdentifier(), occupationName);
      interval.setOccupation(occupation);

      interval.setStart(start);
      interval.setEnd(end);
      return interval;
    }
  }
}
