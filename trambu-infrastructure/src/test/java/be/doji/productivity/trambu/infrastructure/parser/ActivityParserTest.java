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
package be.doji.productivity.trambu.infrastructure.parser;

import static org.assertj.core.api.Assertions.*;

import be.doji.productivity.trambu.infrastructure.transfer.ActivityTO;
import java.text.ParseException;
import org.junit.Test;

public class ActivityParserTest {

  public static final String ACTIVITY_DATA_LINE = "(A) 2017-10-21:14:13.000 TaskTitle  +OverarchingProject @Tag @Tag2 due:2017-12-21:16:15:00.000 index:0 blocksNext:yes skill:SkillName uuid:283b6271-b513-4e89-b757-10e98c9078ea";

  @Test
  public void mapStringToActivity() {
    ActivityTO parsedActivity = ActivityParser.parse(ACTIVITY_DATA_LINE);

    assertThat(parsedActivity).isNotNull();
  }

  @Test
  public void mapStringToActivity_emptyString() {
    assertThatThrownBy(() -> ActivityParser.parse(""))
                                      .isInstanceOf(IllegalArgumentException.class)
                                      .hasMessage("Failure during parsing: empty String or null value not allowed");
  }

  @Test
  public void mapStringToActivity_null() {
    assertThatThrownBy(() -> ActivityParser.parse(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Failure during parsing: empty String or null value not allowed");
  }
}