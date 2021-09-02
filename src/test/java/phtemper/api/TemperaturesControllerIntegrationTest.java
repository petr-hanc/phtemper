package phtemper.api;

import static org.easymock.EasyMock.createStrictMock;
import static org.junit.Assert.*;

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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import phtemper.PeriodCompute;
import phtemper.Temper;
import phtemper.TemperRepository;

@RunWith(SpringRunner.class)
//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")

public class TemperaturesControllerIntegrationTest {
	
    @Autowired
    private TemperRepository repository;
    private List<Temper> tempers;
    @Autowired
    private PeriodCompute periodCompute;
    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		tempers = new ArrayList<Temper>();
	}

	@After
	public void tearDown() throws Exception {
		repository.deleteAll();
	}

	@Test
	public void testAllTempers() throws Exception {
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		tempers.add(new Temper(LocalDateTime.parse("2105-12-31T11:30:00"), -9f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T00:00:01"), 5.01f));
		repository.saveAll(tempers);
		
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
          createURLWithPort("/temperatures"), HttpMethod.GET, entity, String.class);
        String expected = "[{\"id\":1,\"timeStamp\":\"2105-12-15T11:30:00\",\"temper\":-15.0},{\"id\":2,\"timeStamp\":\"2105-12-31T11:30:00\",\"temper\":-9.0},"
				+ "{\"id\":3,\"timeStamp\":\"2106-01-01T00:00:01\",\"temper\":5.01}]";
        System.err.println(response);	// DEBUG
        JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	
	@Test
	public void testAddTemper() throws Exception {
	}

	@Test
	public void testGetTemper() throws Exception {
	}

	@Test
	public void testUpdateTemper() throws Exception {
	}

	@Test
	public void testDelTemper() throws Exception {
	}

}
