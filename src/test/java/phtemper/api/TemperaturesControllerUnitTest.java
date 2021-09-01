package phtemper.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import phtemper.PhtemperApplication;
import phtemper.Temper;
import phtemper.TemperRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.easymock.EasyMock;


/*
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
*/
/*
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
*/
/*
@TestPropertySource(locations = "classpath:application-unittest.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
*/
public class TemperaturesControllerUnitTest {
	
	private MockMvc mockMvc;
	private ObjectMapper mapper = JsonMapper.builder()
		    .findAndAddModules()
		    .build();
    private static final String CONTENT_TYPE = "application/json";
    private static TemperRepository repositMock;
    private static List<Temper> tempers;
    //private static TemperaturesController temperController;
    //@Value("${spring.jackson.serialization.WRITE_DATES_AS_TIMESTAMPS}")
    private String jacksonSer;

	@Before
	public void setUp() throws Exception {
		tempers = new ArrayList<Temper>();
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		repositMock = EasyMock.createStrictMock("mockRepo", TemperRepository.class);
		//EasyMock.expect(repositMock.findAll()).andReturn(tempers);
		EasyMock.expect(repositMock.findById(1L)).andReturn(Optional.ofNullable(tempers.get(0)));
		EasyMock.replay(repositMock);
		//temperController = new TemperaturesController(repositMock);
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
		//System.out.println(this.mockMvc.perform(get("/temperatures/1")));
		
		
		this.mockMvc.perform(get("/temperatures/1")).andExpect(status().isOk()).andExpect(content().contentType(CONTENT_TYPE))
			.andExpect(jsonPath("$.timeStamp").value("2105-12-15T11:30:00"));
		//.andExpect(jsonPath("$.timeStamp").value(LocalDateTime.parse("2105-12-15T11:30:00")));
		
		//this.mockMvc.perform(get("/temperatures/1")).andExpect(status().isOk()).andExpect(content().contentType(CONTENT_TYPE))
		//.andExpect(jsonPath("$.timeStamp").value(LocalDateTime.parse("2105-12-15T11:30:00").toString()));
		//.andExpect(content().json(convertObjectToJsonString(tempers.get(0))));
		//.andExpect(content().json("{'id': null,'timeStamp': '2105-12-15T11:30','temper': -15.0}"));
		//.andExpect(jsonPath("$.timeStamp", is("2105-12-15T11:30")));
		
		// {"id":,"dateTime": "2105-12-15T11:30","temper": -15.0}
		/*
		System.out.println("Ma vratit: " + Optional.ofNullable(tempers.get(0)));
		MvcResult result = this.mockMvc.perform(get("/temperatures/1")).andReturn();
		String resultStr = result.getResponse().getContentAsString();
		System.out.println(resultStr);
		Temper temper = mapper.readValue(resultStr, Temper.class);
		System.out.println(temper);
		System.out.println(jacksonSer);
	    */
	}

	@Test
	public void testUpdateTemper() {
	}

	@Test
	public void testDelTemper() {
	}

}
