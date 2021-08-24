package phtemper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PhtemperApplication implements CommandLineRunner {
	
    @Autowired
    TemperRepository repository;
    @Autowired
    PeriodCompute periodCompute;
	

	public static void main(String[] args) {
		SpringApplication.run(PhtemperApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		/*
		List<Temper> tempers = new ArrayList<Temper>();
		tempers.add(new Temper(LocalDateTime.parse("2021-08-01T11:30:00"), 25.4F));
		tempers.add(new Temper(LocalDateTime.parse("2021-07-31T04:13:00"), 9.8F));
		tempers.add(new Temper(LocalDateTime.parse("2021-07-31T09:09:00"), 16.8F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-03T05:15:00"), 5.0F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-05T13:20:00"), 17F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-15T11:40:00"), 17F));
		tempers.add(new Temper(LocalDateTime.parse("2021-08-20T11:40:00"), 35F));
		repository.saveAll(tempers);
		*/
	}

}
