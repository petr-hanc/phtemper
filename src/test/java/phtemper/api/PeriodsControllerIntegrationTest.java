package phtemper.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import phtemper.Temper;
import phtemper.TemperRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")

public class PeriodsControllerIntegrationTest {
	
    @Autowired
    private TemperRepository repository;
    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
    
    @Before
    public void setUp() {
		List<Temper> tempers = new ArrayList<Temper>();
		tempers.add(new Temper(LocalDateTime.parse("2021-08-01T11:30:00"), 25.4F));
		tempers.add(new Temper(LocalDateTime.parse("2021-07-31T04:13:00"), 9.8F));
		tempers.add(new Temper(LocalDateTime.parse("2021-07-31T09:09:00"), 16.8F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-03T05:15:00"), 5.0F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-04T08:00:00"), 15.0F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-13T12:00:00"), 25.0F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-14T12:00:01"), 25.01F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-15T13:20:00"), 17F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-25T12:00:00"), 17F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-27T11:30:00"), 35F));
		repository.saveAll(tempers);
    }
    
	@After
	public void tearDown() throws Exception {
		repository.deleteAll();
	}
    
    /** adds a temperature to test data to make 2 longest periods */
    public void prepareTestContext_make2PeriodsSameLength() {
    	Temper temper = new Temper(LocalDateTime.parse("2021-08-14T11:00:00"), 20F);
    	repository.save(temper);
    }

    @Test
    public void testLongestPeriod() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
          createURLWithPort("/periods/period?lowTemp=15&hiTemp=25"), HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        String expected = "{\"fromDate\":\"2021-08-15\",\"toDate\":\"2021-08-25\"}";
        System.err.println(response);	// DEBUG
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }  
    
    @Test
	public void testLongestPeriodWithTime() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
          createURLWithPort("/periods/periodTime?lowTemp=15&hiTemp=25&fromTime=08:00&toTime=12:00"), HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        String expected = "{\"fromDate\":\"2021-08-04\",\"toDate\":\"2021-08-25\"}";
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

}
