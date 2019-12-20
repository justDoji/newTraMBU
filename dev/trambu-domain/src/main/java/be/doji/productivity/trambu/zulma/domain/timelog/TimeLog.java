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
package be.doji.productivity.trambu.zulma.domain.timelog;


import be.doji.productivity.trambu.zulma.domain.activity.Activity;
import be.doji.productivity.trambu.zulma.domain.time.TimeSlot;
import java.util.ArrayList;
import java.util.List;

public class TimeLog {


  private final Activity activity;
  private List<TimeSlot> logs;

  public TimeLog(Activity activity) {
    this.activity = activity;
    this.logs = new ArrayList<>();
  }

  public Activity getActivity() {
    return activity;
  }

  public void addLogPoint(TimeSlot logpoint) {
    logs.add(logpoint);
  }

  public List<TimeSlot> getSlots() {
    return new ArrayList<>(logs);
  }
}
