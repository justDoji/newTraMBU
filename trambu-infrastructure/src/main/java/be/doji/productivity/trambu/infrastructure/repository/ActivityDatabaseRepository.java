package be.doji.productivity.trambu.infrastructure.repository;

import be.doji.productivity.trambu.infrastructure.transfer.ActivityData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityDatabaseRepository extends CrudRepository<ActivityData, Long> {

}
