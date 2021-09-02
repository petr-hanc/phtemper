package phtemper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/** Integration test of PeriodCompute - longestPeriod() and longestPeriodWithTime() */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class PeriodComputeIntegrTest {
	
    @Autowired
    private TemperRepository repository;
    @Autowired
    private PeriodCompute periodCompute;
    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    
    @Before
    public void setUp() {
    	repository.deleteAll();
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
    
    /** adds a temperature to test data to make 2 longest periods */
    public void prepareTestContext_make2PeriodsSameLength() {
    	Temper temper = new Temper(LocalDateTime.parse("2021-08-14T11:00:00"), 20F);
    	repository.save(temper);
    }
    
	@Test
	public void testLongestPeriodDb() {
		System.out.println("testLongestPeriod"); // debug
		System.out.println(repository.findAll());// debug
		PeriodD period = periodCompute.longestPeriod(15F, 25F);
		assertThat(period, equalTo(new PeriodD(LocalDate.parse("2021-08-15"), LocalDate.parse("2021-08-25"))));
	}

	@Test
	public void testLongestPeriodWithTimeDb() {
		System.out.println("testLongestPeriodWithTime()");// debug
		//System.out.println(repository.findAll());// debug
		PeriodD period = periodCompute.longestPeriodWithTime(15F, 25F, LocalTime.parse("08:00"), LocalTime.parse("12:00"));		
		assertThat(period, equalTo(new PeriodD(LocalDate.parse("2021-08-04"), LocalDate.parse("2021-08-25"))));
	}
    
	@Test
	public void testLongestPeriodDb_2LongestPeriods_olderOne() {
		prepareTestContext_make2PeriodsSameLength();
		PeriodD period = periodCompute.longestPeriod(15F, 25F);
		//System.out.println(period);
		assertThat(period, equalTo(new PeriodD(LocalDate.parse("2021-08-04"), LocalDate.parse("2021-08-14"))));
	}
	
}
