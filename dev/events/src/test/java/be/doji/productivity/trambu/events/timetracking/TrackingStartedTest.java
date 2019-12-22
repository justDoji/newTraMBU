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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.time.LocalDateTime;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TrackingStartedTest {

  @Test
  public void construction_setsTimeStamp() {
    TrackingStarted event = new TrackingStarted(UUID.randomUUID(), LocalDateTime.of(2019,12,20,12,0));
    Assertions.assertThat(event.getTimestamp()).isNotNull();
  }

  @Test
  public void event_canBeSerialized() throws JsonProcessingException {
    UUID uuid = UUID.randomUUID();
    TrackingStarted event = new TrackingStarted(uuid, LocalDateTime.of(2019,12,20,12,0));

    String jsonString = getObjMapper().writeValueAsString(event);

    Assertions.assertThat(jsonString).contains("\"reference\":\""+ uuid.toString() +"\"");
  }

  private static ObjectMapper getObjMapper() {
    return new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
  }
}