package be.doji.productivity.trambu.infrastructure.repository;

import be.doji.productivity.trambu.domain.activity.Activity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ActivityRepository {

  private List<Activity> activities = new ArrayList<>();

  public List<Activity> getAll() {
    return new ArrayList<>(this.activities);
  }

  public void save(Activity activityToSave) {
    this.activities.add(activityToSave);
  }

  public void clear() {
    this.activities.clear();
  }
}
