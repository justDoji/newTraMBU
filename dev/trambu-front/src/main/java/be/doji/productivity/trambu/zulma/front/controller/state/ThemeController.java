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
package be.doji.productivity.trambu.zulma.front.controller.state;

import be.doji.productivity.trambu.zulma.front.model.Theme;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import org.springframework.web.context.annotation.ApplicationScope;

@ApplicationScope
@Named
public class ThemeController {

  private List<Theme> themes;
  private String theme;

  @PostConstruct
  public void init() {
    Theme defaultTheme = new Theme(0, "Default", "trambu_dark");
    Theme yellorangeTheme = new Theme(1, "Yellorange", "trambu_yellorange");

    themes = new ArrayList<>();
    themes.add(defaultTheme);
    themes.add(yellorangeTheme);

    this.setTheme(defaultTheme.getName());
  }

  public List<Theme> getThemes() {
    return themes;
  }

  public void setThemes(List<Theme> themes) {
    this.themes = themes;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }

  public void onSwitch(ValueChangeEvent event) {
    this.theme = (String) event.getNewValue();
  }
}
