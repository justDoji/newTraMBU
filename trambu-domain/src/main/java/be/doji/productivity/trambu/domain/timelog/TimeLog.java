package be.doji.productivity.trambu.domain.timelog;


import be.doji.productivity.trambu.domain.activity.Activity;
import be.doji.productivity.trambu.domain.time.TimeSlot;
import java.util.ArrayList;
import java.util.List;

public class TimeLog {


  private final Activity activity;
  private List<TimeSlot> logs;

  public TimeLog(Activity activity) {
    this.activity = activity;
    this.logs = new ArrayList<>();
  }

  public Activity getActivity() {
    return activity;
  }

  public void addLogPoint(TimeSlot logpoint) {
    logs.add(logpoint);
  }

  public List<TimeSlot> getSlots() {
    return new ArrayList<>(logs);
  }
}
