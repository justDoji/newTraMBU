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
package be.doji.productivity.trambu.events.timetracking;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class TrackingStarted implements Event {

  private UUID reference;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime timeStarted;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime timestamp = LocalDateTime.now();

  public TrackingStarted(UUID reference, LocalDateTime timeStarted) {
    this.reference = reference;
    this.timeStarted = timeStarted;
  }

  //Needed for JSOn serialization
  public TrackingStarted() {
  }

  @Override
  public LocalDateTime timestamp() {
    return timestamp;
  }
}
