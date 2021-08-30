package phtemper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.lessThan;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class TemperTest {

	@Test
	public void compareTest() {
		Temper temper1 = new Temper(LocalDateTime.parse("2021-08-31T08:00:00"), -10.5F);
		Temper temper2 = new Temper(LocalDateTime.parse("2021-09-01T07:00:00"), -20.5F);
		System.out.println(temper1.compareTo(temper2));
		assertThat(temper1.compareTo(temper2), lessThan(0));
		//assertTrue(5 > 1);
		
	}

}
