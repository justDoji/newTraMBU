/**
 * TraMBU - an open time management tool
 *
 * Copyright (C) 2019  Stijn Dejongh
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 * For further information on usage, or licensing, contact the author through his github profile:
 * https://github.com/justDoji
 */
package be.doji.productivity.trambu.timetracking.infra.persistence;

import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.timetracking.domain.time.TimeService;
import be.doji.productivity.trambu.timetracking.infra.dto.IntervalData;
import be.doji.productivity.trambu.timetracking.infra.dto.IntervalMapper;
import be.doji.productivity.trambu.timetracking.infra.dto.OccupationData;
import be.doji.productivity.trambu.timetracking.infra.dto.OccupationMapper;
import be.doji.productivity.trambu.timetracking.infra.persistence.dao.IntervalDAO;
import be.doji.productivity.trambu.timetracking.infra.persistence.dao.OccupationDAO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OccupationRepositoryImpl implements OccupationRepository {

  private TimeService timeservice;
  private OccupationDAO occupationDAO;
  private IntervalDAO intervalDAO;


  @Autowired
  public OccupationRepositoryImpl(OccupationDAO occupationDAO, IntervalDAO intervalDAO,
      TimeService timeService) {
    this.occupationDAO = occupationDAO;
    this.intervalDAO = intervalDAO;
    this.timeservice = timeService;
  }

  @Override
  public Optional<Occupation> occupationById(UUID rootIdentifier) {
    return occupationDAO
        .findByCorrelationId(rootIdentifier)
        .map(this::occupationPersitenceToDomain);
  }

  private Occupation occupationPersitenceToDomain(OccupationData nameData) {
    Occupation aggregate = OccupationMapper.instance().occupationDataToOccupation(nameData);
    List<IntervalData> intervalData = intervalDAO.findByCorrelationId(nameData.getCorrelationId());
    intervalData.stream()
        .map(i -> IntervalMapper.instance().intervalDataToInterval(i))
        .forEach(aggregate::addInterval);
    return aggregate;
  }

  @Override
  public void save(Occupation occupation) {
    storeOccupationData(occupation);
    storeIntervalData(occupation);
  }

  private void storeOccupationData(Occupation occupation) {
    OccupationData mappedData = OccupationMapper.instance().occupationToOccupationData(occupation);
    occupationDAO
        .findByCorrelationId(occupation.getIdentifier())
        .ifPresent(data -> mappedData.setId(data.getId()));
    occupationDAO.save(mappedData);
  }

  private void storeIntervalData(Occupation occupation) {
    mapToIntervalData(occupation).forEach(data -> intervalDAO.save(data));
  }

  private List<IntervalData> mapToIntervalData(Occupation occupation) {
    return occupation.getIntervals().stream()
        .map(i -> IntervalMapper.instance().intervalToIntervalData(i))
        .collect(Collectors.toList());
  }
}
