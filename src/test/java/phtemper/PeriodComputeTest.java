package phtemper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PeriodComputeTest {
	
    private static PeriodCompute periodCompute;
    private static TemperRepository repositMock;
    private static PeriodD period;
    private static List<Temper> tempers;
    

	@BeforeClass
	public static void setUpClass() throws Exception {
		periodCompute = new PeriodCompute(null);
	}
	
	@Before
	public void setUp() throws Exception {
		tempers = new ArrayList<Temper>();
		repositMock = EasyMock.createStrictMock("mockRepo", TemperRepository.class);
		EasyMock.expect(repositMock.findAll()).andReturn(tempers);
		EasyMock.replay(repositMock);
		
	}

	@Test
	public void testLongestPeriodInList() {
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
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		tempers.add(new Temper(LocalDateTime.parse("2105-12-31T11:30:00"), -9f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T00:00:01"), 5f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-03T10:00:00"), 10f));
		
		period = periodCompute.longestPeriodInList(-10f, 10f, tempers);
		assertThat(period, equalTo(new PeriodD(LocalDate.parse("2105-12-31"), LocalDate.parse("2106-01-03"))));
	}
	
	@Test
	public void testLongestPeriodInList_morePeriods() {
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
		period = periodCompute.longestPeriodInList(-10f, 10f, tempers);
		assertNull(period);
	}
	
	@Test
	public void testLongestPeriodInList_nullInputs() {
		period = periodCompute.longestPeriodInList(null, null, null);
		assertNull(period);
	}

}
