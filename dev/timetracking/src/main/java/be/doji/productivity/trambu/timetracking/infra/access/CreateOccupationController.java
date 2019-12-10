package be.doji.productivity.trambu.timetracking.infra.access;

import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.timetracking.domain.time.TimeService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/create")
public class CreateOccupationController {

  private final OccupationRepository repository;
  private final TimeService timeService;

  public CreateOccupationController(@Autowired OccupationRepository repository,
      @Autowired TimeService timeService) {
    this.repository = repository;
    this.timeService = timeService;
  }

  @PostMapping()
  public void createOccupation(
      @RequestParam("title") String title,
      @RequestParam("reference") String reference
  ) {
    Occupation.builder(repository, timeService)
        .rootIdentifier(UUID.fromString(reference))
        .name(title)
        .build();
  }
  
}
