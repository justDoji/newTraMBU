package be.doji.productivity.trambu.timetracking.infra.dto;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.Data;

@Data
@Entity(name = "OCCUPATION")
public class OccupationData {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "occupation_seq")
  @SequenceGenerator(name = "occupation_seq", sequenceName = "SEQ_OCCUPATION")
  private Long id;

  @Column(name = "CORRELATION_ID", nullable = false)
  private UUID correlationId;

  @Column(name = "NAME", nullable = false)
  private String name;

}
