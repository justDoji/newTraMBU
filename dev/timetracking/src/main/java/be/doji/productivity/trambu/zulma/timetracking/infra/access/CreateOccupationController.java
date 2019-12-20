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
package be.doji.productivity.trambu.zulma.timetracking.infra.access;

import be.doji.productivity.trambu.zulma.timetracking.domain.Occupation;
import be.doji.productivity.trambu.zulma.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.zulma.timetracking.domain.time.TimeService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/create")
public class CreateOccupationController {

  private final OccupationRepository repository;
  private final TimeService timeService;

  public CreateOccupationController(@Autowired OccupationRepository repository,
      @Autowired TimeService timeService) {
    this.repository = repository;
    this.timeService = timeService;
  }

  @PostMapping()
  public void createOccupation(
      @RequestParam("title") String title,
      @RequestParam("reference") String reference
  ) {
    Occupation.builder(repository, timeService)
        .rootIdentifier(UUID.fromString(reference))
        .name(title)
        .build();
  }
  
}
