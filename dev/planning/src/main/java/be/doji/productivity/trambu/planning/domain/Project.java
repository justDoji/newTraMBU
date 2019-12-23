package be.doji.productivity.trambu.planning.domain;

import lombok.Data;

@Data
public class Project {

  private String name;

  public Project(String name) {
    this.name = name;
  }
}
