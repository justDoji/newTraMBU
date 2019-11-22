/**
 * TraMBU - an open time management tool
 *
 *     Copyright (C) 2019  Stijn Dejongh
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     For further information on usage, or licensing, contact the author
 *     through his github profile: https://github.com/justDoji
 */
package be.doji.productivity.trambu.timetracking.infra.persistence;

import be.doji.productivity.trambu.timetracking.domain.Interval;
import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.timetracking.domain.time.PointInTimeConverter;
import be.doji.productivity.trambu.timetracking.infra.persistence.dao.IntervalDAO;
import be.doji.productivity.trambu.timetracking.infra.persistence.dao.OccupationDAO;
import be.doji.productivity.trambu.timetracking.infra.dto.IntervalData;
import be.doji.productivity.trambu.timetracking.infra.dto.OccupationData;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OccupationRepositoryImpl implements OccupationRepository {

  private OccupationDAO occupationDAO;
  private IntervalDAO intervalDAO;

  //TODO: move this
  private static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

  static {
    initMapperFactory();
  }

  private static void initMapperFactory() {
    ConverterFactory converterFactory = mapperFactory.getConverterFactory();
    converterFactory.registerConverter(new PointInTimeConverter());

    mapperFactory.classMap(Interval.class, IntervalData.class)
        .field("start", "start")
        .field("end", "end")
        .field("occupationId", "correlationId")
        .register();

    mapperFactory.classMap(Occupation.class, OccupationData.class)
        .field("name", "name")
        .field("identifier", "correlationId")
        .byDefault()
        .register();
  }


  @Autowired
  public OccupationRepositoryImpl(OccupationDAO occupationDAO, IntervalDAO intervalDAO) {
    this.occupationDAO = occupationDAO;
    this.intervalDAO = intervalDAO;
  }

  @Override
  public Optional<Occupation> occupationById(UUID rootIdentifier) {
    return occupationDAO
        .findByCorrelationId(rootIdentifier)
        .map(this::occupationPersitenceToDomain);
  }

  private Occupation occupationPersitenceToDomain(OccupationData nameData) {
    Occupation aggregate = mapperFactory.getMapperFacade().map(nameData, Occupation.class);
    List<IntervalData> intervalData = intervalDAO.findByCorrelationId(nameData.getCorrelationId());
    mapperFactory.getMapperFacade()
        .mapAsList(intervalData, Interval.class)
        .forEach(aggregate::addInterval);
    return aggregate;
  }

  @Override
  public void save(Occupation occupation) {
    storeOccupationData(occupation);
    storeIntervalData(occupation);
  }

  private void storeOccupationData(Occupation occupation) {
    OccupationData mappedData = mapperFactory.getMapperFacade()
        .map(occupation, OccupationData.class);
    occupationDAO
        .findByCorrelationId(occupation.getIdentifier())
        .ifPresent(data -> mappedData.setId(data.getId()));
    occupationDAO.save(mappedData);
  }

  private void storeIntervalData(Occupation occupation) {
    mapToIntervalData(occupation).forEach(data -> intervalDAO.save(data));
  }

  private List<IntervalData> mapToIntervalData(Occupation occupation) {
    return mapperFactory.getMapperFacade()
        .mapAsList(occupation.getIntervals(), IntervalData.class);
  }
}
