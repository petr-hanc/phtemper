package phtemper.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import phtemper.Temper;
import phtemper.TemperRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.easymock.EasyMock;


public class TemperaturesControllerUnitTest {
	
	private MockMvc mockMvc;
    private static final String CONTENT_TYPE = "application/json";
    private static TemperRepository repositMock;
    private static List<Temper> tempers;

	@Before
	public void setUp() throws Exception {
		tempers = new ArrayList<Temper>();
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		repositMock = EasyMock.createStrictMock("mockRepo", TemperRepository.class);
		//EasyMock.expect(repositMock.findAll()).andReturn(tempers);
		EasyMock.expect(repositMock.findById(1L)).andReturn(Optional.ofNullable(tempers.get(0)));
		EasyMock.replay(repositMock);
		this.mockMvc = MockMvcBuilders.standaloneSetup(new TemperaturesController(repositMock)).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAllTempers() {
	}

	@Test
	public void testAddTemper() {
	}

	@Test
	public void testGetTemper() throws Exception {
		this.mockMvc.perform(get("/temperatures/1")).andExpect(status().isOk()).andExpect(content().contentType(CONTENT_TYPE))
			.andExpect(jsonPath("$.timeStamp").value("2105-12-15T11:30:00"));
	}

	@Test
	public void testUpdateTemper() {
	}

	@Test
	public void testDelTemper() {
	}

}
