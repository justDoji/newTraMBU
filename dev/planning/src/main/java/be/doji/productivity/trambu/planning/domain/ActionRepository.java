package be.doji.productivity.trambu.planning.domain;

import java.util.List;

public interface ActionRepository {

  public List<Action> findByProject(Project project);
  public List<Action> findByTag(Tag tag);

}
