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


  private OccupationRepository repository;

  private UUID identifier = UUID.randomUUID();
  private String name;
  private List<Interval> intervals = new ArrayList<>();

  private Occupation(OccupationRepository repository) {
    this.repository = repository;
  }

  public UUID getIdentifier() {
    return identifier;
  }

  private void setIdentifier(UUID identifier) {
    if(identifier != null) {
      this.identifier = identifier;
    }
  }

  public String getName() {
    return name;
  }

  private void setName(String name) {
    this.name = name;
  }

  private void addInterval(PointInTime start, PointInTime end) {
    this.intervals.add(new Interval(this.getIdentifier(), start, end));
  }

  private void addInterval(Interval interval) {
    this.intervals.add(interval);
  }

  private void setIntervals(List<Interval> intervals) {
    if (intervals != null) {
      this.intervals = intervals;
    }
  }

  public static Builder builder(OccupationRepository repository) {
    return new Builder(repository);
  }

  public double getTimeSpent() {
    return intervals.stream().map(Interval::getTimeSpanInHours).mapToDouble(Double::doubleValue)
        .sum();
  }

  private void persist() {
    this.repository.save(this);
  }

  public static class Builder {

    private Occupation occupation;

    private List<Interval> intervals = new ArrayList<>();
    private String occupationName;
    private UUID rootIdentifier;

    Builder(OccupationRepository repository) {
      occupation = new Occupation(repository);
    }

    public Builder rootIdentifier(UUID rootIdentifier) {
      this.rootIdentifier = rootIdentifier;
      return this;
    }

    public Builder name(String occupationName) {
      this.occupationName = occupationName;
      return this;
    }

    public Builder interval(PointInTime start, PointInTime firstEnd) {
      Interval interval = new Interval(occupation.getIdentifier(), start, firstEnd);
      this.intervals.add(interval);
      return this;
    }

    public Occupation build() {
      occupation.setName(occupationName);
      occupation.setIdentifier(this.rootIdentifier);
      occupation.setIntervals(this.intervals);

      occupation.persist();
      return occupation;
    }
  }
}
