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