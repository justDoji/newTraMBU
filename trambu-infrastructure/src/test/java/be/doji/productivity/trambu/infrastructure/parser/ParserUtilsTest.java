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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.re2j.PatternSyntaxException;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

public class ParserUtilsTest {


  public static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
  public static final String REGEX_ATTACK_LINE = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaac";
  public static final String REGEX_ATTACK_REGEX = "(a+)++";

  @Test
  public void findAllMatches_withMatches() {
    List<String> lorem = ParserUtils.findAllMatches("(l|L)orem", LOREM_IPSUM);
    assertThat(lorem).isNotEmpty();
    assertThat(lorem).hasSize(1);
  }

  @Test
  public void matches_withMatches() {
    assertThat(ParserUtils.matches("(l|L)orem", LOREM_IPSUM)).isTrue();
  }

  @Test
  public void findFirstMatch_withMatches() {
    Optional<String> lorem = ParserUtils.findFirstMatch("(l|L)orem", LOREM_IPSUM);
    assertThat(lorem).isPresent();
    assertThat(lorem.get()).isEqualTo("Lorem");
  }

  @Test
  public void findAllMatches_noMatches() {
    List<String> lorem = ParserUtils.findAllMatches("Jos is machtig", LOREM_IPSUM);
    assertThat(lorem).isEmpty();
  }

  @Test
  public void matches_noMatches() {
    assertThat(ParserUtils.matches("Jos is machtig", LOREM_IPSUM)).isFalse();
  }

  @Test
  public void findFirstMatch_noMatches() {
    Optional<String> lorem = ParserUtils.findFirstMatch("Jos is machtig", LOREM_IPSUM);
    assertThat(lorem).isNotPresent();
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

  @Test(expected = PatternSyntaxException.class)
  public void findAllMatches_regexAttack() {
    ParserUtils.findAllMatches(REGEX_ATTACK_REGEX, REGEX_ATTACK_LINE);
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

  @Test
  public void replaceFirst_replacesFirstOccurence() {
    assertThat(ParserUtils.replaceFirst("aa", "aaaaaaaaa", "bb")).isEqualTo("bbaaaaaaa");
  }

  @Test
  public void replaceFirst_doesntReplace_noMatch() {
    assertThat(ParserUtils.replaceFirst("aa", "cccccccccccc", "bb")).isEqualTo("cccccccccccc");
  }

  @Test
  public void replaceLast_replacesFirstOccurence() {
    assertThat(ParserUtils.replaceLast("aa", "aaaaaaaaa", "bb")).isEqualTo("aaaaaaabb");
  }

  @Test
  public void replaceLast_doesntReplace_noMatch() {
    assertThat(ParserUtils.replaceLast("aa", "cccccccccccc", "bb")).isEqualTo("cccccccccccc");
  }
}