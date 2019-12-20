/**
 * TraMBU - an open time management tool
 *
 * Copyright (C) 2019  Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 * For further information on usage, or licensing, contact the author through his github profile:
 * https://github.com/justDoji
 */
package be.doji.productivity.trambu.zulma.timetracking.domain;

import be.doji.productivity.trambu.zulma.kernel.annotations.AggregateRoot;
import be.doji.productivity.trambu.zulma.timetracking.domain.time.PointInTime;
import be.doji.productivity.trambu.zulma.timetracking.domain.time.TimeService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Value object representing what is being done in a certain {@link PointInTime}
 */
@AggregateRoot
public class Occupation {

  private final TimeService timeService;
  private final OccupationRepository repository;
  private UUID identifier;
  private String name;
  private List<Interval> intervals = new ArrayList<>();


  public Occupation(TimeService timeService, OccupationRepository repository) {
    this.timeService = timeService;
    this.repository = repository;
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
    addInterval(new Interval(this.getIdentifier(), start, end, timeService));
  }

  public void addInterval(Interval toAdd) {
    this.intervals.add(toAdd);
    save();
  }

  private void setIntervals(List<Interval> intervals) {
    if (intervals != null) {
      this.intervals = intervals;
    }
  }

  private void save() {
    repository.save(this);
  }

  public static Builder builder(OccupationRepository repository, TimeService timeService) {
    return new Builder(repository, timeService);
  }

  public double getTimeSpentInHours() {
    return intervals.stream().map(Interval::getTimeSpanInHours).mapToDouble(Double::doubleValue)
        .sum();
  }

  public final List<Interval> getIntervals() {
    return new ArrayList<>(this.intervals);
  }

  public void start() {
    if (this.intervals.stream().noneMatch(Interval::inProgress)) {
      this.intervals.add(new Interval(this.identifier, timeService));
    }
    save();
  }

  public void startedAt(LocalDateTime timeStarted) {
    if (this.intervals.stream().noneMatch(Interval::inProgress)) {
      Interval interval = new Interval(this.identifier, timeService);
      interval.setStart(PointInTime.fromDateTime(timeStarted));
      this.intervals.add(interval);
    }
    save();
  }

  public void stop() {
    this.intervals.stream().filter(Interval::inProgress).findFirst().ifPresent(Interval::endNow);
  }

  public void stoppedAt(LocalDateTime timeStopped) {
    this.intervals.stream().filter(Interval::inProgress).findFirst()
        .ifPresent(
            interval -> interval.setEnd(PointInTime.fromDateTime(timeStopped))
        );
    save();
  }


  public static class Builder {

    private final OccupationRepository repository;
    private final TimeService timeService;

    private List<Interval> intervals = new ArrayList<>();
    private String occupationName;
    private UUID rootIdentifier;

    Builder(OccupationRepository repository, TimeService timeService) {
      this.repository = repository;
      this.timeService = timeService;
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
      Interval interval = new Interval(start, end, timeService);
      this.intervals.add(interval);
      return this;
    }

    public Occupation build() {
      Occupation occupation = new Occupation(timeService, repository);
      occupation.setIdentifier(this.rootIdentifier);
      occupation.setName(occupationName);
      intervals.forEach(interval -> interval.setOccupationId(this.rootIdentifier));
      occupation.setIntervals(this.intervals);

      repository.save(occupation);
      return occupation;
    }
  }
}
