package phtemper;

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
		
		//List<Temper> tempers = new ArrayList<Temper>();
		//tempers.add(new Temper(LocalDateTime.parse("2021-08-01T10:05"), 20.3F));
		//repository.saveAll(tempers);
	}

}
