package be.doji.productivity.trambu.front.controller;

import javax.inject.Named;
import org.springframework.web.context.annotation.SessionScope;

@SessionScope
@Named
public class ThemeController {

  private String theme = "/css/trambu.css";

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }
}
