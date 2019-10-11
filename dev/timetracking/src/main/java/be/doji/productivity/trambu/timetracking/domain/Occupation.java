package be.doji.productivity.trambu.timetracking.domain;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Value object representing what is being done in a certain {@link PointInTime}
 */
@Data
@AllArgsConstructor
public class Occupation {

  private UUID correlationId;
  private String name;


}
