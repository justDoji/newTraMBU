package be.doji.productivity.trambu.timetracking.api;

import org.springframework.http.ResponseEntity;

public interface OccupationModule {

  ResponseEntity<TimeTracked> getTrackedInformation();



}
