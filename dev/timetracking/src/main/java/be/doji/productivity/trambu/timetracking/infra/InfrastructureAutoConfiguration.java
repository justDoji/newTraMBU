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
package be.doji.productivity.trambu.timetracking.infra;

import be.doji.productivity.trambu.timetracking.infra.persistence.PersistenceConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The purpose of this class is to allow spring to componentscan automatically. It is a hassle to
 * get rid of this, and changing it will make the code a lot more verbose.
 */

@SpringBootConfiguration()
@Import({PersistenceConfiguration.class})
@ComponentScan(basePackages = "be.doji.productivity.trambu.timetracking")
@EnableTransactionManagement
public class InfrastructureAutoConfiguration {

  private static final Log log = LogFactory.getLog(InfrastructureAutoConfiguration.class);

  InfrastructureAutoConfiguration() {
    log.info("Created");
  }

}
