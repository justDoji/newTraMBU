package be.doji.productivity.trambu.planning.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import be.doji.productivity.trambu.events.planning.ActionCreated;
import be.doji.productivity.trambu.planning.domain.events.EventBroadcaster;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ActionTest {

  @Mock
  private EventBroadcaster broadcaster;


  @Test
  public void defaultCreation_initializesReference() {
    Action action = new Action("Do Something", broadcaster);
    assertThat(action.getReference()).isNotNull();
  }

  @Test
  public void afterCreation_commentsCanBeAdded() {
    Action action = new Action("Do Something", broadcaster);

    String commentText = "I did something";
    action.addComment(new Comment(commentText));

    assertThat(action.getComments()).hasSize(1);
    assertThat(action.getComments()).extracting(Comment::getText).containsExactly(commentText);
  }

  @Test
  public void afterCreation_projectsCanBeAdded() {
    Action action = new Action("Do Something", broadcaster);

    String projectTitle = "TRAMBU development";
    action.assingToProject(new Project(projectTitle));

    assertThat(action.getProject()).extracting(Project::getName).isEqualTo(projectTitle);
  }

  @Test
  public void creation_triggersEventBroadcast() {
    Action action = new Action("Inform other modules", broadcaster);

    ArgumentCaptor<ActionCreated> captor = ArgumentCaptor.forClass(ActionCreated.class);

    verify(broadcaster).broadcast(captor.capture());
    assertThat(captor.getValue()).extracting(ActionCreated::getActionName).isEqualTo("Inform other modules");
    assertThat(captor.getValue()).extracting(ActionCreated::getReference).isEqualTo(action.getReference());
    assertThat(captor.getValue()).extracting(ActionCreated::getTimestamp).isNotNull();
  }

}