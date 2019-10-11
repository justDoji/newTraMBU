package be.doji.productivity.trambu.timetracking.infra;

import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.timetracking.infra.dao.OccupationDAO;
import be.doji.productivity.trambu.timetracking.infra.dto.OccupationData;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OccupationRepositoryImpl implements OccupationRepository {

  private OccupationDAO occupationDAO;

  @Autowired
  public OccupationRepositoryImpl(OccupationDAO occupationDAO) {
    this.occupationDAO = occupationDAO;
  }

  @Override
  public Optional<Occupation> occupationById(UUID rootIdentifier) {
    Optional<OccupationData> nameData = occupationDAO.findByCorrelationId(rootIdentifier);
    if (nameData.isPresent()) {
      Occupation build = Occupation.builder(this)
          .name(nameData.get().getName())
          .build();
      return Optional.of(build);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void save(Occupation occupation) {
    storeOccupationData(occupation);
  }

  private void storeOccupationData(Occupation occupation) {
    Optional<OccupationData> nameData = occupationDAO
        .findByCorrelationId(occupation.getIdentifier());
    if (nameData.isPresent()) {
      updateOccupationData(occupation, nameData.get());
    } else {
      createNewOccupationData(occupation);
    }
  }

  private void updateOccupationData(Occupation occupation, OccupationData data) {
    data.setName(occupation.getName());
    occupationDAO.save(data);
  }

  private void createNewOccupationData(Occupation occupation) {
    OccupationData occupationData = new OccupationData();
    occupationData.setName(occupation.getName());
    occupationData.setCorrelationId(occupation.getIdentifier());
    occupationDAO.save(occupationData);
  }
}
