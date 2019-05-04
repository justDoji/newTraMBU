package be.doji.productivity.trambu.domain.priority;


import be.doji.productivity.trambu.domain.activity.Activity;

public interface PriorityCalculator {

  Priority calculatePriority(Activity activity);

}
