package be.doji.productivity.trambu.timetracking.domain;

import java.util.UUID;

/**
 * Value object representing what is being done in a certain {@link PointInTime}
 */
public class Occupation {

  private UUID correlationId;
  private String name;

}
