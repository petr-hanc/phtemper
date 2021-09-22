package phtemper.api;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import phtemper.PeriodCompute;
import phtemper.Temper;
import phtemper.TemperRepository;

public class PeriodsControllerUnitTest {
	
	private MockMvc mockMvc;
    private static final String CONTENT_TYPE = "application/json";
    private static TemperRepository repositMock;
    private static List<Temper> tempers;

	@Before
	public void setUp() throws Exception {
		tempers = new ArrayList<Temper>();
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		tempers.add(new Temper(LocalDateTime.parse("2105-12-31T11:30:00"), -9f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T00:00:01"), 5f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-01T15:00:00"), 30f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-03T10:00:00"), 10f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-05T10:00:00"), -10.01f));
		repositMock = createStrictMock("mockRepo", TemperRepository.class);
		expect(repositMock.findAll()).andReturn(tempers);
		replay(repositMock);
		mockMvc = MockMvcBuilders.standaloneSetup(
					new PeriodsController(new PeriodCompute(repositMock))
				).build();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLongestPeriod() throws Exception {
		mockMvc.perform(get("/periods/period?lowTemp=-10&hiTemp=10"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(CONTENT_TYPE))
			.andExpect(content().string(
					"{\"fromDate\":\"2105-12-31\",\"toDate\":\"2106-01-01\"}"
					));
		verify(repositMock);
	}
	
	@Test
	public void testGetLongestPeriod_outOfTemperRange_noContent() throws Exception {
		mockMvc.perform(get("/periods/period?lowTemp=35&hiTemp=40"))
			.andExpect(status().isNoContent());
		verify(repositMock);
	}
	
	public void setUpWithTime() throws Exception {
		tempers = new ArrayList<Temper>();
		tempers.add(new Temper(LocalDateTime.parse("2105-12-15T11:30:00"), -15f));
		tempers.add(new Temper(LocalDateTime.parse("2105-12-31T11:30:00"), -9f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-03T10:00:00"), 10f));
		tempers.add(new Temper(LocalDateTime.parse("2106-01-05T10:00:00"), -10.01f));
		repositMock = createStrictMock("mockRepo", TemperRepository.class);
		expect(repositMock.findByTimeRangeOrder(LocalTime.parse("10:00"), LocalTime.parse("14:00")))
			.andReturn(tempers);
		replay(repositMock);
		mockMvc = MockMvcBuilders.standaloneSetup(
					new PeriodsController(new PeriodCompute(repositMock))
				).build();
	}

	@Test
	public void testGetLongestPeriodWithTime() throws Exception {
		setUpWithTime();
		mockMvc.perform(get("/periods/periodTime?lowTemp=-10&hiTemp=10&fromTime=10:00&toTime=14:00"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(CONTENT_TYPE))
			.andExpect(content().string(
				"{\"fromDate\":\"2105-12-31\",\"toDate\":\"2106-01-03\"}"
				));
		verify(repositMock);
	}
	
	public void setUpEmptyTempersWithTime() throws Exception {
		tempers = new ArrayList<Temper>();
		repositMock = createStrictMock("mockRepo", TemperRepository.class);
		expect(repositMock.findByTimeRangeOrder(LocalTime.parse("14:00"), LocalTime.parse("10:00")))
			.andReturn(tempers);
		replay(repositMock);
		mockMvc = MockMvcBuilders.standaloneSetup(
					new PeriodsController(new PeriodCompute(repositMock))
				).build();
	}
	
	@Test
	public void testGetLongestPeriodWithTime_outOfTimeRange_noContent() throws Exception {
		setUpEmptyTempersWithTime();
		mockMvc.perform(get("/periods/periodTime?lowTemp=-10&hiTemp=10&fromTime=14:00&toTime=10:00"))
			.andExpect(status().isNoContent());
		verify(repositMock);
	}
	
	@Test
	public void testGetLongestPeriodWithTime_badFromTime_badRequest() throws Exception {
		setUpEmptyTempersWithTime();
		mockMvc.perform(get("/periods/periodTime?lowTemp=5&hiTemp=10&fromTime=blabla&toTime=10:00"))
			.andExpect(status().isBadRequest());
	}

}
