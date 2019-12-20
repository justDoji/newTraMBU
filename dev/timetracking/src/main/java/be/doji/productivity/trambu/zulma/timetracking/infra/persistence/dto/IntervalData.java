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
package be.doji.productivity.trambu.zulma.timetracking.infra.persistence.dto;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.Data;

@Data
@Entity(name = "INTERVAL")
public class IntervalData {

  public IntervalData() {
    // Default constructor for hibernate
  }

  public IntervalData(String start, String end, UUID correlationId) {
    this.start = start;
    this.end = end;
  }

  @Id
  @Column(name = "INTERVAL_ID", nullable = false)
  private UUID intervalId;

  @Column(name = "START", nullable = false)
  private String start;

  @Column(name = "END", nullable = false)
  private String end;

  @Column(name = "CORRELATION_ID", nullable = false)
  private UUID correlationId;
}
