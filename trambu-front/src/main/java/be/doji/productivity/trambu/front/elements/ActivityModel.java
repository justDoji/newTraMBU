/**
 * MIT License
 *
 * Copyright (c) 2019 Stijn Dejongh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package be.doji.productivity.trambu.front.elements;

import be.doji.productivity.trambu.domain.activity.Activity;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.Data;

@Data
public class ActivityModel {

  private static final String BASIC_DATE_TIME_PATTERN = "dd/MM/uuuu HH:mm:ss";
  private static final DateTimeFormatter df = DateTimeFormatter
      .ofPattern(BASIC_DATE_TIME_PATTERN, Locale.FRANCE);
  private final Long dataBaseId;

  private String frontId;
  private String title;
  private List<String> projects;
  private List<String> tags;
  private String deadline = "No deadline in sight";
  private boolean completed;

  public ActivityModel(Activity toBuildFrom, Long id) {
    this.title = toBuildFrom.getTitle();
    this.projects = toBuildFrom.getProjects();
    this.tags = toBuildFrom.getTags();
    this.completed = toBuildFrom.isCompleted();
    this.frontId = UUID.randomUUID().toString();
    this.dataBaseId = id;

    toBuildFrom.getDeadline()
        .ifPresent(timePoint -> this.deadline = df.format(timePoint.toLocalDateTime()));
  }

  public void toggleCompleted() {
    this.completed = !this.completed;
  }
}
