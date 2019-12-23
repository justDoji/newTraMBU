package be.doji.productivity.trambu.planning.domain;

import be.doji.productivity.trambu.events.planning.ActionCreated;
import be.doji.productivity.trambu.kernel.annotations.AggregateRoot;
import be.doji.productivity.trambu.planning.domain.events.EventBroadcaster;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AggregateRoot
public class Action {

  private final EventBroadcaster broadcaster;
  private UUID reference;
  private String name;
  private boolean completed;
  private Project parentProject;
  private List<Tag> tags = new ArrayList<>();
  private List<Comment> comments = new ArrayList<>();

  public Action(UUID referenceId, String name, EventBroadcaster broadcaster) {
    this.name = name;
    this.reference = referenceId;
    this.broadcaster = broadcaster;
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
    if(comment != null && !this.comments.contains(comment)) {
      this.comments.add(comment);
    }
  }

  public List<Comment> getComments() {
    return new ArrayList<>(comments);
  }
}
