package be.doji.productivity.trambu.timetracking.infra.access;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import be.doji.productivity.trambu.timetracking.domain.Occupation;
import be.doji.productivity.trambu.timetracking.domain.OccupationRepository;
import be.doji.productivity.trambu.timetracking.domain.TimeServiceRule;
import be.doji.productivity.trambu.timetracking.infra.TimetrackingApplication;
import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(CreateOccupationController.class)
@ContextConfiguration(classes= TimetrackingApplication.class)
public class CreateOccupationControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private OccupationRepository occupationRepository;

  @Rule
  public TimeServiceRule timeRule = new TimeServiceRule();

  private static final UUID REFERENCE = UUID.randomUUID();
  private TestFlow flow;

  @Before
  public void setUp() {
    this.flow = new TestFlow(mvc);
  }

  @Test
  public void activityCreated_createsOccupation() throws Exception {
    //given values
    UUID reference = UUID.randomUUID();
    String title = "Occupation to be created";

    //when
    MockHttpServletResponse result = flow.whenCallingCreateOccupation(title, reference);

    //then
    assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
    ArgumentCaptor captor = ArgumentCaptor.forClass(Occupation.class);
    verify(occupationRepository).save((Occupation) captor.capture(    ));

  }

}