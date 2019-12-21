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
package be.doji.productivity.trambu.timetracking.infra.access;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import be.doji.productivity.trambu.timetracking.api.events.TrackingStarted;
import be.doji.productivity.trambu.timetracking.api.events.TrackingStopped;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

class TestFlow {

  private MockMvc mvc;

  TestFlow(MockMvc mvc) {
    this.mvc = mvc;
  }

  MockHttpServletResponse whenCallingTimespentForReference(UUID reference) throws Exception {
    return mvc.perform(
        get("/timespent")
            .param("reference", reference.toString())
            .accept(MediaType.APPLICATION_JSON)
    ).andReturn().getResponse();
  }

  MockHttpServletResponse whenCallingCreateOccupation(String title, UUID reference)
      throws Exception {
    return mvc.perform(
        post("/create")
            .param("title", title)
            .param("reference", reference.toString())
            .accept(MediaType.APPLICATION_JSON)
    ).andReturn().getResponse();
  }

  MockHttpServletResponse whenCallingTrackingStartedForReference(UUID reference,
      LocalDateTime timeStarted) throws Exception {
    return mvc.perform(
        post("/startTracking")
            .content(
                new ObjectMapper().writeValueAsString(new TrackingStarted(reference, timeStarted)))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    ).andReturn().getResponse();
  }

  MockHttpServletResponse whenCallingTrackingStoppedForReference(UUID reference,
      LocalDateTime timeStopped) throws Exception {
    return mvc.perform(
        post("/stopTracking")
            .content(
                new ObjectMapper().writeValueAsString(new TrackingStopped(reference, timeStopped)))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    ).andReturn().getResponse();
  }

}
