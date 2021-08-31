package phtemper.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

public class TemperaturesControllerUnitTest {
	
	private MockMvc mockMvc;
    private static final String CONTENT_TYPE = "application/json";
    private static TemperRepository repositMock;
    private static List<Temper> tempers;
    //private static TemperaturesController temperController;

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
		
		/*
		this.mockMvc.perform(get("/temperatures/1")).andExpect(status().isOk()).andExpect(content().contentType(CONTENT_TYPE))
			.andExpect(jsonPath("$.timeStamp").value(LocalDateTime.parse("2105-12-15T11:30:00")));
			*/
		//this.mockMvc.perform(get("/temperatures/1")).andExpect(status().isOk()).andExpect(content().contentType(CONTENT_TYPE))
		//.andExpect(jsonPath("$.timeStamp").value(LocalDateTime.parse("2105-12-15T11:30:00").toString()));
		//.andExpect(content().json(convertObjectToJsonString(tempers.get(0))));
		//.andExpect(content().json("{'id': null,'timeStamp': '2105-12-15T11:30','temper': -15.0}"));
		//.andExpect(jsonPath("$.timeStamp", is("2105-12-15T11:30")));
		
		// {"id":,"dateTime": "2105-12-15T11:30","temper": -15.0}
		
		MvcResult result = this.mockMvc.perform(get("/temperatures/1")).andReturn();
		System.out.println(result.getResponse().getContentAsString());
	    
	}

	@Test
	public void testUpdateTemper() {
	}

	@Test
	public void testDelTemper() {
	}

}
