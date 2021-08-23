package phtemper;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
  locations = "classpath:application-integrationtest.properties")
public class PeriodComputeIntegrTest {
	
    @Autowired
    TemperRepository repository;
    @Autowired
    PeriodCompute periodCompute;
    @LocalServerPort
    private int port;

	@Test
	public void testLongestPeriod() {
		System.out.println(repository.findAll());
		PeriodD period = periodCompute.longestPeriod(10F, 30F);
		System.out.println(period);
		fail("Not yet implemented");
	}
	
	/*
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
    */
}
