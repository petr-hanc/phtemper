package phtemper.api;

import static org.easymock.EasyMock.createStrictMock;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import phtemper.PeriodCompute;
import phtemper.PeriodD;
import phtemper.Temper;
import phtemper.TemperRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")

public class TemperaturesControllerIntegrationTest {
	
    @Autowired
    private TemperRepository repository;
    private List<Temper> tempers;
    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> request;
    
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		tempers = new ArrayList<Temper>();
		request = new HttpEntity<String>(null, headers);
	}

	@After
	public void tearDown() throws Exception {
		repository.deleteAll();
	}

	@Test
	@Sql(scripts="classpath:cleanup.sql",executionPhase=Sql.ExecutionPhase.AFTER_TEST_METHOD)	// reset of id sequence in DB
	public void testAllTempers() throws Exception {
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		tempers.add(new Temper(LocalDateTime.parse("2105-12-31T11:30:00"), -9f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T00:00:01"), 5.01f));
		repository.saveAll(tempers);
		
        ResponseEntity<String> response = restTemplate.exchange(
          createURLWithPort("/temperatures"), HttpMethod.GET, request, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        String expected = "[{\"id\":1,\"timeStamp\":\"2105-12-15T11:30:00\",\"temper\":-15.0},{\"id\":2,\"timeStamp\":\"2105-12-31T11:30:00\",\"temper\":-9.0},"
				+ "{\"id\":3,\"timeStamp\":\"2106-01-01T00:00:01\",\"temper\":5.01}]";
        //System.err.println(response);	// DEBUG
        JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	
	@Test
	@Sql(scripts="classpath:cleanup.sql",executionPhase=Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testAddTemper() throws Exception {
		Temper newTemper = new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f);
		HttpEntity<Temper> request = new HttpEntity<>(newTemper, headers);
		
        ResponseEntity<Temper> response = restTemplate.exchange(
          createURLWithPort("/temperatures"), HttpMethod.POST, request, Temper.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        Temper temperReceived = response.getBody();
        newTemper.setId(1L);
        assertThat(temperReceived, equalTo(newTemper));
	}
	
	@Test
	@Sql(scripts="classpath:cleanup.sql",executionPhase=Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testGetTemper() throws Exception {
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		tempers.add(new Temper(LocalDateTime.parse("2105-12-31T11:30:00"), -9f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T00:00:01"), 5.01f));
		repository.saveAll(tempers);
		// System.err.println(repository.findAll());	// debug
		
        ResponseEntity<String> response = restTemplate.exchange(
          createURLWithPort("/temperatures/3"), HttpMethod.GET, request, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        String expected = "{\"id\":3,\"timeStamp\":\"2106-01-01T00:00:01\",\"temper\":5.01}";
        // System.err.println(response);	// DEBUG
        JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	@Sql(scripts="classpath:cleanup.sql",executionPhase=Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testUpdateTemper() throws Exception {
	}

	@Test
	@Sql(scripts="classpath:cleanup.sql",executionPhase=Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testDelTemper() throws Exception {
	}

}
