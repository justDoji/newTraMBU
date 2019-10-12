package be.doji.productivity.trambu.timetracking.domain;

import java.util.Optional;
import java.util.UUID;

public interface OccupationRepository {

  Optional<Occupation> occupationById(UUID rootIdentifier);

  void save(Occupation occupation);
}
