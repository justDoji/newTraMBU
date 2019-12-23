/**
 * TraMBU - an open time management tool
 * <p>
 * Copyright (C) 2019  Stijn Dejongh
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * <p>
 * For further information on usage, or licensing, contact the author through his github profile:
 * https://github.com/justDoji
 */
package be.doji.productivity.trambu.planning.domain;

import be.doji.productivity.trambu.events.planning.ActionCreated;
import be.doji.productivity.trambu.kernel.annotations.AggregateRoot;
import be.doji.productivity.trambu.planning.domain.events.EventBroadcaster;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AggregateRoot
public class Action {

  private UUID reference;
  private String name;
  private boolean completed;
  private Project parentProject;
  private List<Tag> tags = new ArrayList<>();
  private List<Comment> comments = new ArrayList<>();

  public Action(UUID referenceId, String name, EventBroadcaster broadcaster) {
    this.name = name;
    this.reference = referenceId;
    broadcaster.broadcast(new ActionCreated(this.reference, this.name));
  }

  public Action(String name, EventBroadcaster broadcaster) {
    this(UUID.randomUUID(), name, broadcaster);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public Project getProject() {
    return parentProject;
  }

  public void assingToProject(Project parentProject) {
    this.parentProject = parentProject;
  }

  public UUID getReference() {
    return reference;
  }

  public void setReference(UUID reference) {
    this.reference = reference;
  }

  public void addTag(Tag tag) {
    if (tag != null && !this.tags.contains(tag)) {
      this.tags.add(tag);
    }
  }

  public List<Tag> getTags() {
    return new ArrayList<>(tags);
  }

  public void addComment(Comment comment) {
    if (comment != null && !this.comments.contains(comment)) {
      this.comments.add(comment);
    }
  }

  public List<Comment> getComments() {
    return new ArrayList<>(comments);
  }
}
