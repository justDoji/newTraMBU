package be.doji.productivity.trambu.timetracking.infra.access;

import static be.doji.productivity.trambu.timetracking.infra.access.ControllerMapper.*;

import be.doji.productivity.trambu.timetracking.api.TimeTracked;
import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.timetracking.infra.exception.OccupationUnknown;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timespent")
public class OccupationController {

  private final OccupationRepository repository;

  public OccupationController(@Autowired OccupationRepository repository) {
    this.repository = repository;
  }


  @GetMapping("/{occupationReference}")
  public TimeTracked getTracking(@PathVariable String occupationReference) {
    Occupation occupation = repository
        .occupationById(UUID.fromString(occupationReference))
        .orElseThrow(() -> noKnownOccupation(occupationReference));
    return timeTracked(occupation);
  }

  public OccupationUnknown noKnownOccupation(
      @PathVariable String occupationReference) {
    return new OccupationUnknown(
        String.format("No Occupation tied to reference: {%s}", occupationReference));
  }

  @Bean
  @Primary
  public ObjectMapper geObjMapper(){
    return new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
  }


}
