package be.doji.productivity.trambu.infrastructure.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.Test;

public class ParserUtilsTest {


  public static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";

  @Test
  public void findAllMatches_withMatches() {
    List<String> lorem = ParserUtils.findAllMatches("(l|L)orem", LOREM_IPSUM);
    assertThat(lorem).isNotEmpty();
    assertThat(lorem).hasSize(1);
  }

  @Test
  public void findAllMatches_noMatches() {
    List<String> lorem = ParserUtils.findAllMatches("Jos is machtig", LOREM_IPSUM);
    assertThat(lorem).isEmpty();
  }

  @Test
  public void findAllMatches_emptyRegex() {
    assertThatThrownBy(() -> ParserUtils.findAllMatches("", LOREM_IPSUM))
        .hasMessage("I will not search for an empty String");
  }

  @Test
  public void findAllMatches_nullRegex() {
    assertThatThrownBy(() -> ParserUtils.findAllMatches(null, LOREM_IPSUM))
        .hasMessage("I will not search for an empty String");
  }

  @Test
  public void findAllMatches_emptyData() {
    List<String> lorem = ParserUtils.findAllMatches("Jos is machtig", "");
    assertThat(lorem).isEmpty();
  }

  @Test
  public void findAllMatches_nullData() {
    List<String> lorem = ParserUtils.findAllMatches("Jos is machtig", null);
    assertThat(lorem).isEmpty();
  }

  @Test
  public void escape_default() {
    assertThat(ParserUtils.escape("test")).isEqualTo("\"test\"");
  }

  @Test
  public void escape_empty() {
    assertThat(ParserUtils.escape("")).isEqualTo("\"\"");
  }

  @Test
  public void escape_null() {
    assertThatThrownBy(() -> ParserUtils.escape(null)).hasMessage("I will not escape a null value");
  }

}