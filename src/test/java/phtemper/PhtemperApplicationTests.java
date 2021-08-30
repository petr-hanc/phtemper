package phtemper;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import phtemper.api.TemperaturesController;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PhtemperApplicationTests {
	
	/*
    @Autowired
    TemperRepository repository;
    @Autowired
    PeriodCompute periodCompute;
    */
	@Autowired
	TemperaturesController temperController; 

	@Test
	void contextLoads() {
		assertThat(temperController).isNotNull();
	}

}
