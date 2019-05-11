package be.doji.productivity.trambu.infrastructure.transfer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ACTIVITY_PROJECT")
public class ActivityProjectData {

  public ActivityProjectData(String value) {
    this.value = value;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq")
  @SequenceGenerator(name = "project_seq", sequenceName = "SEQ_PROJECT")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "FK_ACTIVITY_ID")
  private ActivityData activity;

  @Column(name = "VALUE")
  private String value;

}
