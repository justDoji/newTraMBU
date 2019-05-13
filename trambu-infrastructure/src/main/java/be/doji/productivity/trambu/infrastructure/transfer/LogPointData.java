package be.doji.productivity.trambu.infrastructure.transfer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import lombok.Data;

@Data
@Entity(name = "LOGPOINT")
public class LogPointData {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logpoint_seq")
  @SequenceGenerator(name = "logpoint_seq", sequenceName = "SEQ_LOGPOINT")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "FK_ACTIVITY_ID")
  private ActivityData activity;

  @Column(name = "START", nullable = false)
  private String start;

  @Column(name = "END", nullable = false)
  private String end;

}
