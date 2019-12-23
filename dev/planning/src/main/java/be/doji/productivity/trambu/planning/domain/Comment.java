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
package be.doji.productivity.trambu.planning.domain;

import be.doji.productivity.trambu.kernel.time.PointInTime;
import java.time.LocalDateTime;

public class Comment {

  private PointInTime timeStamp = PointInTime.fromDateTime(LocalDateTime.now());
  private String text;

  public Comment(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public PointInTime getTimeStamp() {
    return timeStamp;
  }
}
