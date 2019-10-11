package be.doji.productivity.trambu.timetracking.domain;

import static org.junit.Assert.*;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class IntervalTest {

  @Test
  public void whenCreatingAnInterval_compositeIsFilledIn() {
    String expectedName = "Coding TRAMBU";

    Interval timeSpentCoding =Interval.builder()
        .occupationName(expectedName)
        .build();

    Assertions.assertThat(timeSpentCoding.getOccupationName()).isEqualTo(expectedName);
  }
}