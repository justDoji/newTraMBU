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
package be.doji.productivity.trambu.events.timetracking; /**
 * TraMBU - an open time management tool
 * <p>
 * Copyright (C) 2019  Stijn Dejongh
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * <p>
 * For further information on usage, or licensing, contact the author through his github profile:
 * https://github.com/justDoji
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class TrackingStopped implements Event {

  private UUID reference;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime timeStopped;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime timestamp = LocalDateTime.now();

  public TrackingStopped(UUID reference, LocalDateTime timeStopped) {
    this.reference = reference;
    this.timeStopped = timeStopped;
  }

  //Needed for JSOn serialization
  public TrackingStopped() {
  }

  @Override
  public LocalDateTime timestamp() {
    return timestamp;
  }
}
