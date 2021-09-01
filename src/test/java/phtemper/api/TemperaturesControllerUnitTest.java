package phtemper.api;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import phtemper.Temper;
import phtemper.TemperRepository;

import static org.easymock.EasyMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
@RunWith(SpringRunner.class)
@WebMvcTest(TemperaturesController.class)
*/
public class TemperaturesControllerUnitTest {
	
	private MockMvc mockMvc;
    private static final String CONTENT_TYPE = "application/json";
    private static TemperRepository repositMock;
    private static List<Temper> tempers;
    private static ObjectMapper objectMapper;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    	objectMapper = new ObjectMapper();
    	objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    	objectMapper.registerModule(new JavaTimeModule());
    }

	@Before
	public void setUp() throws Exception {
		tempers = new ArrayList<Temper>();
		repositMock = createStrictMock("mockRepo", TemperRepository.class);
		this.mockMvc = MockMvcBuilders.standaloneSetup(new TemperaturesController(repositMock)).build();
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testAllTempers() throws Exception {
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		tempers.add(new Temper(LocalDateTime.parse("2105-12-31T11:30:00"), -9f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T00:00:01"), 5.01f));
		Long id = 1L;
		for (Temper temper : tempers) {
			temper.setId(id++);
		}
		expect(repositMock.findAll()).andReturn(tempers);
		replay(repositMock);
		
		this.mockMvc.perform(get("/temperatures")).andExpect(status().isOk()).andExpect(content().contentType(CONTENT_TYPE))
		.andExpect(content().string(
				"[{\"id\":1,\"timeStamp\":\"2105-12-15T11:30:00\",\"temper\":-15.0},{\"id\":2,\"timeStamp\":\"2105-12-31T11:30:00\",\"temper\":-9.0},"
				+ "{\"id\":3,\"timeStamp\":\"2106-01-01T00:00:01\",\"temper\":5.01}]"
				));
		
		/*
		MvcResult result = this.mockMvc.perform(get("/temperatures")).andExpect(status().isOk()).andReturn();
		String resultStr = result.getResponse().getContentAsString();
		System.err.println(resultStr);
		*/
		
		verify(repositMock);
		
	}

	@Test
	public void testAddTemper() throws Exception {
		Temper temper = new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f);
		expect(repositMock.save(eq(temper)))
			.andReturn(temper);
		replay(repositMock);
		
		mockMvc.perform(
				post("/temperatures")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(temper))
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isCreated())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	        .andExpect(content().string(
					"{\"id\":null,\"timeStamp\":\"2105-12-15T11:30:00\",\"temper\":-15.0}"
					));
		verify(repositMock);
		
		/*
		  		String resultStr = mockMvc.perform(
				post("/temperatures")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(temper))
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isCreated())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	        .andReturn().getResponse().getContentAsString()
			;
		System.err.println(resultStr);
		 */
	}

	@Test
	public void testGetTemper() throws Exception {
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		expect(repositMock.findById(1L)).andReturn(Optional.ofNullable(tempers.get(0)));
		replay(repositMock);
		
		this.mockMvc.perform(get("/temperatures/1")).andExpect(status().isOk()).andExpect(content().contentType(CONTENT_TYPE))
			.andExpect(jsonPath("$.timeStamp").value("2105-12-15T11:30:00"))
			.andExpect(jsonPath("$.temper").value("-15.0"));
		verify(repositMock);
	}
	
	@Test
	public void testGetTemper_nonExistentId_badRequest() throws Exception {
		expect(repositMock.findById(60000L)).andReturn(Optional.ofNullable(null));
		replay(repositMock);
		mockMvc.perform(get("/temperatures/60000")).andExpect(status().isBadRequest());
		verify(repositMock);
		
		/*
		//MvcResult result = this.mockMvc.perform(get("/temperatures/60000")).andExpect(status().isBadRequest()).andReturn();
		String resultStr = result.getResponse().getContentAsString();
		System.err.println(resultStr);
		*/
	}

	@Test
	public void testUpdateTemper() throws Exception {
		Temper temper = new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f);
		temper.setId(1L);
		Temper temperNew = new Temper(LocalDateTime.parse("2106-02-28T22:30:00"), 10.02f);
		Temper temperNewDb = objectMapper.readValue(asJsonString(temperNew), Temper.class);		// deep copy
		temperNewDb.setId(1L);
		expect(repositMock.findById(1L))
			.andReturn(Optional.ofNullable(temper));
		expect(repositMock.save(eq(temperNewDb)))
			.andReturn(temperNewDb);
		replay(repositMock);
		
		mockMvc.perform(
				patch("/temperatures/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(temperNew))
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	        .andExpect(content().string(
					"{\"id\":1,\"timeStamp\":\"2106-02-28T22:30:00\",\"temper\":10.02}"
					));
		verify(repositMock);
	}
	
	@Test
	public void testUpdateTemper_nonExistentId_badRequest() throws Exception {
		expect(repositMock.findById(60000L)).andReturn(Optional.ofNullable(null));
		replay(repositMock);
		Temper temperNew = new Temper(LocalDateTime.parse("2106-02-28T22:30:00"), 10.02f);
		mockMvc.perform(
				patch("/temperatures/60000")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(temperNew))
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest());
		verify(repositMock);
	}

	@Test
	public void testDelTemper() throws Exception {
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return objectMapper.writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

}
