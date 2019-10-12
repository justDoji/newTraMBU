package be.doji.productivity.trambu.timetracking.domain;

import be.doji.productivity.trambu.kernel.annotations.AggregateRoot;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Value object representing what is being done in a certain {@link PointInTime}
 */
@AggregateRoot
public class Occupation {

  private UUID identifier;
  private String name;
  private List<Interval> intervals = new ArrayList<>();

  Occupation() {

  }

  public UUID getIdentifier() {
    return identifier;
  }

  public void setIdentifier(UUID identifier) {
    if (identifier != null) {
      this.identifier = identifier;
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  private void addInterval(PointInTime start, PointInTime end) {
    this.intervals.add(new Interval(this.getIdentifier(), start, end));
  }

  public void addInterval(Interval interval) {
    this.intervals.add(interval);
  }

  public void setIntervals(List<Interval> intervals) {
    if (intervals != null) {
      this.intervals = intervals;
    }
  }

  public static Builder builder(OccupationRepository repository) {
    return new Builder(repository);
  }

  public double getTimeSpentInHours() {
    return intervals.stream().map(Interval::getTimeSpanInHours).mapToDouble(Double::doubleValue)
        .sum();
  }

  public final List<Interval> getIntervals() {
    return new ArrayList<>(this.intervals);
  }

  public static class Builder {

    private final OccupationRepository repository;

    private List<Interval> intervals = new ArrayList<>();
    private String occupationName;
    private UUID rootIdentifier;

    Builder(OccupationRepository repository) {
      this.repository = repository;
    }

    public Builder rootIdentifier(UUID rootIdentifier) {
      this.rootIdentifier = rootIdentifier;
      return this;
    }

    public Builder name(String occupationName) {
      this.occupationName = occupationName;
      return this;
    }

    public Builder interval(PointInTime start, PointInTime end) {
      Interval interval = new Interval(start, end);
      this.intervals.add(interval);
      return this;
    }

    public Occupation build() {
      Occupation occupation = new Occupation();
      occupation.setIdentifier(this.rootIdentifier);
      occupation.setName(occupationName);
      intervals.forEach(interval -> interval.setOccupationId(this.rootIdentifier));
      occupation.setIntervals(this.intervals);

      repository.save(occupation);
      return occupation;
    }
  }
}
