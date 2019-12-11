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
        post("/create/")
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
