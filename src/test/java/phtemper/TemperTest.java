package phtemper;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

public class TemperTest {

	@Test
	public void compareTest() {
		Temper temper1 = new Temper(LocalDateTime.parse("2021-08-31T08:00:00"), -10.5F);
		Temper temper2 = new Temper(LocalDateTime.parse("2021-09-01T07:00:00"), -20.5F);
		assertTrue("Comparation shall be negative.", temper1.compareTo(temper2) < 0);
	}

}
