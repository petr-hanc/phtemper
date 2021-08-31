package phtemper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class PeriodComputeTest {
	
    private static PeriodCompute periodCompute;

	@BeforeClass
	public static void setUp() throws Exception {
		periodCompute = new PeriodCompute(null);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testLongestPeriodInList() {
		PeriodD period;
		List<Temper> tempers = new ArrayList<Temper>();
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		tempers.add(new Temper(LocalDateTime.parse("2105-12-31T11:30:00"), -9f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T00:00:01"), 5f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-03T10:00:00"), 10f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-05T10:00:00"), -10.01f));
		
		period = periodCompute.longestPeriodInList(-10f, 10f, tempers);
		assertThat(period, equalTo(new PeriodD(LocalDate.parse("2105-12-31"), LocalDate.parse("2106-01-03"))));
	}
	
	@Test
	public void testLongestPeriodInList_notOrderedTempers() {
		PeriodD period;
		List<Temper> tempers = new ArrayList<Temper>();
		tempers.add(new Temper(LocalDateTime.parse("2106-01-05T10:00:00"), -10.01f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T00:00:01"), 5f));
		tempers.add(new Temper(LocalDateTime.parse("2105-12-31T11:30:00"), -9f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-03T10:00:00"), 10f));
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		
		period = periodCompute.longestPeriodInList(-10f, 10f, tempers);
		assertThat(period, equalTo(new PeriodD(LocalDate.parse("2105-12-31"), LocalDate.parse("2106-01-03"))));
	}
	
	@Test
	public void testLongestPeriodInList_periodAtEnd() {
		PeriodD period;
		List<Temper> tempers = new ArrayList<Temper>();
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		tempers.add(new Temper(LocalDateTime.parse("2105-12-31T11:30:00"), -9f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T00:00:01"), 5f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-03T10:00:00"), 10f));
		
		period = periodCompute.longestPeriodInList(-10f, 10f, tempers);
		assertThat(period, equalTo(new PeriodD(LocalDate.parse("2105-12-31"), LocalDate.parse("2106-01-03"))));
	}
	
	@Test
	public void testLongestPeriodInList_morePeriods() {
		PeriodD period;
		List<Temper> tempers = new ArrayList<Temper>();
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		tempers.add(new Temper(LocalDateTime.parse("2105-12-31T11:30:00"), -9f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T00:00:01"), 5f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T15:00:00"), 30f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-03T10:00:00"), 10f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-05T10:00:00"), -10.01f));
		
		period = periodCompute.longestPeriodInList(-10f, 10f, tempers);
		assertThat(period, equalTo(new PeriodD(LocalDate.parse("2105-12-31"), LocalDate.parse("2106-01-01"))));
	}
	
	@Test
	public void testLongestPeriodInList_badTemperRange() {
		PeriodD period;
		List<Temper> tempers = new ArrayList<Temper>();
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		tempers.add(new Temper(LocalDateTime.parse("2105-12-31T11:30:00"), -9f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T00:00:01"), 5f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T15:00:00"), 30f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-03T10:00:00"), 10f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-05T10:00:00"), -10.01f));
		
		period = periodCompute.longestPeriodInList(10f, -10f, tempers);
		assertNull(period);
	}
	
	@Test
	public void testLongestPeriodInList_emptyTempers() {
		PeriodD period;
		List<Temper> tempers = new ArrayList<Temper>();
		
		period = periodCompute.longestPeriodInList(-10f, 10f, tempers);
		assertNull(period);
	}
	
	@Test
	public void testLongestPeriodInList_nullInputs() {
		PeriodD period;
		
		period = periodCompute.longestPeriodInList(null, null, null);
		assertNull(period);
	}

	@Ignore("Temporary - making test development faster")
	@Test
	public void testLongestPeriod() {
		fail("Not yet implemented"); // TODO
	}

	@Ignore("Temporary - making test development faster")
	@Test
	public void testLongestPeriodWithTime() {
		fail("Not yet implemented"); // TODO
	}

}
