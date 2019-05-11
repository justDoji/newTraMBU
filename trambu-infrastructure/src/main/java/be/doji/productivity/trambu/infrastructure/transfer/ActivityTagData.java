package be.doji.productivity.trambu.infrastructure.transfer;

import be.doji.productivity.trambu.domain.activity.Activity;
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
@Table(name = "ACTIVITY_TAG")
class ActivityTagData {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_seq")
  @SequenceGenerator(name = "tag_seq", sequenceName = "SEQ_TAG")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "FK_ACTIVITY_ID")
  private Activity activity;

  @Column(name = "VALUE")
  private String value;

}
