/**
 * TraMBU - an open time management tool
 *
 *     Copyright (C) 2019  Stijn Dejongh
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     For further information on usage, or licensing, contact the author
 *     through his github profile: https://github.com/justDoji
 */
package be.doji.productivity.trambu.zulma.infrastructure.transfer;

import java.util.Optional;
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

  public LogPointData() {
    // Default constructor for hibernate
  }

  public LogPointData(String start, String end) {
    this.start = start;
    this.end = end;
  }

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

  public Optional<ActivityData> getActivity() {
    return Optional.ofNullable(this.activity);
  }
}
