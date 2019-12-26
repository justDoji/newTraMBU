package be.doji.productivity.trambu.planning.domain;

import java.util.UUID;

public interface ProjectRepository {

  Project getProjectById(UUID projectId);

  Project getProjectByAction(UUID actionId);

}
