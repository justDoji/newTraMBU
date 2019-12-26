package be.doji.productivity.trambu.planning.infra.access;

import be.doji.productivity.trambu.planning.domain.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrganizationController {

  private final ActionRepository actionRepository;

  public OrganizationController(@Autowired ActionRepository actionRepository) {
    this.actionRepository = actionRepository;
  }

  @PostMapping(value = "/projectCreated", consumes = "application/json", produces = "application/json")
  public void projectCreated() {

  }
}
