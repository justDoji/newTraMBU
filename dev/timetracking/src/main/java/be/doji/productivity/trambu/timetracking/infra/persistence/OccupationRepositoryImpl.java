/**
 * TraMBU - an open time management tool
 * <p>
 * Copyright (C) 2019  Stijn Dejongh
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * <p>
 * For further information on usage, or licensing, contact the author through his github profile:
 * https://github.com/justDoji
 */
package be.doji.productivity.trambu.timetracking.infra.persistence;

import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.timetracking.infra.persistence.dao.IntervalDAO;
import be.doji.productivity.trambu.timetracking.infra.persistence.dao.OccupationDAO;
import be.doji.productivity.trambu.timetracking.infra.persistence.dto.IntervalData;
import be.doji.productivity.trambu.timetracking.infra.persistence.dto.IntervalMapper;
import be.doji.productivity.trambu.timetracking.infra.persistence.dto.OccupationData;
import be.doji.productivity.trambu.timetracking.infra.persistence.dto.OccupationMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OccupationRepositoryImpl implements OccupationRepository {

  private final IntervalMapper intervalMapper;
  private final OccupationDAO occupationDAO;
  private final IntervalDAO intervalDAO;
  private final OccupationMapper occupationMapper;


  @Autowired
  public OccupationRepositoryImpl(
      OccupationDAO occupationDAO,
      IntervalDAO intervalDAO,
      OccupationMapper occupationMapper,
      IntervalMapper intervalMapper
  ) {
    this.occupationDAO = occupationDAO;
    this.intervalDAO = intervalDAO;
    this.occupationMapper = occupationMapper;
    this.intervalMapper = intervalMapper;
  }

  @Override
  public Optional<Occupation> occupationById(UUID rootIdentifier) {
    return occupationDAO
        .findByCorrelationId(rootIdentifier)
        .map(this::occupationPersitenceToDomain);
  }

  private Occupation occupationPersitenceToDomain(OccupationData nameData) {
    Occupation aggregate = occupationMapper.occupationDataToOccupation(nameData, this);
    List<IntervalData> intervalData = intervalDAO.findByCorrelationId(nameData.getCorrelationId());
    intervalData.stream()
        .map(intervalMapper::intervalDataToInterval)
        .forEach(aggregate::addInterval);
    return aggregate;
  }

  @Override
  public void save(Occupation occupation) {
    storeOccupationData(occupation);
    storeIntervalData(occupation);
  }

  @Override
  public void clear() {
    intervalDAO.deleteAll();
    occupationDAO.deleteAll();
  }

  private void storeOccupationData(Occupation occupation) {
    OccupationData mappedData = occupationMapper.occupationToOccupationData(occupation);
    occupationDAO
        .findByCorrelationId(occupation.getIdentifier())
        .ifPresent(data -> mappedData.setId(data.getId()));

    occupationDAO.save(mappedData);
  }

  private void storeIntervalData(Occupation occupation) {
    mapToIntervalData(occupation).forEach(intervalDAO::save);
  }

  private List<IntervalData> mapToIntervalData(Occupation occupation) {
    return occupation.getIntervals().stream()
        .map(intervalMapper::intervalToIntervalData)
        .collect(Collectors.toList());
  }
}
