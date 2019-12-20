/**
 * TraMBU - an open time management tool
 *
 * Copyright (C) 2019  Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 * For further information on usage, or licensing, contact the author through his github profile:
 * https://github.com/justDoji
 */
package be.doji.productivity.trambu.zulma.timetracking.infra.access;

import be.doji.productivity.trambu.zulma.timetracking.api.dto.TimeTracked;
import be.doji.productivity.trambu.zulma.timetracking.api.events.TrackingStarted;
import be.doji.productivity.trambu.zulma.timetracking.api.events.TrackingStopped;
import be.doji.productivity.trambu.zulma.timetracking.domain.Occupation;
import be.doji.productivity.trambu.zulma.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.zulma.timetracking.infra.exception.OccupationUnknown;
import be.doji.productivity.trambu.zulma.timetracking.infra.persistence.dto.OccupationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackingController {

  private final OccupationRepository repository;
  private final OccupationMapper mapper;

  public TrackingController(@Autowired OccupationRepository repository, @Autowired OccupationMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }


  @GetMapping("/timespent")
  public TimeTracked getTracking(@RequestParam("reference") String occupationReference) {
    Occupation occupation = repository
        .occupationById(UUID.fromString(occupationReference))
        .orElseThrow(() -> noKnownOccupation(occupationReference));
    return mapper.occupationToTimeTracked(occupation);
  }

  @PostMapping(value = "/startTracking" , consumes = "application/json", produces = "application/json")
  public void trackingStarted(@RequestBody TrackingStarted startedEvent) {
    UUID reference = startedEvent.getReference();
    Occupation occupation = repository.occupationById(reference)
        .orElseThrow(() -> noKnownOccupation(reference == null ? null : reference.toString()));
    occupation.startedAt(startedEvent.getTimeStarted());
  }

  @PostMapping(value = "/stopTracking" , consumes = "application/json", produces = "application/json")
  public void trackingStopped(@RequestBody TrackingStopped stoppedEvent) {
    UUID reference = stoppedEvent.getReference();
    Occupation occupation = repository.occupationById(reference)
        .orElseThrow(() -> noKnownOccupation(reference == null ? null : reference.toString()));
    occupation.stoppedAt(stoppedEvent.getTimeStopped());
  }

  public OccupationUnknown noKnownOccupation(
      @PathVariable String occupationReference) {
    return new OccupationUnknown(
        String.format("No Occupation tied to reference: {%s}", occupationReference));
  }

  @Bean
  @Primary
  public ObjectMapper geObjMapper() {
    return new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
  }


}
