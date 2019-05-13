package be.doji.productivity.trambu.front.view;

import be.doji.productivity.trambu.front.elements.ActivityModel;
import be.doji.productivity.trambu.infrastructure.repository.ActivityDatabaseRepository;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityProjectData;
import be.doji.productivity.trambu.infrastructure.transfer.ActivityTagData;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

@SessionScope
@Named
public class ActivityOverview {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired ActivityDatabaseRepository repository;

  private List<ActivityModel> model;

  @PostConstruct
  public void init() {
    System.out.println("Init restarted");
    if (this.model == null) {
      repository.save(createMockData());

      this.model = repository.findAll().stream()
          .map(ActivityData::toDomainObject)
          .map(ActivityModel::new)
          .collect(Collectors.toList());
    }

  }

  public ActivityData createMockData() {
    ActivityData activityData = new ActivityData();
    activityData.setTitle("Build a User Interface in JSF");
    activityData.setCompleted(true);

    ArrayList<ActivityTagData> tags = new ArrayList<>();
    tags.add(new ActivityTagData("tag 1"));
    tags.add(new ActivityTagData("tag 2"));
    tags.add(new ActivityTagData("tag 3"));
    activityData.setTags(tags);

    ArrayList<ActivityProjectData> projects = new ArrayList<>();
    projects.add(new ActivityProjectData("project 1"));
    projects.add(new ActivityProjectData("project 2"));
    projects.add(new ActivityProjectData("project 3"));
    activityData.setProjects(projects);

    return activityData;
  }

  public List<ActivityModel> getActivities() {
    return this.model;
  }
}
