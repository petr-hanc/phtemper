package phtemper.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
		verify(repositMock);
	        
		/*
		this.mockMvc.perform(post("/greetWithPostAndFormData").param("id", "1").param("name", "John Doe")).andDo(print()).andExpect(status().isOk()).andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(jsonPath("$.message").value("Hello World John Doe!!!")).andExpect(jsonPath("$.id").value(1));
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
		this.mockMvc.perform(get("/temperatures/60000")).andExpect(status().isBadRequest()).andReturn();
		verify(repositMock);
		
		/*
		//MvcResult result = this.mockMvc.perform(get("/temperatures/60000")).andExpect(status().isBadRequest()).andReturn();
		String resultStr = result.getResponse().getContentAsString();
		System.err.println(resultStr);
		*/
	}

	@Test
	public void testUpdateTemper() throws Exception {
	}

	@Test
	public void testDelTemper() throws Exception {
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        //return new ObjectMapper().writeValueAsString(obj); // debug
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	        mapper.registerModule(new JavaTimeModule());
	        return mapper.writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

}
