package be.doji.productivity.trambu.timetracking.infra.dao;

import be.doji.productivity.trambu.timetracking.infra.dto.IntervalData;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervalDAO extends CrudRepository<IntervalData, Long> {

  List<IntervalData> findByCorrelationId(UUID correlationID);

}
